/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v11;

import org.goko.core.controller.bean.DefaultControllerValues;

/**
 * Constants definition for Grbl 1.1
 * @author Psyko
 * @date 6 avr. 2017
 */
public abstract class Grbl implements DefaultControllerValues {
	/** Current status command */
	public static final String CURRENT_STATUS = "?";
	/** View parameters request */
	public static final String VIEW_PARAMETERS = "$#";
	/** View parser state request */
	public static final String PARSER_STATE = "$G";
	/** Response ok from Grbl */
	public static final CharSequence OK_RESPONSE = "ok";
	/** Pause command */
    public static final byte PAUSE_COMMAND = '!';
    /** Resume command */
    public static final byte RESUME_COMMAND = '~';
    /** Status command */
    public static final byte STATUS_COMMAND = '?';
    /** Reset command */
    public static final byte RESET_COMMAND = 0x18;
    /** Kill alarm command*/
    public static final String KILL_ALARM_COMMAND = "$X";
    /** Check mode enable/disable*/
    public static final String CHECK_MODE = "$C";
    /** Home sequence command*/
    public static final String HOME_COMMAND = "$H";
    /** Read configuration command */
    public static final String CONFIGURATION = "$$";
    /** Constant for Grbl used buffer value in value store */
    public static final String GRBL_USED_RXTX_BUFFER = "GrblControllerUsedRxBuffer";
    /** Constant for Grbl planner buffer value in value store */
    public static final String GRBL_PLANNER_BUFFER = "GrblControllerUsedPlannerBuffer";
	/** Grbl buffer size */
	public static final int GRBL_BUFFER_SIZE = 120;
	/** G54 offset name */
	public static final String G54_OFFSET = "G54";
	/** G55 offset name */
	public static final String G55_OFFSET = "G55";
	/** G56 offset name */
	public static final String G56_OFFSET = "G56";
	/** G57 offset name */
	public static final String G57_OFFSET = "G57";

	public static final String MACHINE_POSITION_X 		= "GrblControllerValue.MachinePositionX";
	public static final String MACHINE_POSITION_Y		= "GrblControllerValue.MachinePositionY";
	public static final String MACHINE_POSITION_Z		= "GrblControllerValue.MachinePositionZ";		

	public static class Commands{
		 public static final byte JOG_CANCEL = (byte) 0x85;
		 
		 public static final byte FEED_OVERRIDE_100 	= (byte) 0x90;
		 public static final byte FEED_OVERRIDE_PLUS_10 = (byte) 0x91;		 
		 public static final byte FEED_OVERRIDE_MINUS_10= (byte) 0x92;
		 public static final byte FEED_OVERRIDE_PLUS_1 	= (byte) 0x93;
		 public static final byte FEED_OVERRIDE_MINUS_1 = (byte) 0x94;
		 
		 public static final byte RAPID_OVERRIDE_100 	= (byte) 0x95;
		 public static final byte RAPID_OVERRIDE_50 	= (byte) 0x96;
		 public static final byte RAPID_OVERRIDE_25 	= (byte) 0x97;
		 
		 public static final byte SPINDLE_OVERRIDE_100 		= (byte) 0x99;
		 public static final byte SPINDLE_OVERRIDE_PLUS_10 	= (byte) 0x9A;		 
		 public static final byte SPINDLE_OVERRIDE_MINUS_10	= (byte) 0x9B;
		 public static final byte SPINDLE_OVERRIDE_PLUS_1 	= (byte) 0x9C;
		 public static final byte SPINDLE_OVERRIDE_MINUS_1	= (byte) 0x9D;
		 
		 public static final byte TOGGLE_SPINDLE			= (byte) 0x9E;
		 public static final byte TOGGLE_FLOOD_COOLANT		= (byte) 0xA0;
		 public static final byte TOGGLE_MIST_COOLANT		= (byte) 0xA1;
		 
		 public static final byte SAFETY_DOOR				= (byte) 0x84;
	}
	
	public static class MachineValue{
		static final String FEED_OVERRIDE	= "Grbl.v1.1.override.feed";
		static final String RAPID_OVERRIDE	= "Grbl.v1.1.override.rapid";
		static final String SPINDLE_OVERRIDE	= "Grbl.v1.1.override.spindle";
		
		static final String WCO_X	= "Grbl.v1.1.wco.x";
		static final String WCO_Y	= "Grbl.v1.1.wco.y";
		static final String WCO_Z	= "Grbl.v1.1.wco.z";
		
		static final String SPINDLE_DIRECTION 	= "Grbl.v1.1.spindle.direction";
		static final String MIST_COOLANT_STATE	= "Grbl.v1.1.mist.state";
		static final String FLOOD_COOLANT_STATE	= "Grbl.v1.1.flood.state";
		
		static final String MESSAGE	= "Grbl.v1.1.message";
	}
	
	public static class Configuration{
		public static final String STEP_PULSE 				= "$0";
		public static final String STEP_IDLE_DELAY 			= "$1";
		public static final String STEP_PORT_INVERT	 		= "$2";
		public static final String DIRECTION_PORT_INVERT	= "$3";
		public static final String STEP_ENABLE_INVERT 		= "$4";
		public static final String LIMIT_PIN_INVERT 		= "$5";
		public static final String PROBE_PIN_INVERT 		= "$6";
		public static final String STATUS_REPORT 			= "$10";
		public static final String JUNCTION_DEVIATION 		= "$11";
		public static final String ARC_TOLERANCE 			= "$12";
		public static final String REPORT_INCHES 			= "$13";
		public static final String SOFT_LIMITS_ENABLED 		= "$20";
		public static final String HARD_LIMITS_ENABLED 		= "$21";
		public static final String HOMING_CYCLE_ENABLED 	= "$22";
		public static final String HOMING_DIRECTION_INVERT 	= "$23";
		public static final String HOMING_FEED 				= "$24";
		public static final String HOMING_SEEK 				= "$25";
		public static final String HOMING_DEBOUNCE			= "$26";
		public static final String HOMING_PULL_OFF 			= "$27";
		public static final String MAX_SPINDLE_SPEED 		= "$30";
		public static final String MIN_SPINDLE_SPEED 		= "$31";
		public static final String LASER_MODE	 			= "$32";
		public static final String STEPS_PER_MM_X 			= "$100";
		public static final String STEPS_PER_MM_Y 			= "$101";
		public static final String STEPS_PER_MM_Z 			= "$102";
		public static final String MAX_RATE_X  				= "$110";
		public static final String MAX_RATE_Y  				= "$111";
		public static final String MAX_RATE_Z  				= "$112";
		public static final String ACCELERATION_X 			= "$120";
		public static final String ACCELERATION_Y 			= "$121";
		public static final String ACCELERATION_Z 			= "$122";
		public static final String MAX_TRAVEL_X 			= "$130";
		public static final String MAX_TRAVEL_Y 			= "$131";
		public static final String MAX_TRAVEL_Z 			= "$132";
	}
	
	public class Topic{
		public class GrblExecutionError{
			/** GRBL Topic : Grbl execution was paused due to an error during execution */ 
			public static final String TOPIC		= "topic/grbl/11/execution/error";
			/** GRBL Topic : Grbl execution was paused due to an error during execution */ 
			public static final String TITLE		= "prop/grbl/11/execution/error/title";
			/** GRBL Topic : Grbl execution was paused due to an error during execution */ 
			public static final String MESSAGE		= "prop/grbl/11/execution/error/message";
			/** GRBL Topic : Grbl execution was paused due to an error during execution */ 
			public static final String ERROR		= "prop/grbl/11/execution/error/error";
		}
	}
}
