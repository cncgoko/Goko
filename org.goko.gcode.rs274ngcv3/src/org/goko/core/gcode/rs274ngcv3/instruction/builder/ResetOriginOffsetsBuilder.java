package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ResetOriginOffsetInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class ResetOriginOffsetsBuilder extends AbstractInstructionBuilder<ResetOriginOffsetInstruction> {

	public ResetOriginOffsetsBuilder() {
		super(InstructionType.RESET_ORIGIN_OFFSETS);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWord("G92.1", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected ResetOriginOffsetInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("G92.1", words);
		return new ResetOriginOffsetInstruction();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, ResetOriginOffsetInstruction instruction) throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

}
