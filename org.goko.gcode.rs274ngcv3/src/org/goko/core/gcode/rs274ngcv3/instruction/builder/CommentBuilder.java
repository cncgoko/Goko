package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.CommentInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class CommentBuilder extends AbstractInstructionBuilder<CommentInstruction> {
	/** Constructor */
	public CommentBuilder() {
		super(InstructionType.COMMENT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWordByLetter(";", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected CommentInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWord commentword = GCodeWordUtils.getAndRemoveWordByLetter(";", words);
		return new CommentInstruction(commentword.getValue());
	}
}
