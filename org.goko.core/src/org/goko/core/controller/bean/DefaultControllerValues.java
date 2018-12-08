/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.controller.bean;

public interface DefaultControllerValues {

	static final String STATE 				= "GkDefaultControllerValue.State";
	static final String POSITION 			= "GkDefaultControllerValue.Position";
	static final String POSITION_X 			= "GkDefaultControllerValue.PositionX";	
	static final String POSITION_Y			= "GkDefaultControllerValue.PositionY";
	static final String POSITION_Z			= "GkDefaultControllerValue.PositionZ";
	static final String POSITION_A			= "GkDefaultControllerValue.PositionA";
	static final String POSITION_B			= "GkDefaultControllerValue.PositionB";
	static final String POSITION_C			= "GkDefaultControllerValue.PositionC";
	static final String MACHINE_POSITION 	= "GkDefaultControllerValue.MachinePosition";
	static final String MACHINE_POSITION_X 	= "GkDefaultControllerValue.MachinePositionX";	
	static final String MACHINE_POSITION_Y	= "GkDefaultControllerValue.MachinePositionY";
	static final String MACHINE_POSITION_Z	= "GkDefaultControllerValue.MachinePositionZ";
	static final String MACHINE_POSITION_A	= "GkDefaultControllerValue.MachinePositionA";
	static final String MACHINE_POSITION_B	= "GkDefaultControllerValue.MachinePositionB";
	static final String MACHINE_POSITION_C	= "GkDefaultControllerValue.MachinePositionC";
	static final String VELOCITY			= "GkDefaultControllerValue.Velocity";
	static final String SPINDLE_STATE		= "GkDefaultControllerValue.SpindleState";
	static final String SPINDLE_SPEED		= "GkDefaultControllerValue.SpindleSpeed";
	static final String UNITS 				= "GkDefaultControllerValue.Units";
	static final String COORDINATES 		= "GkDefaultControllerValue.Coordinates";
	static final String DISTANCE_MODE		= "GkDefaultControllerValue.DistanceMode";
	static final String TOOL_OFFSET			= "GkDefaultControllerValue.ToolOffset";

	static final String CONTEXT_FEEDRATE		= "GCodeContext.Feedrate";
	static final String CONTEXT_PLANE			= "GCodeContext.Plane";
	static final String CONTEXT_MOTION_MODE		= "GCodeContext.MotionMode";
	static final String CONTEXT_UNIT			= "GCodeContext.Unit";
	static final String CONTEXT_DISTANCE_MODE	= "GCodeContext.DistanceMode";
	static final String CONTEXT_COORD_SYSTEM	= "GCodeContext.CoordinateSystem";

}
