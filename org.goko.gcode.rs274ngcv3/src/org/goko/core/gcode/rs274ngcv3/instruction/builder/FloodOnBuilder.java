package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.FloodOnInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class FloodOnBuilder extends AbstractInstructionBuilder<FloodOnInstruction> {
	
	/** Constructor */
	public FloodOnBuilder() {
		super(InstructionType.FLOOD_ON);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {
		return GCodeWordUtils.containsWord("M8", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected FloodOnInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return new FloodOnInstruction();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, FloodOnInstruction instruction) throws GkException {
		return wrap(new GCodeWord("M", "8"));
	}

}
