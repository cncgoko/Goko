package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class ChangeToolInstruction extends AbstractInstruction {

	public ChangeToolInstruction() {
		super(InstructionType.CHANGE_TOOL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setActiveToolNumber(context.getSelectedToolNumber());
//	}

}
