package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemInstruction;

public class SetCoordinateSystemExporter extends AbstractInstructionExporter<SetCoordinateSystemInstruction>{

	public SetCoordinateSystemExporter() {
		super(InstructionType.SET_COORDINATE_SYSTEM);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetCoordinateSystemInstruction instruction) throws GkException {
		switch (instruction.getTargetCoordinateSystem()) {
		case G53: return wrap(new GCodeWord("G", "53"));			
		case G54: return wrap(new GCodeWord("G", "54"));			
		case G55: return wrap(new GCodeWord("G", "55"));			
		case G56: return wrap(new GCodeWord("G", "56"));			
		case G57: return wrap(new GCodeWord("G", "57"));			
		case G58: return wrap(new GCodeWord("G", "58"));			
		case G59: return wrap(new GCodeWord("G", "59"));			
		case G59_1: return wrap(new GCodeWord("G", "59.1"));			
		case G59_2: return wrap(new GCodeWord("G", "59.2"));			
		case G59_3: return wrap(new GCodeWord("G", "59.3"));				
		}
		return null;
	}

}
