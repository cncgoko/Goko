package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.math.BigDecimal;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetSpindleSpeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetSpindleSpeedBuilder extends AbstractInstructionBuilder<SetSpindleSpeedInstruction> {
	
	/** Constructor */
	public SetSpindleSpeedBuilder() {
		super(InstructionType.SET_SPINDLE_SPEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWordByLetter("S", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public SetSpindleSpeedInstruction toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWord word = GCodeWordUtils.getAndRemoveWordByLetter("S", words);		
		return new SetSpindleSpeedInstruction(new BigDecimal(word.getValue()));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, SetSpindleSpeedInstruction instruction) throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

}
