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
	public static final GrblMachineState UNDEFINED 	= new GrblMachineState(-1,"Undefined");
	public static final GrblMachineState IDLE 		= new GrblMachineState(0, "Idle");
	public static final GrblMachineState RUN 		= new GrblMachineState(1,"Run");
	public static final GrblMachineState ALARM 		= new GrblMachineState(2,"Alarm");
	public static final GrblMachineState HOLD		= new GrblMachineState(3,"Hold");
	public static final GrblMachineState JOG 		= new GrblMachineState(4,"Jog");
	public static final GrblMachineState DOOR 		= new GrblMachineState(5,"Door");
	public static final GrblMachineState CHECK 		= new GrblMachineState(6,"Check");
	public static final GrblMachineState HOME 		= new GrblMachineState(7,"Home");
	public static final GrblMachineState SLEEP 		= new GrblMachineState(8,"Sleep");
	/**
	 * Constructor 
	 * @param code
	 * @param label
	 */
	public GrblMachineState(int code, String label) {
		super(code, label);
	}
	
}
