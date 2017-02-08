package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetCoordinateSystemBuilder extends AbstractInstructionBuilder<SetCoordinateSystemInstruction>{
	/** Constructor */
	public SetCoordinateSystemBuilder() {
		super(InstructionType.SET_COORDINATE_SYSTEM);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G54", words)
			|| GCodeWordUtils.containsWord("G55", words)
			|| GCodeWordUtils.containsWord("G56", words)
			|| GCodeWordUtils.containsWord("G57", words)
			|| GCodeWordUtils.containsWord("G58", words)
			|| GCodeWordUtils.containsWord("G59", words)
			|| GCodeWordUtils.containsWord("G59.1", words)
			|| GCodeWordUtils.containsWord("G59.2", words)
			|| GCodeWordUtils.containsWord("G59.3", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SetCoordinateSystemInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		CoordinateSystem targetCoordinateSystem = null;
		if(GCodeWordUtils.findAndRemoveWord("G54", words) != null){
			targetCoordinateSystem = CoordinateSystem.G54;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G55", words) != null){
			targetCoordinateSystem = CoordinateSystem.G55;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G56", words) != null){
			targetCoordinateSystem = CoordinateSystem.G56;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G57", words) != null){
			targetCoordinateSystem = CoordinateSystem.G57;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G58", words) != null){
			targetCoordinateSystem = CoordinateSystem.G58;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G59", words) != null){
			targetCoordinateSystem = CoordinateSystem.G59;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G59.1", words) != null){
			targetCoordinateSystem = CoordinateSystem.G59_1;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G59.2", words) != null){
			targetCoordinateSystem = CoordinateSystem.G59_2;
			
		}else if(GCodeWordUtils.findAndRemoveWord("G59.3", words) != null){
			targetCoordinateSystem = CoordinateSystem.G59_3;
		}else{
			throw new GkTechnicalException("Unknown Coordinate system...");
		}
		
		return new SetCoordinateSystemInstruction(targetCoordinateSystem);
	}
}
