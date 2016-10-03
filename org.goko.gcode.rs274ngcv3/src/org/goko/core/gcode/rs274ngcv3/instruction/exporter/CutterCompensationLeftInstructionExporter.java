package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationLeftInstruction;

public class CutterCompensationLeftInstructionExporter extends AbstractInstructionExporter<CutterCompensationLeftInstruction>{

	public CutterCompensationLeftInstructionExporter() {
		super(InstructionType.CUTTER_COMPENSATION_LEFT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, CutterCompensationLeftInstruction instruction, RenderingFormat format) throws GkException {
		List<GCodeWord> lst = new ArrayList<GCodeWord>();
		lst.add(new GCodeWord("G", "41"));
		if(instruction.getOffset() != null){
			lst.add(new GCodeWord("D", format.format(instruction.getOffset(), context.getUnit().getUnit())));	
		}
		return lst;
	}

}
