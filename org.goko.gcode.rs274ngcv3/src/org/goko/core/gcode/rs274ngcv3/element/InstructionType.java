package org.goko.core.gcode.rs274ngcv3.element;

import org.goko.core.gcode.element.IInstructionType;

public enum InstructionType implements IInstructionType{
		SET_ORIGIN_OFFSETS,
     	RESET_ORIGIN_OFFSETS,
     	ORIGIN_OFFSETS_ON,
     	ORIGIN_OFFSETS_OFF,
     	SET_COORDINATE_SYSTEM_DATA,
     	SET_COORDINATE_SYSTEM,
     	SET_DISTANCE_MODE, 
     	USER_LENGTH_UNITS, 
		STRAIGHT_TRAVERSE, 
/*T*/	SELECT_PLANE,
/*T*/	SET_FEED_RATE, 
/* */	SET_FEED_REFERENCE,
/*T*/	SET_MOTION_CONTROL_MODE,
/* */	START_SPEED_FEED_SYNCH,
/* */	STOP_SPEED_FEED_SYNCH,
/*T*/	ARC_FEED, 
/*T*/	DWELL,
		STRAIGHT_FEED, 
/***/	STRAIGHT_PROBE,
/* */	ORIENT_SPINDLE,
/***/	SET_SPINDLE_SPEED,
/***/	START_SPINDLE_CLOCKWISE, 
/***/	START_SPINDLE_COUNTERCLOCKWISE, 
/***/	STOP_SPINDLE_TURNING, 
/***/	CHANGE_TOOL, 
/***/	SELECT_TOOL, 
/* */	USER_TOOL_LENGTH_OFFSET,
/***/	COMMENT,
/* */	DISABLE_FEED_OVERRIDE,
/* */	DISABLE_SPEED_OVERRIDE,
/* */	ENABLE_FEED_OVERRIDE,
/* */	ENABLE_SPEED_OVERRIDE,
/***/	FLOOD_OFF,
/***/	FLOOD_ON,
/* */	INIT_CANON,
/* */	MESSAGE,
/***/	MIST_OFF,
/***/	MIST_ON,
/* */	DISABLE_CUTTER_COMPENSATION,
/* */	CUTTER_COMPENSATION_LEFT,
/* */	CUTTER_COMPENSATION_RIGHT,
/* */	PALLET_SHUTTLE,
/* */	OPTIONAL_PROGRAM_STOP,
/***/	PROGRAM_END,
/* */	PROGRAM_STOP;

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstructionType#equals(org.goko.core.gcode.element.IInstructionType)
	 */
	@Override
	public boolean equals(IInstructionType other) {
		if(other == this){
			return true;
		}else if(other instanceof InstructionType){
			return ((InstructionType)other) == this;
		}
		return false;
	}
	
	
	
}
