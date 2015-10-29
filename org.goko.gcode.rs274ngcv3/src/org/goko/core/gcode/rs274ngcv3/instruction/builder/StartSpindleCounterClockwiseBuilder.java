package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleCounterClockwiseInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StartSpindleCounterClockwiseBuilder extends AbstractInstructionBuilder<StartSpindleCounterClockwiseInstruction> {

	public StartSpindleCounterClockwiseBuilder() {
		super(InstructionType.START_SPINDLE_COUNTERCLOCKWISE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWord("M4", words);
	}

	@Override
	protected StartSpindleCounterClockwiseInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("M4", words);
		return new StartSpindleCounterClockwiseInstruction();
	}
}
