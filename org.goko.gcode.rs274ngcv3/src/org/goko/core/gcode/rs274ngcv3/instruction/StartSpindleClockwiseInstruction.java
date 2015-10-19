package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class StartSpindleClockwiseInstruction extends AbstractInstruction {

	public StartSpindleClockwiseInstruction() {
		super(InstructionType.START_SPINDLE_CLOCKWISE);
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setSpindleMode(EnumSpindleMode.ON_CLOCKWISE);
//	}

}
