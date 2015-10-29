package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectPlaneInstruction;

public class SelectPlaneExporter extends AbstractInstructionExporter<SelectPlaneInstruction> {

	public SelectPlaneExporter() {
		super(InstructionType.SELECT_PLANE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SelectPlaneInstruction instruction) throws GkException {		
		switch(instruction.getPlane()){
		case XY_PLANE: return wrap(new GCodeWord("G","17"));
		case XZ_PLANE: return wrap(new GCodeWord("G","18"));
		case YZ_PLANE: return wrap(new GCodeWord("G","19"));
		}
		return null;
	}

}
