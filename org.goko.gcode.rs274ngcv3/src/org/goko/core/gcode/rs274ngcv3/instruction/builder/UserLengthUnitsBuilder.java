package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.UserLengthUnitsInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class UserLengthUnitsBuilder extends AbstractInstructionBuilder<UserLengthUnitsInstruction>{

	public UserLengthUnitsBuilder() {
		super(InstructionType.USER_LENGTH_UNITS);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G20", words) || GCodeWordUtils.containsWord("G21", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public UserLengthUnitsInstruction toInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		if(GCodeWordUtils.containsWord("G20", words)){
			GCodeWordUtils.getAndRemoveWord("G20", words);
			return new UserLengthUnitsInstruction(EnumUnit.INCHES);
			
		}else if(GCodeWordUtils.containsWord("G21", words)){
			GCodeWordUtils.getAndRemoveWord("G21", words);
			return new UserLengthUnitsInstruction(EnumUnit.MILLIMETERS);
		}
		throw new GkTechnicalException("No valid word for unit selection");
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, UserLengthUnitsInstruction instruction) throws GkException {
		GCodeWord word = null;
		if(instruction.getUnit() == EnumUnit.INCHES){
			word = new GCodeWord("G", "20");
		}else{
			word = new GCodeWord("G", "21");
		}
		return wrap(word);
	}

}
