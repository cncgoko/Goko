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
package org.goko.controller.grbl.v08.bean;

import org.goko.controller.grbl.v08.GrblMachineState;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.Tuple6b;

public class StatusReport {
	/** The state of the machine */
	private GrblMachineState state;
	/** The machine position */
	private Tuple6b machinePosition;
	/** The work position of the machine */
	private Tuple6b workPosition;
	/**
	 * @return the state
	 */
	public GrblMachineState getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(GrblMachineState state) {
		this.state = state;
	}
	/**
	 * @return the machinePosition
	 */
	public Tuple6b getMachinePosition() {
		return machinePosition;
	}
	/**
	 * @param machinePosition the machinePosition to set
	 */
	public void setMachinePosition(Tuple6b machinePosition) {
		this.machinePosition = machinePosition;
	}
	/**
	 * @return the workPosition
	 */
	public Tuple6b getWorkPosition() {
		return workPosition;
	}
	/**
	 * @param workPosition the workPosition to set
	 */
	public void setWorkPosition(Tuple6b workPosition) {
		this.workPosition = workPosition;
	}
}
