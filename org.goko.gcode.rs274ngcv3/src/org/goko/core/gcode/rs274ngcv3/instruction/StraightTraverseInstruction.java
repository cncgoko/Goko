package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

public class StraightTraverseInstruction extends AbstractStraightInstruction {

	/**
	 * Constructor 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param a A coordinate
	 * @param b B coordinate
	 * @param c C coordinate
	 */
	public StraightTraverseInstruction(Length x, Length y, Length z, Angle a, Angle b, Angle c) {
		super(InstructionType.STRAIGHT_TRAVERSE, x, y, z, a, b, c);
	}
	
	
	/**
	 * Copy constructor
	 * @param instr the instruction to copy
	 */
	public StraightTraverseInstruction(AbstractStraightInstruction instr) {
		super(InstructionType.STRAIGHT_TRAVERSE, instr);
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setMotionMode(EnumMotionMode.RAPID);
		super.apply(context);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}
