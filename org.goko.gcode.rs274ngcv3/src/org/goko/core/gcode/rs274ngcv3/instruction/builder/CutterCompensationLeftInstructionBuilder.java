package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationLeftInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class CutterCompensationLeftInstructionBuilder extends AbstractInstructionBuilder<CutterCompensationLeftInstruction> {
	
	/** Constructor */
	public CutterCompensationLeftInstructionBuilder() {
		super(InstructionType.CUTTER_COMPENSATION_LEFT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWordRegex("(G|g)41", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected CutterCompensationLeftInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.findAndRemoveWordRegex("(G|g)41", words);
		GCodeWord word = GCodeWordUtils.findAndRemoveWordByLetter("D", words);
		Length offset = null;
		if(word != null){
			offset = Length.valueOf(word.getValue(), context.getUnit().getUnit());
		}
		CutterCompensationLeftInstruction instr = new CutterCompensationLeftInstruction();
		instr.setOffset(offset);
		return instr;
	}
		
}
