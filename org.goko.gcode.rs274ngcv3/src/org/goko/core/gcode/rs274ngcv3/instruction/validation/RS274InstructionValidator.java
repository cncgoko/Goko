/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.instruction.validation;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.i18n.I18n;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.utils.Location;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.validation.IValidationElement.ValidationSeverity;
import org.goko.core.gcode.element.validation.ValidationElement;
import org.goko.core.gcode.element.validation.ValidationResult;
import org.goko.core.gcode.rs274ngcv3.RS274GCodeValidationServiceImpl;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemDataInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 2 juil. 2017
 */
public class RS274InstructionValidator extends RS274InstructionVisitorAdapter{
	private ValidationResult validationTarget;
	private IGCodeProvider provider;
	private RS274GCodeValidationServiceImpl rs274gCodeValidationService;
	/**
	 * Constructor 
	 * @param rs274gCodeValidationServiceImpl 
	 */
	public RS274InstructionValidator(IGCodeProvider provider, RS274GCodeValidationServiceImpl rs274gCodeValidationServiceImpl) {
		this.validationTarget = new ValidationResult();
		this.provider = provider;
		this.rs274gCodeValidationService = rs274gCodeValidationServiceImpl;
	}

	/**
	 * @return
	 */
	public ValidationResult getResult() {
		return validationTarget;
	}
	
	private Location getLocation(AbstractInstruction instr) throws GkException{
		GCodeLine gcodeLine = provider.getLine(instr.getIdGCodeLine());
		return gcodeLine.getLocation();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter#visit(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction)
	 */
	@Override
	public void visit(GCodeContext context, ArcFeedInstruction arcInstruction) throws GkException{
		Location location = getLocation(arcInstruction);
		// Words verification
		switch (context.getPlane()) {
		case XY_PLANE:	
				if(arcInstruction.getX() == null && arcInstruction.getY() == null){
					//throw new GkFunctionalException("GCO-130", "X", "Y");
					validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "X", "Y")));
				}
				if(arcInstruction.getI() == null && arcInstruction.getJ() == null && arcInstruction.getRadius() == null){
					//throw new GkFunctionalException("GCO-130", "I", "J");
					validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "I", "J")));
				}
			break;
		case YZ_PLANE:	
			if(arcInstruction.getY() == null && arcInstruction.getZ() == null){
				//throw new GkFunctionalException("GCO-130", "Y", "Z");
				validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "Y", "Z")));
			}
			if(arcInstruction.getJ() == null && arcInstruction.getK() == null && arcInstruction.getRadius() == null){
				//throw new GkFunctionalException("GCO-130", "J", "K");
				validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "J", "K")));
			}
			break;
		case XZ_PLANE:
			if(arcInstruction.getZ() == null && arcInstruction.getX() == null){
				//throw new GkFunctionalException("GCO-130", "X", "Z");
				validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "X", "Z")));
			}
			if(arcInstruction.getK() == null && arcInstruction.getI() == null && arcInstruction.getRadius() == null){
				//throw new GkFunctionalException("GCO-130", "I", "K");
				validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("GCO-130", "I", "K")));
			}
			break;
		default: throw new GkTechnicalException("Not a valid plane in GCodeContext ["+context.getPlane()+"]");			
		}
		
		if(rs274gCodeValidationService.isArcToleranceCheckEnabled()){
			// Test arc specification error
			Tuple6b start = new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
			// Apply coordinate system offset
			Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
			start = start.add(offset);
	
			Tuple6b center 	= InstructionUtils.getCenterPoint(context, arcInstruction);
			Tuple6b end 	= InstructionUtils.getEndPoint(context, arcInstruction);
	
			switch (context.getPlane()) { // Just ignore the dimension along the normal of the plane
			case XY_PLANE: 
						end.setZ( center.getZ() );
						start.setZ( center.getZ() );
				break;
			case YZ_PLANE: 
					end.setX( center.getX() );
					start.setX( center.getX() );
				break;
			case XZ_PLANE: 
					end.setY( center.getY() );
					start.setY( center.getY() );
				break;
			}
			Length startCenterDistance 	= start.distance(center);
			Length endCenterDistance 	= end.distance(center);
			Length error = startCenterDistance.subtract(endCenterDistance).abs();
			Length errorMax = rs274gCodeValidationService.getArcTolerance();
			if(error.greaterThan(errorMax)){			
				validationTarget.addElement( new ValidationElement(ValidationSeverity.ERROR, location, I18n.get("gcode.error.arc.radius", GokoPreference.getInstance().format(error), GokoPreference.getInstance().format(errorMax))));
	//					la validation doit être faite sur l'instruction
	//					transformée en affichage uniquement dans l'editeur ?
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter#visit(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction)
	 */
	@Override
	public void visit(GCodeContext context, DwellInstruction instr) throws GkException {
		if(instr.getSeconds() < 0){
			validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, getLocation(instr), I18n.get("GCO-140")));
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter#visit(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemDataInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetCoordinateSystemDataInstruction instr) throws GkException {
		if(instr.getTargetCoordinateSystem() == null){
			validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, getLocation(instr), I18n.get("GCO-120")));
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter#visit(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetOriginOffsetInstruction instr) throws GkException {
		if(instr.getX() == null && instr.getY() == null && instr.getZ() == null && instr.getA() == null && instr.getB() == null && instr.getC() == null){
			//throw new GkFunctionalException("GCO-110", "G92");
			validationTarget.addElement(new ValidationElement(ValidationSeverity.ERROR, getLocation(instr), I18n.get("GCO-110", "G92")));
		}
	}
	
}
