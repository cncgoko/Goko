package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ProgramEndInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class ProgramEndBuilder extends AbstractInstructionBuilder<ProgramEndInstruction> {
	/** Constructor */
	public ProgramEndBuilder() {
		super(InstructionType.PROGRAM_END);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("M30", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected ProgramEndInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("M30", words);
		return new ProgramEndInstruction();
	}
}
