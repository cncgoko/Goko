package org.goko.core.gcode.rs274ngcv3.instruction;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;

public interface IInstructionExporter {

	boolean match(GCodeContext context, List<AbstractInstruction> instructions) throws GkException;
		
	AbstractInstruction toWords(GCodeContext context, List<AbstractInstruction> instructions, RenderingFormat format, List<GCodeWord> targetList ) throws GkException;		
}
