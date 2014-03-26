/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
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
package org.goko.core.controller.bean;

/**
 * Enumeration of TinyG machine states
 *
 * @author PsyKo
 *
 */
public class MachineState {
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
	/** The code of the state */
	private int code;
	/** Label of the state */ // TODO : make it work for I18N
	private String label;

	public MachineState(int code, String label){
		this.code = code;
		this.label = label;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MachineState other = (MachineState) obj;
		if (code != other.code) {
			return false;
		}
		return true;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
