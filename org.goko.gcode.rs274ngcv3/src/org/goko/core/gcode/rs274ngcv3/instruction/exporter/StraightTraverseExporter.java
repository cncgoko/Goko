package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;

public class StraightTraverseExporter extends AbstractStraightInstructionExporter<StraightTraverseInstruction>{

	public StraightTraverseExporter() {
		super(InstructionType.STRAIGHT_TRAVERSE);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractStraightInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, StraightTraverseInstruction instruction, RenderingFormat format) throws GkException {
		List<GCodeWord> result = wrap(new GCodeWord("G","0"));
		result.addAll(super.getWords(context, instruction, format));
		return result;
	}

}
