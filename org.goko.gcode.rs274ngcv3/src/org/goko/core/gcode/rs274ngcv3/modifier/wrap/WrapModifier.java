/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.modifier.wrap;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
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
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;

/**
 * @author Psyko
 * @date 28 avr. 2016
 */
public class WrapModifier extends AbstractModifier<GCodeProvider> implements IModifier<GCodeProvider> {	
	private BigDecimal ratio = BigDecimal.ONE;
	private WrapModifierAxis axis = WrapModifierAxis.X_TO_A_AXIS;
	
	/**
	 * Constructor
	 */
	public WrapModifier() {
		super("Wrap");
	}	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#isConfigured()
	 */
	@Override
	public boolean isConfigured() {
		// Ratio should be non zero
		return !BigDecimal.ZERO.equals(ratio);
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
			//localContext = new GCodeContext(iterator.getContext()); // Get the context before applying the command
			AbstractInstruction instr = iterator.next();

			if(instr.getType() == InstructionType.STRAIGHT_FEED){
				resultInstructionProvider.addInstructionSet(applyModifier(localContext, (StraightFeedInstruction) instr));
			
			}else if(instr.getType() == InstructionType.STRAIGHT_TRAVERSE){
				resultInstructionProvider.addInstructionSet(applyModifier(localContext, (StraightTraverseInstruction) instr));
				
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
	 * @param localContext
	 * @param instr
	 * @return
	 * @throws GkException 
	 */
	private InstructionSet applyModifier(GCodeContext localContext, StraightFeedInstruction instr) throws GkException {		
		return applyModifier(localContext, instr, new StraightFeedInstruction(instr));
	}
	
	/**
	 * @param localContext
	 * @param instr
	 * @return
	 * @throws GkException 
	 */
	private InstructionSet applyModifier(GCodeContext localContext, StraightTraverseInstruction instr) throws GkException {		
		return applyModifier(localContext, instr, new StraightTraverseInstruction(instr));
	}

	private InstructionSet applyModifier(GCodeContext localContext, AbstractStraightInstruction src, AbstractStraightInstruction target) throws GkException {
		InstructionSet set = new InstructionSet();
		src.apply(localContext);
		Length aValue = Length.ZERO;
		switch (axis) {
		case Y_TO_A_AXIS:			
			aValue = localContext.getY();	
			target.setY(null);
			break;
		default:
			aValue = localContext.getX();
			target.setX(null);
			break;
		}
				
		target.setA( Angle.valueOf(aValue.value(localContext.getUnit().getUnit()).multiply(ratio), AngleUnit.DEGREE_ANGLE) );
		
		set.addInstruction(target);
		return set;
	}

	/**
	 * @return the axis
	 */
	public WrapModifierAxis getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(WrapModifierAxis axis) {
		this.axis = axis;
	}

	/**
	 * @return the ratio
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
	
}
