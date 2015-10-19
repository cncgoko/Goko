package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class ResetOriginOffsetInstruction extends AbstractInstruction {
	/** Constructor */
	public ResetOriginOffsetInstruction() {
		super(InstructionType.RESET_ORIGIN_OFFSETS);
	}

//	/**
//	 * @param context
//	 * @throws GkException
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setOriginOffset(new Tuple6b().setZero());
//		context.setOriginOffsetActive(false);
//	}

}
