package org.goko.core.gcode.rs274ngcv3.instruction;

import java.math.BigDecimal;

import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SetSpindleSpeedInstruction extends AbstractInstruction {
	/** Spindle speed */
	private BigDecimal spindleSpeed;
	
	/** Constructor */
	public SetSpindleSpeedInstruction(BigDecimal speed) {
		super(InstructionType.SET_SPINDLE_SPEED);
		this.spindleSpeed = speed;
	}

	/**
	 * @return the spindleSpeed
	 */
	public BigDecimal getSpindleSpeed() {
		return spindleSpeed;
	}

	/**
	 * @param spindleSpeed the spindleSpeed to set
	 */
	public void setSpindleSpeed(BigDecimal spindleSpeed) {
		this.spindleSpeed = spindleSpeed;
	}
	
//	/** (inheritDoc)
//	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
//	 */
//	@Override
//	public void apply(GCodeContext context) throws GkException {
//		context.setSpindleSpeed(spindleSpeed);
//	}

}
