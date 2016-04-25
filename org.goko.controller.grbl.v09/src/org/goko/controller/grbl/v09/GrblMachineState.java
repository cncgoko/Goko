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
package org.goko.controller.grbl.v09;

import org.goko.core.controller.bean.MachineState;

/**
 * Enumeration of TinyG machine states
 *
 * @author PsyKo
 *
 */
public class GrblMachineState extends MachineState{
	public static final GrblMachineState UNDEFINED 		= new GrblMachineState(-1,"Undefined");
	public static final GrblMachineState INITIALIZING 	= new GrblMachineState(0, "Initializing");
	public static final GrblMachineState READY 			= new GrblMachineState(1,"Ready");
	public static final GrblMachineState ALARM 			= new GrblMachineState(2,"Alarm");
	public static final GrblMachineState PROGRAM_STOP	= new GrblMachineState(3,"Program stop");
	public static final GrblMachineState PROGRAM_END 	= new GrblMachineState(4,"Program end");
	public static final GrblMachineState MOTION_RUNNING = new GrblMachineState(5,"Motion running");
	public static final GrblMachineState MOTION_HOLDING = new GrblMachineState(6,"Motion holding");
	public static final GrblMachineState PROBE_CYCLE 	= new GrblMachineState(7,"Probe cycle");
	public static final GrblMachineState RUNNING 		= new GrblMachineState(8,"Running");
	public static final GrblMachineState HOMING 		= new GrblMachineState(9,"Homing");
	public static final GrblMachineState IDLE 			= new GrblMachineState(100, "Idle");
	public static final GrblMachineState CHECK 			= new GrblMachineState(10,"Check");
	public static final GrblMachineState HOLD 			= new GrblMachineState(11,"Hold");
	
	public GrblMachineState(int code, String label){
		super(code,label);
	}

}
