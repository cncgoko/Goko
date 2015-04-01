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
package org.goko.core.gcode.bean.commands;

public enum EnumGCodeCommandFunctionType {
	UNKNOWN,
	PROGRAM_STOP, //M0 program stop
	OPTIONAL_PROGRAM_STOP,	//	M1 optional program stop
	PROGRAM_END,	//	M2 program end
	TURN_SPINDLE_CLOCKWISE,	//	M3 turn spindle clockwise
	TURN_SPINDLE_COUNTERCLOCKWISE,	//	M4 turn spindle counterclockwise
	TURN_SPINDLE_OFF,	//	M5 stop spindle turning
	TOOL_CHANGE,	//	M6 tool change
	MIST_COOLANTE_ON,	//	M7 mist coolant on
	FLOOD_COOLANT_ON,	//	M8 flood coolant on
	COOLANT_OFF,	//	M9 mist and flood coolant off
	PROGRAM_END_AND_RESET,	//	M30 program end, pallet shuttle, and reset
	SPEED_AND_FEED_OVERRIDE_ON,	//	M48 enable speed and feed overrides
	SPEED_AND_FEED_OVERRIDE_OFF,	//	M49 disable speed and feed overrides
	PALLET_SHUTTLE_AND_PROGRAM_STOP;	//	M60 pallet shuttle and program stop
}
