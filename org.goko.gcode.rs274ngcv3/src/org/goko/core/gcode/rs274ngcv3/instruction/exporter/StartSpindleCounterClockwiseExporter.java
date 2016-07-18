package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleCounterClockwiseInstruction;

public class StartSpindleCounterClockwiseExporter extends AbstractInstructionExporter<StartSpindleCounterClockwiseInstruction>{

	public StartSpindleCounterClockwiseExporter() {
		super(InstructionType.START_SPINDLE_COUNTERCLOCKWISE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, StartSpindleCounterClockwiseInstruction instruction, RenderingFormat format) throws GkException {
		return wrap(new GCodeWord("M","4"));
	}

}
