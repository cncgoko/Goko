package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class StopSpindleTurningInstruction extends AbstractInstruction {

	public StopSpindleTurningInstruction() {
		super(InstructionType.STOP_SPINDLE_TURNING);
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setSpindleMode(EnumSpindleMode.OFF);
//	}

}
