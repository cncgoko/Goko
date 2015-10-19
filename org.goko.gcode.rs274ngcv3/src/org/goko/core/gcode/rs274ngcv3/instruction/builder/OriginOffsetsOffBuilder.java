package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOffInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class OriginOffsetsOffBuilder extends AbstractInstructionBuilder<OriginOffsetsOffInstruction> {
	/** Constructor */
	public OriginOffsetsOffBuilder() {
		super(InstructionType.ORIGIN_OFFSETS_OFF);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G92.2", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public OriginOffsetsOffInstruction toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {		
		GCodeWordUtils.getAndRemoveWord("G92.2", words);
		return new OriginOffsetsOffInstruction();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, OriginOffsetsOffInstruction instruction) throws GkException {
		return wrap(new GCodeWord("G", "92.2"));
	}

}
