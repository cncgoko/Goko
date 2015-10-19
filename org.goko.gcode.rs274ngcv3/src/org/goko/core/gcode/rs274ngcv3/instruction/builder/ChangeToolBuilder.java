package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ChangeToolInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class ChangeToolBuilder extends AbstractInstructionBuilder<ChangeToolInstruction> {
	
	/** Constructor */
	public ChangeToolBuilder() {
		super(InstructionType.CHANGE_TOOL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.findWord("M6", words) != null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(java.util.List)
	 */
	@Override
	public ChangeToolInstruction toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		// Make sure the word exists
		GCodeWordUtils.getAndRemoveWord("M6", words);
		return new ChangeToolInstruction();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, ChangeToolInstruction instruction) throws GkException {
		List<GCodeWord> result = new ArrayList<GCodeWord>();
		result.add(new GCodeWord("M","6"));
		return result;
	}

}
