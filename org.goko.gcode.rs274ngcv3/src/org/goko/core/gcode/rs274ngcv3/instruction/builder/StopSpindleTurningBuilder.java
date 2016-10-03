package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StopSpindleTurningInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class StopSpindleTurningBuilder extends AbstractInstructionBuilder<StopSpindleTurningInstruction> {
	/** Constructor */
	public StopSpindleTurningBuilder() {
		super(InstructionType.STOP_SPINDLE_TURNING);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWordRegex("M(0?)5", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected StopSpindleTurningInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWordRegex("M(0?)5", words);
		return new StopSpindleTurningInstruction();
	}
}
