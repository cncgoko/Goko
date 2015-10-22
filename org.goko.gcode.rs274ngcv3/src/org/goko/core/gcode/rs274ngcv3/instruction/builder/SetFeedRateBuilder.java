package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetFeedRateBuilder extends AbstractInstructionBuilder<SetFeedRateInstruction> {
	/** Constructor */
	public SetFeedRateBuilder() {
		super(InstructionType.SET_FEED_RATE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWordByLetter("F", words);
	}

	@Override
	protected SetFeedRateInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWord fWord = GCodeWordUtils.getAndRemoveWordByLetter("F", words);
		return new SetFeedRateInstruction(BigDecimalUtils.parse(fWord.getValue()));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, SetFeedRateInstruction instruction) throws GkException {		
		return wrap(new GCodeWord("F", instruction.getFeedrate().toPlainString()));
	}

}
