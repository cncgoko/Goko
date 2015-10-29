package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;

public class DwellExporter extends AbstractInstructionExporter<DwellInstruction>{

	public DwellExporter() {
		super(InstructionType.DWELL);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, DwellInstruction instruction) throws GkException {
		List<GCodeWord> lst = wrap(new GCodeWord("G", "4"));
		lst.add( new GCodeWord("P", String.valueOf(instruction.getSeconds())));
		return lst;
	}

}
