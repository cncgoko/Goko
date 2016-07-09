package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class StraightProbeInstruction extends AbstractStraightInstruction {

	/**
	 * Constructor 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param a A coordinate
	 * @param b B coordinate
	 * @param c C coordinate
	 */
	public StraightProbeInstruction(Length x, Length y, Length z, Angle a, Angle b, Angle c) {
		super(InstructionType.STRAIGHT_PROBE, x, y, z, a, b, c);
	}
		
	/**
	 * Copy constructor
	 * @param instr the instruction to copy
	 */
	public StraightProbeInstruction(AbstractStraightInstruction instr) {
		super(InstructionType.STRAIGHT_PROBE, instr);
	}
}
