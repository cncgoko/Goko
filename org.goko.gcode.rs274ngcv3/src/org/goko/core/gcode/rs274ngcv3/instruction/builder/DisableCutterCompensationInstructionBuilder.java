package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.DisableCutterCompensationInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class DisableCutterCompensationInstructionBuilder extends AbstractInstructionBuilder<DisableCutterCompensationInstruction> {
	
	/** Constructor */
	public DisableCutterCompensationInstructionBuilder() {
		super(InstructionType.DISABLE_CUTTER_COMPENSATION);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWordRegex("G40", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected DisableCutterCompensationInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.findAndRemoveWordRegex("G40", words);
		return new DisableCutterCompensationInstruction();
	}
}
