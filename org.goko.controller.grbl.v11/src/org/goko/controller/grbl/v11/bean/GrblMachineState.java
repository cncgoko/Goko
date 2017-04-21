/**
 * 
 */
package org.goko.controller.grbl.v11.bean;

import org.goko.core.controller.bean.MachineState;

/**
 * 
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblMachineState extends MachineState {
	// Ready is not used
	public static final GrblMachineState UNDEFINED 	= new GrblMachineState(-1,"Undefined");	
	public static final GrblMachineState IDLE 		= new GrblMachineState(0, "Idle");
	public static final GrblMachineState RUN 		= new GrblMachineState(1,"Run");	
	public static final GrblMachineState HOLD		= new GrblMachineState(3,"Hold");
	public static final GrblMachineState HOLDING	= new GrblMachineState(9,"Holding");
	public static final GrblMachineState JOG 		= new GrblMachineState(4,"Jog");
	public static final GrblMachineState DOOR 		= new GrblMachineState(5,"Door");
	public static final GrblMachineState CHECK 		= new GrblMachineState(6,"Check");
	public static final GrblMachineState HOME 		= new GrblMachineState(7,"Home");
	public static final GrblMachineState SLEEP 		= new GrblMachineState(8,"Sleep");
	
	public static final GrblMachineState ALARM 			= new GrblMachineState(10,"Alarm");
	public static final GrblMachineState ALARM_1 		= new GrblMachineState(11,"Alarm - Hard limit");
	public static final GrblMachineState ALARM_2 		= new GrblMachineState(13,"Alarm - Soft limit");
	public static final GrblMachineState ALARM_3 		= new GrblMachineState(14,"Alarm - Aborted");
	public static final GrblMachineState ALARM_4 		= new GrblMachineState(15,"Alarm 4 - Probe fail");
	public static final GrblMachineState ALARM_5 		= new GrblMachineState(16,"Alarm 5 - Probe fail");
	public static final GrblMachineState ALARM_6 		= new GrblMachineState(17,"Alarm 6 - Homing fail");
	public static final GrblMachineState ALARM_7 		= new GrblMachineState(18,"Alarm 7 - Homing fail");
	public static final GrblMachineState ALARM_8 		= new GrblMachineState(19,"Alarm 8 - Homing fail");
	public static final GrblMachineState ALARM_9 		= new GrblMachineState(20,"Alarm 9 - Homing fail");
	
	
	/**
	 * Constructor 
	 * @param code
	 * @param label
	 */
	public GrblMachineState(int code, String label) {
		super(code, label);
	}
	
}
