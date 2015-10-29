package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetDistanceModeInstruction;

public class SetDistanceModeExporter extends AbstractInstructionExporter<SetDistanceModeInstruction>{

	public SetDistanceModeExporter() {
		super(InstructionType.SET_DISTANCE_MODE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetDistanceModeInstruction instruction) throws GkException {
		if(instruction.getDistanceMode() == EnumDistanceMode.RELATIVE){
			return wrap(new GCodeWord("G", "91"));
		}
		return wrap(new GCodeWord("G", "90"));
	}

}
