package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetMotionControlModeInstruction;

public class SetMotionControlModeExporter extends AbstractInstructionExporter<SetMotionControlModeInstruction>{

	public SetMotionControlModeExporter() {
		super(InstructionType.SET_MOTION_CONTROL_MODE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetMotionControlModeInstruction instruction) throws GkException {
		List<GCodeWord> words = new ArrayList<GCodeWord>();
		switch (instruction.getControlMode()) {
		case EXACT_PATH: words = wrap(new GCodeWord("G", "61"));			
			break;
		case EXACT_STOP: words = wrap(new GCodeWord("G", "61.1"));			
			break;
		case CONTINUOUS: words = wrap(new GCodeWord("G", "64"));
			break;
		default:
			break;
		}
		return words;
	}

}
