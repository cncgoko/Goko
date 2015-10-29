package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleClockwiseInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StartSpindleClockwiseBuilder extends AbstractInstructionBuilder<StartSpindleClockwiseInstruction> {

	public StartSpindleClockwiseBuilder() {
		super(InstructionType.START_SPINDLE_CLOCKWISE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWord("M3", words);
	}

	@Override
	protected StartSpindleClockwiseInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("M3", words);
		return new StartSpindleClockwiseInstruction();
	}
}
