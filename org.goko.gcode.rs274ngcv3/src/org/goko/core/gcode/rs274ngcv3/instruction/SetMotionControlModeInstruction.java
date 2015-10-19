package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.gcode.rs274ngcv3.context.EnumMotionControl;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SetMotionControlModeInstruction extends AbstractInstruction {
	/** The target control mode */ 
	private EnumMotionControl controlMode;
	
	/** Constructor */
	public SetMotionControlModeInstruction(EnumMotionControl controlMode) {
		super(InstructionType.SET_MOTION_CONTROL_MODE);
		this.controlMode = controlMode;
	}

//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setMotionControl(controlMode);
//	}

	/**
	 * @return the controlMode
	 */
	public EnumMotionControl getControlMode() {
		return controlMode;
	}

}
