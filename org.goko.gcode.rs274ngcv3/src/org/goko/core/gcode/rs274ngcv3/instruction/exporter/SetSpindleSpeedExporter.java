package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetSpindleSpeedInstruction;

public class SetSpindleSpeedExporter extends AbstractInstructionExporter<SetSpindleSpeedInstruction> {

	public SetSpindleSpeedExporter() {
		super(InstructionType.SET_SPINDLE_SPEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetSpindleSpeedInstruction instruction, RenderingFormat format) throws GkException {
		return wrap(new GCodeWord("S", instruction.getSpindleSpeed().toPlainString()));
	}

}
