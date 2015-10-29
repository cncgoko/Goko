package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectToolInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SelectToolBuilder extends  AbstractInstructionBuilder<SelectToolInstruction> {

	/** Constructor */
	public SelectToolBuilder() {
		super(InstructionType.SELECT_TOOL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.findWordByLetter("T", words) != null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(java.util.List)
	 */
	@Override
	protected SelectToolInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWord word = GCodeWordUtils.getAndRemoveWordByLetter("T", words);
		Integer toolNumber = GCodeWordUtils.intValue(word);
		return new SelectToolInstruction(toolNumber);
	}

}
