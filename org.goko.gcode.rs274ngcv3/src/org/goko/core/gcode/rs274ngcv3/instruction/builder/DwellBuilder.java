package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.math.BigDecimal;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class DwellBuilder extends AbstractInstructionBuilder<DwellInstruction> {

	/** Constructor */
	public DwellBuilder() {
		super(InstructionType.DWELL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWordRegex("(G|g)(0?)4", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected DwellInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWordRegex("(G|g)(0?)4", words);
		GCodeWord pWord = GCodeWordUtils.getAndRemoveWordByLetter("P", words);		
		BigDecimal seconds = new BigDecimal(pWord.getValue());
		if(seconds.compareTo(BigDecimal.ZERO) < 0){
			throw new GkFunctionalException("GCO-140");
		}
		return new DwellInstruction(seconds.intValue());
	}

}
