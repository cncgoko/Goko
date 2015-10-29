package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOnInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class OriginOffsetsOnBuilder extends AbstractInstructionBuilder<OriginOffsetsOnInstruction> {
	/** Constructor */
	public OriginOffsetsOnBuilder() {
		super(InstructionType.ORIGIN_OFFSETS_ON);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G92.3", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected OriginOffsetsOnInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("G92.3", words);
		return new OriginOffsetsOnInstruction();
	}
}
