package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

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

}
