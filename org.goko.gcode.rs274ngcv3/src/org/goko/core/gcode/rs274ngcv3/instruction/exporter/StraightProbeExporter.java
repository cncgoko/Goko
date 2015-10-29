package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction;

public class StraightProbeExporter extends AbstractStraightInstructionExporter<StraightProbeInstruction>{

	public StraightProbeExporter() {
		super(InstructionType.STRAIGHT_PROBE);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractStraightInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, StraightProbeInstruction instruction) throws GkException {
		if(true){
			throw new GkTechnicalException("A faire");
		}
		List<GCodeWord> result = wrap(new GCodeWord("G","1"));
		result.addAll(super.getWords(context, instruction));
		return result;
	}

}
