package org.goko.core.gcode.rs274ngcv3.instruction;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

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
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setSpindleSpeed(spindleSpeed);
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((spindleSpeed == null) ? 0 : spindleSpeed.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SetSpindleSpeedInstruction other = (SetSpindleSpeedInstruction) obj;
		if (spindleSpeed == null) {
			if (other.spindleSpeed != null)
				return false;
		} else if (!spindleSpeed.equals(other.spindleSpeed))
			return false;
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction#accept(org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter)
	 */
	@Override
	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}
