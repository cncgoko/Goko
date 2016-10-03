package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionControl;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetMotionControlModeInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SetMotionControlModeBuilder extends AbstractInstructionBuilder<SetMotionControlModeInstruction> {
	/** Constructor */
	public SetMotionControlModeBuilder() {
		super(InstructionType.SET_MOTION_CONTROL_MODE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G61", words) || GCodeWordUtils.containsWord("G61.1", words) || GCodeWordUtils.containsWord("G64", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SetMotionControlModeInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		EnumMotionControl controlMode = EnumMotionControl.CONTINUOUS;
		
		if(GCodeWordUtils.findAndRemoveWord("G61", words) != null){
			controlMode = EnumMotionControl.EXACT_PATH;
		}else if (GCodeWordUtils.findAndRemoveWord("G61.1", words) != null){
			controlMode = EnumMotionControl.EXACT_STOP;
		}else if( GCodeWordUtils.findAndRemoveWord("G64", words) != null){
			controlMode = EnumMotionControl.CONTINUOUS;
		}else{
			throw new GkTechnicalException("Error while parsing [SetMotionControlModeInstruction]");
		}
		
		return new SetMotionControlModeInstruction(controlMode);
	}
}
