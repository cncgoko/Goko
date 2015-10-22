package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetDistanceModeInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetDistanceModeBuilder extends AbstractInstructionBuilder<SetDistanceModeInstruction> {

	public SetDistanceModeBuilder() {
		super(InstructionType.SET_DISTANCE_MODE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G90", words) || GCodeWordUtils.containsWord("G91", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SetDistanceModeInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		if(GCodeWordUtils.containsWord("G90", words)){
			GCodeWordUtils.findAndRemoveWord("G90", words);		
			return new SetDistanceModeInstruction(EnumDistanceMode.ABSOLUTE);
			
		}else if(GCodeWordUtils.containsWord("G91", words)){
			GCodeWordUtils.findAndRemoveWord("G91", words);
			return new SetDistanceModeInstruction(EnumDistanceMode.RELATIVE);
		}
		throw new GkTechnicalException("No valid word for distane mode");
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, SetDistanceModeInstruction instruction) throws GkException {
		return null;
	}

}
