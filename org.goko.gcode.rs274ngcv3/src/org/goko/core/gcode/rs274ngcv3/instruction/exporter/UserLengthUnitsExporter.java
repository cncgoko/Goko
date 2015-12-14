package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.UserLengthUnitsInstruction;

public class UserLengthUnitsExporter extends AbstractInstructionExporter<UserLengthUnitsInstruction>{

	public UserLengthUnitsExporter() {
		super(InstructionType.USER_LENGTH_UNITS);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, UserLengthUnitsInstruction instruction) throws GkException {
		GCodeWord word = null;
		if(instruction.getUnit() == EnumUnit.INCHES){
			word = new GCodeWord("G", "20");
		}else{
			word = new GCodeWord("G", "21");
		}
		return wrap(word);
	}

}
