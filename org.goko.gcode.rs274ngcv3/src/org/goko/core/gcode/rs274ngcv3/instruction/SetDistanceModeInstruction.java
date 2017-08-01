package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;

public class SetDistanceModeInstruction extends AbstractInstruction {
	/** The distance mode set by this instruction */
	private EnumDistanceMode distanceMode;
	
	/** Constructor */
	public SetDistanceModeInstruction(EnumDistanceMode mode) {
		super(InstructionType.SET_DISTANCE_MODE);
		this.distanceMode = mode;
	}

	/**
	 * @return the distanceMode
	 */
	public EnumDistanceMode getDistanceMode() {
		return distanceMode;
	}

	/**
	 * @param distanceMode the distanceMode to set
	 */
	public void setDistanceMode(EnumDistanceMode distanceMode) {
		this.distanceMode = distanceMode;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setDistanceMode(distanceMode);
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((distanceMode == null) ? 0 : distanceMode.hashCode());
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
		SetDistanceModeInstruction other = (SetDistanceModeInstruction) obj;
		if (distanceMode != other.distanceMode)
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
