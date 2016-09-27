/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.rotate;

import java.math.BigDecimal;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 11 mai 2016
 */
public class RotateModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider>{
	/** The rotation angle */
	private Angle rotationAngle = Angle.valueOf("45", AngleUnit.DEGREE_ANGLE);
	/** The rotation axis */
	private EnumControllerAxis rotationAxis = EnumControllerAxis.Z_POSITIVE;
	/** Rotation matrix */
	private Matrix3d rotationMatrix;
	/**
	 * Constructeur
	 */
	public RotateModifier() {
		super("Rotate");
	}

//	fix wrap and autolevel modifier ?
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {
		return rotationAngle != null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier#applyModifier(org.goko.core.gcode.element.IGCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		rotationMatrix = null;
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = getRS274NGCService().getInstructions(localContext, source);
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = getRS274NGCService().getIterator(sourceInstructionSet, localContext);
	
		while(iterator.hasNext()){
			GCodeContext preContext = iterator.getContext();
			AbstractInstruction instr = iterator.next();
			
			if(instr.getType() == InstructionType.STRAIGHT_FEED
				|| instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
				AbstractStraightInstruction straightInstruction = (AbstractStraightInstruction) instr;
				rotateStraightInstruction((AbstractStraightInstruction)straightInstruction, preContext);
			}else if(instr.getType() == InstructionType.ARC_FEED){
				rotateArcInstruction((ArcFeedInstruction)instr, preContext);				
			}
		}
		GCodeProvider result = getRS274NGCService().getGCodeProvider(localContext, sourceInstructionSet);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);
		}
	}

	/**
	 * @param straightInstruction
	 * @param preContext
	 */
	private void rotateStraightInstruction(AbstractStraightInstruction instr, GCodeContext preContext)  throws GkException{
		Unit<Length> unit = preContext.getUnit().getUnit();
		GCodeContext postContext = new GCodeContext(preContext);
		instr.apply(postContext);
		Tuple6b tuple = postContext.getPosition();
		Vector3d tuple3d = tuple.toVector3d(unit);
		rotate(tuple3d);
		
		instr.setX( Length.valueOf(BigDecimal.valueOf(tuple3d.x), unit));
		instr.setY( Length.valueOf(BigDecimal.valueOf(tuple3d.y), unit));
		instr.setZ( Length.valueOf(BigDecimal.valueOf(tuple3d.z), unit));
	}

	private void calculateRotationMatrix() throws GkException{
		if(rotationMatrix == null){
			rotationMatrix = new Matrix3d();
			rotationMatrix.setIdentity();
			switch(rotationAxis){
				case X_POSITIVE: rotationMatrix.rotX(rotationAngle.doubleValue(AngleUnit.RADIAN));
				break;
				case Y_POSITIVE: rotationMatrix.rotY(rotationAngle.doubleValue(AngleUnit.RADIAN));
				break;
				case Z_POSITIVE: rotationMatrix.rotZ(rotationAngle.doubleValue(AngleUnit.RADIAN));
				break;
				default: throw new GkTechnicalException("Unsupported rotation axis "+rotationAxis.getCode());
			}
		}
	}
	private void rotate(Tuple3d tuple3d) throws GkException{				
		calculateRotationMatrix();		
		rotationMatrix.transform(tuple3d);
	}
	
	private void checkRotationPlane(GCodeContext preContext) throws GkException {
		switch(preContext.getPlane()){
			case XY_PLANE: 
				if(rotationAxis != EnumControllerAxis.Z_POSITIVE){
					throw new GkFunctionalException("An arc motion in the XY plane can only be rotated around Z");
				}
				break;
			case YZ_PLANE: 
				if(rotationAxis != EnumControllerAxis.X_POSITIVE){
					throw new GkFunctionalException("An arc motion in the YZ plane can only be rotated around X");
				}
				break;
			case XZ_PLANE: 
				if(rotationAxis != EnumControllerAxis.Y_POSITIVE){
					throw new GkFunctionalException("An arc motion in the XZ plane can only be rotated around Y");
				}
				break;
			default: throw new GkTechnicalException("Not supported "+preContext.getPlane());
		}
	}
	
	private void rotateArcInstruction(ArcFeedInstruction instr, GCodeContext preContext) throws GkException {
		// Make sure the arc is in a plane that can be rotated
		checkRotationPlane(preContext);
		
		Arc3b arc = InstructionUtils.getArc(preContext, instr);
		Unit<Length> unit = preContext.getUnit().getUnit();
		Point3d start = arc.getStart().toPoint3d(unit);
		Point3d end = arc.getEnd().toPoint3d(unit);
		Point3d center = arc.getCenter().toPoint3d(unit);
		rotate(start);
		rotate(end);
		rotate(center);
	//pas bon
		switch(preContext.getDistanceMode()){
			case ABSOLUTE:
				instr.setX(Length.valueOf(BigDecimal.valueOf(end.x), unit));
				instr.setY(Length.valueOf(BigDecimal.valueOf(end.y), unit));
				instr.setZ(Length.valueOf(BigDecimal.valueOf(end.z), unit));				
			break;
			case RELATIVE:
				instr.setX(Length.valueOf(BigDecimal.valueOf(end.x - start.x), unit));
				instr.setY(Length.valueOf(BigDecimal.valueOf(end.y - start.y), unit));
				instr.setZ(Length.valueOf(BigDecimal.valueOf(end.z - start.z), unit));
			break;
			default: throw new GkTechnicalException("Not supported "+preContext.getDistanceMode());
		}
		
		instr.setI(Length.valueOf(BigDecimal.valueOf(center.x - start.x), unit));
		instr.setJ(Length.valueOf(BigDecimal.valueOf(center.y - start.y), unit));
		instr.setK(Length.valueOf(BigDecimal.valueOf(center.z - start.z), unit));
//		
//		switch(preContext.getPlane()){
//			case XY_PLANE: 
//				instr.setFirstEnd(Length.valueOf(BigDecimal.valueOf(end.x), unit));
//				instr.setSecondEnd(Length.valueOf(BigDecimal.valueOf(end.y), unit));
//				instr.setFirstAxis(Length.valueOf(BigDecimal.valueOf(center.x), unit));
//				instr.setSecondAxis(Length.valueOf(BigDecimal.valueOf(center.y), unit));
//				instr.setAxisEndPoint(Length.valueOf(BigDecimal.valueOf(end.z), unit));
//				break;
//			case YZ_PLANE: 
//				instr.setFirstEnd(Length.valueOf(BigDecimal.valueOf(end.y), unit));
//				instr.setSecondEnd(Length.valueOf(BigDecimal.valueOf(end.z), unit));
//				instr.setFirstAxis(Length.valueOf(BigDecimal.valueOf(center.y), unit));
//				instr.setSecondAxis(Length.valueOf(BigDecimal.valueOf(center.z), unit));
//				instr.setAxisEndPoint(Length.valueOf(BigDecimal.valueOf(end.x), unit));
//				break;
//			case XZ_PLANE: 
//				instr.setFirstEnd(Length.valueOf(BigDecimal.valueOf(end.z), unit));
//				instr.setSecondEnd(Length.valueOf(BigDecimal.valueOf(end.x), unit));
//				instr.setFirstAxis(Length.valueOf(BigDecimal.valueOf(center.z), unit));
//				instr.setSecondAxis(Length.valueOf(BigDecimal.valueOf(center.x), unit));
//				instr.setAxisEndPoint(Length.valueOf(BigDecimal.valueOf(end.y), unit));
//				break;
//			default: throw new GkTechnicalException("Not supported "+preContext.getPlane());
//		}
	}

	/**
	 * @return the rotationAngle
	 */
	public Angle getRotationAngle() {
		return rotationAngle;
	}

	/**
	 * @param rotationAngle the rotationAngle to set
	 */
	public void setRotationAngle(Angle rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	/**
	 * @return the rotationAxis
	 */
	public EnumControllerAxis getRotationAxis() {
		return rotationAxis;
	}

	/**
	 * @param rotationAxis the rotationAxis to set
	 */
	public void setRotationAxis(EnumControllerAxis rotationAxis) {
		this.rotationAxis = rotationAxis;
	}
}
