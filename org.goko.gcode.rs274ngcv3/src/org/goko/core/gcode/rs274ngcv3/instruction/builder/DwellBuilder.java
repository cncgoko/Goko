package org.goko.core.gcode.rs274ngcv3.instruction.builder;

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
		return GCodeWordUtils.containsWord("G4", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected DwellInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		GCodeWordUtils.getAndRemoveWord("G4", words);
		GCodeWord pWord = GCodeWordUtils.getAndRemoveWordByLetter("P", words);		
		int seconds = GCodeWordUtils.intValue(pWord);
		if(seconds < 0){
			throw new GkFunctionalException("GCO-140");
		}
		return new DwellInstruction(seconds);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, DwellInstruction instruction) throws GkException {
		List<GCodeWord> lst = wrap(new GCodeWord("G", "4"));
		lst.add( new GCodeWord("P", String.valueOf(instruction.getSeconds())));
		return lst;
	}

}
