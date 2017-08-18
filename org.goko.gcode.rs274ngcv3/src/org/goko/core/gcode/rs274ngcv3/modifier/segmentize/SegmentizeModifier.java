/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.segmentize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

/**
 * @author PsyKo
 * @date 16 janv. 2016
 */
public class SegmentizeModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider>{
	public static final String MODIFIER_NAME = "Segmentize";
	private Length chordalTolerance;
	
	/**
	 * Constructeur
	 */
	public SegmentizeModifier() {
		super(MODIFIER_NAME);
		this.chordalTolerance = Length.valueOf("0.1", LengthUnit.MILLIMETRE);
	}
	
	/**
	 * @param idGCodeProvider
	 * @param modifierName
	 */
	public SegmentizeModifier(Integer idGCodeProvider) {
		super(idGCodeProvider, MODIFIER_NAME);
		this.chordalTolerance = Length.valueOf("0.1", LengthUnit.MILLIMETRE);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {		
		return chordalTolerance != null && !chordalTolerance.equals(Length.ZERO);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier#applyModifier(org.goko.core.gcode.element.IGCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	protected void applyModifier(IGCodeProvider source, GCodeProvider target) throws GkException {
		GCodeContext localContext = new GCodeContext();
		InstructionProvider sourceInstructionSet = getRS274NGCService().getInstructions(localContext, source);
		InstructionProvider resultInstructionProvider = new InstructionProvider();

		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = getRS274NGCService().getIterator(sourceInstructionSet, localContext);


		while(iterator.hasNext()){
			localContext = new GCodeContext(iterator.getContext()); // Get the context before applying the command
			AbstractInstruction instr = iterator.next();

			if(instr.getType() == InstructionType.ARC_FEED){
				resultInstructionProvider.addInstructionSet(applyModifier(localContext, (ArcFeedInstruction) instr));

			}else{
				// Other non modified instruction
				InstructionSet resultInstructionSet = new InstructionSet();
				resultInstructionSet.addInstruction(instr);
				resultInstructionProvider.addInstructionSet(resultInstructionSet);
			}
			
		}
		GCodeProvider result = getRS274NGCService().getGCodeProvider(new GCodeContext(), resultInstructionProvider);
		for (GCodeLine line : result.getLines()) {
			target.addLine(line);			
		}
	}


	/**
	 * Segmentize the given arc feed instruction
	 * @param context the gcode context
	 * @param instruction the instruction to modify
	 * @return a list of instruction set
	 * @throws GkFunctionalException 
	 */
	private List<InstructionSet> applyModifier(GCodeContext context, ArcFeedInstruction instruction) throws GkFunctionalException {
		List<InstructionSet> result = new ArrayList<>();
		
		if(InstructionUtils.isValidArcFeedInstruction(context, instruction)){
			throw new GkFunctionalException("modifier.segmentize.invalid.arc");
		}
		
		Arc3b arc = InstructionUtils.getArc(context, instruction);
		
		int division = 0;
		// We only subdivide if the arc radius is greater than the chordal tolerance
		if(arc.getRadius().greaterThan(chordalTolerance)){
			// Get the arc angle for the given tolerance using h = R ( 1 - cos (A/2)) (aka  A = 2*acos(1  - (h / R))
			double angle = 2 * Math.acos( 1 - chordalTolerance.divide(arc.getRadius()).doubleValue() );
			BigDecimal nbCount = arc.getAngle().abs().divide(Angle.valueOf(BigDecimal.valueOf(angle).abs(), AngleUnit.RADIAN));
			division = (int) Math.ceil(nbCount.doubleValue());
		}
		for(int i = 1; i < division; i++){
			InstructionSet set = new InstructionSet();
			Tuple6b pt = arc.point( (float)i/division );			 
			StraightFeedInstruction straightInstruction = new StraightFeedInstruction(pt.getX(), pt.getY(), pt.getZ(), instruction.getA(), instruction.getB(), instruction.getC());
			set.addInstruction(straightInstruction);
			result.add(set);
		}
		
		// Add the very end point of the instruction so it always ends at the exact location		
		Tuple6b pt = arc.point( 1.0f );			 
		StraightFeedInstruction straightInstruction = new StraightFeedInstruction(pt.getX(), pt.getY(), pt.getZ(), instruction.getA(), instruction.getB(), instruction.getC());
//		switch (context.getPlane()) {		
//		case XZ_PLANE:	straightInstruction = new StraightFeedInstruction(instruction.getSecondEnd(), instruction.getAxisEndPoint(), instruction.getFirstEnd(), instruction.getA(), instruction.getB(), instruction.getC()); 
//			break;
//		case YZ_PLANE: straightInstruction = new StraightFeedInstruction(instruction.getAxisEndPoint(), instruction.getFirstEnd(), instruction.getSecondEnd(), instruction.getA(), instruction.getB(), instruction.getC()); 
//			break;		
//		case XY_PLANE:	straightInstruction = new StraightFeedInstruction(instruction.getFirstEnd(), instruction.getSecondEnd(), instruction.getAxisEndPoint(), instruction.getA(), instruction.getB(), instruction.getC());	
//		default:
//			break;
//		}	
		InstructionSet set = new InstructionSet();
		set.addInstruction(straightInstruction);
		result.add(set);
		
		return result;
	}


	/**
	 * @return the chordalTolerance
	 */
	public Length getChordalTolerance() {
		return chordalTolerance;
	}


	/**
	 * @param chordalTolerance the chordalTolerance to set
	 */
	public void setChordalTolerance(Length chordalTolerance) {
		this.chordalTolerance = chordalTolerance;
	}
}
