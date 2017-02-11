/**
 * 
 */
package org.goko.controller.g2core.controller;

import org.goko.controller.tinyg.commons.TinyG;
import org.goko.core.controller.bean.MachineState;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public class G2Core extends TinyG {
	static final String AVAILABLE_BUFFER_COUNT	= "G2Core.buffer.count";	
	/** The last received message */
	static final String MESSAGE = "G2Core.msg";
	/** Feed hold command */
	public static final String FEED_HOLD_COMMAND = "!";
	/** Cycle start command */
	public static final String CYCLE_START_COMMAND = "~";
	/** Queue flush command */
	public static final String QUEUE_FLUSH_COMMAND = "%";
    /** Reset command */
    public static final byte RESET_COMMAND = 0x18;
    
    /** Empty request value */
    public static final String REQUEST_VALUE  = "";
    /** Kill alarm header */
    public static final String KILL_ALARM_HEADER = "clear";
    /** GCode for turning spindle on */
    public static final String TURN_SPINDLE_ON_GCODE = "M3";
    /** GCode for turning spindle off */
    public static final String TURN_SPINDLE_OFF_GCODE = "M5";
    
    /** The number of available slot in the planner buffer */
    public static final Integer PLANNER_BUFFER_SIZE = 48;
    
    public static final String STATUS_REPORT_G92 = "g92e";
    
    /** Probe constants */
	public static final String PROBE_REPORT_SUCCESS    = "e";
	public static final String PROBE_REPORT_POSITION_X = "x";
	public static final String PROBE_REPORT_POSITION_Y = "y";
	public static final String PROBE_REPORT_POSITION_Z = "z";
	public static final String PROBE_REPORT_POSITION_A = "a";
	public static final String PROBE_REPORT_POSITION_B = "b";
	public static final String PROBE_REPORT_POSITION_C = "c";
	
	
    public static class State{
    	public static final MachineState UNDEFINED 		= new MachineState(-1,"Undefined");
    	public static final MachineState INITIALIZING 	= new MachineState(0, "Initializing");
    	public static final MachineState READY 			= new MachineState(1,"Ready");
    	public static final MachineState ALARM 			= new MachineState(2,"Alarm");
    	public static final MachineState PROGRAM_STOP	= new MachineState(3,"Program stop");
    	public static final MachineState PROGRAM_END 	= new MachineState(4,"Program end");
    	public static final MachineState MOTION_RUNNING = new MachineState(5,"Motion running");
    	public static final MachineState MOTION_HOLDING = new MachineState(6,"Motion holding");
    	public static final MachineState PROBE_CYCLE 	= new MachineState(7,"Probe cycle");
    	public static final MachineState RUNNING 		= new MachineState(8,"Running");
    	public static final MachineState HOMING 		= new MachineState(9,"Homing");
    	public static final MachineState INTERLOCK		= new MachineState(11,"Interlock");
    	public static final MachineState SHUTDOWN		= new MachineState(12,"Shutdown");
    	public static final MachineState PANIC	 		= new MachineState(13,"PANIC");
    }
    
    public static class Preferences{
    	public static final String HOMING_SEQUENCE_CONFIGURED = "homingSequenceConfigured";
    	public static final String HOMING_ENABLED_AXIS_X 	  = "homingEnabledAxisX";
    	public static final String HOMING_ENABLED_AXIS_Y 	  = "homingEnabledAxisY";
    	public static final String HOMING_ENABLED_AXIS_Z 	  = "homingEnabledAxisZ";
    	public static final String HOMING_ENABLED_AXIS_A 	  = "homingEnabledAxisA";
    }

    public static class Topic{
    	 public static class Error{
    		 /** G2Core Topic : error notification topic */ 
 			public static final String TOPIC		= "topic/g2core/execution/error";
 			/** G2Core Topic : Title property */ 
 			public static final String TITLE		= "prop/g2core/execution/error/title";
 			/** G2Core Topic : Message property */ 
 			public static final String MESSAGE		= "prop/g2core/execution/error/message";
 			/** G2Core Topic : Error property */ 
 			public static final String ERROR		= "prop/g2core/execution/error/error";	
	    }
    }
    
    public static class Configuration{
    	public static class Groups{
    		public static final String SYSTEM 	  = "sys";
    		public static final String X_AXIS = "x";
    		public static final String Y_AXIS = "y";
    		public static final String Z_AXIS = "z";
    		public static final String A_AXIS = "a";
    		public static final String B_AXIS = "b";
    		public static final String C_AXIS = "c";

    		public static final String MOTOR_1 = "1";
    		public static final String MOTOR_2 = "2";
    		public static final String MOTOR_3 = "3";
    		public static final String MOTOR_4 = "4";
    		
    		public static final String PWM_CHANNEL_1 = "p1";
        }	
    	/**
    	 * System settings
    	 */
    	public static class System{
	    	/**
	    	 *  Identification Parameters
	    	 */
	    	public static final String FIRMWARE_VERSION 		= "fv";
	    	public static final String FIRMWARE_BUILD 			= "fb";
	    	public static final String FIRMWARE_BUILD_STRING 	= "fbs";
	    	public static final String FIRMWARE_BUILD_CONFIG	= "fbc";
	    	public static final String HARDWARE_PLATFORM 		= "hp";
	    	public static final String HARDWARE_VERSION 		= "hv";
	    	public static final String BOARD_ID					= "id";
	    	
	    	/**
	    	 * Global Machining Parameters
	    	 */
	    	public static final String JUNCTION_INTEGRATION_TIME= "jt";
	    	public static final String CHORDAL_TOLERANCE		= "ct";
	    	public static final String MOTOR_DISABLE_TIMEOUT	= "mt";
	    	
	    	/**
	    	 * Communications Parameters
	    	 */
	    	public static final String JSON_MODE				= "ej";
	    	public static final String JSON_MODE_VERBOSITY		= "jv";
	    	public static final String TEXT_MODE_VERBOSITY		= "jt";
	    	public static final String QUEUE_REPORT_VERBOSITY	= "qv";
	    	public static final String STATUS_REPORT_VERBOSITY	= "sv";
	    	public static final String STATUS_REPORT_INTERVAL	= "si";
	    	public static final String FLOW_CONTROL				= "ex";
	    	
	    	/**
	    	 * GCode Initialization Defaults
	    	 */
	    	public static final String DEFAULT_PLANE_SELECTION	= "gpl";
	    	public static final String DEFAULT_UNITS_MODE		= "gun";
	    	public static final String DEFAULT_COORDINATE_SYSTEM= "gco";
	    	public static final String DEFAULT_PATH_CONTROL		= "gpa";
	    	public static final String DEFAULT_DISTANCE_MODE	= "gdi";
    	}
    	/**
    	 * Motor settings
    	 */
    	public static class Motors{
    		public static final String MOTOR_MAPPING		= "ma";
    		public static final String STEP_ANGLE			= "sa";
    		public static final String TRAVEL_PER_REVOLUTION= "tr";
    		public static final String MICROSTEPS			= "mi";
    		public static final String STEPS_PER_UNIT		= "su";
    		public static final String POLARITY				= "po";
    		public static final String POWER_MODE			= "pm";
    		public static final String POWER_LEVEL			= "pl";
    	}
    	/**
    	 * Axis settings
    	 */
    	public static class Axes{
    		public static final String AXIS_MODE 			= "am";
    		public static final String VELOCITY_MAXIMUM 	= "vm";
    		public static final String FEEDRATE_MAXIMUM 	= "fr";
    		public static final String TRAVEL_MINIMUM 		= "tn";
    		public static final String TRAVEL_MAXIMUM 		= "tm";
    		public static final String JERK_MAXIMUM 		= "jm";
    		public static final String JERK_HOMING 			= "jh";
    		public static final String RADIUS_SETTING 		= "ra";
    		public static final String HOMING_INPUT			= "hi";
    		public static final String HOMING_DIRECTION		= "hd";    		
    		public static final String SEARCH_VELOCITY		= "sv";
    		public static final String LATCH_VELOCITY 		= "lv";
    		public static final String LATCH_BACKOFF 		= "lb";
    		public static final String ZERO_BACKOFF 		= "zb";    		
    	}
    	
     	/**
    	 * System settings
    	 */
    	public static class PwmChannel{
    		public static final String FREQUENCY 			= "frq";
    		public static final String CLOCKWISE_SPEED_LOW 	= "csl";
    		public static final String CLOCKWISE_SPEED_HIGH	= "csh";
    		public static final String CLOCKWISE_PHASE_LOW 	= "cpl";
    		public static final String CLOCKWISE_PHASE_HIGH	= "cph";
    		public static final String COUNTERCLOCKWISE_SPEED_LOW 	= "wsl";
    		public static final String COUNTERCLOCKWISE_SPEED_HIGH	= "wsh";
    		public static final String COUNTERCLOCKWISE_PHASE_LOW 	= "wpl";
    		public static final String COUNTERCLOCKWISE_PHASE_HIGH	= "wph";
    		public static final String PHASE_OFF			= "pof";
    	}
    }
}
