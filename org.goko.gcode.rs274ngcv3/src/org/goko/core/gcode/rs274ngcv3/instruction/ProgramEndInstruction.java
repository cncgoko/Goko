package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class ProgramEndInstruction extends AbstractInstruction {
	
	/** Constructor */
	public ProgramEndInstruction() {
		super(InstructionType.PROGRAM_END);
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		// TODO Auto-generated method stub
//
//	}

}
