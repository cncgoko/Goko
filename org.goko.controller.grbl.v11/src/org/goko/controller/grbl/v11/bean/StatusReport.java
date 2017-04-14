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
package org.goko.controller.grbl.v11.bean;

import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.math.Tuple6b;

public class StatusReport {
	/** The state of the machine */
	private GrblMachineState state;
	/** The machine position */
	private Tuple6b machinePosition;
	/** The work position of the machine */
	private Tuple6b workPosition;
	/** The current work coordinate offset */
	private Tuple6b currentWorkCoordinateOffset;
	/** The number of available planner buffer slot */
	private Integer availablePlannerBuffer;
	/** The number of available RX buffer slot */
	private Integer availableRxBuffer;
	/** Override value for feed motion */
	private Integer overrideFeed;
	/** Override value for rapid motion */
	private Integer overrideRapid;
	/** Override value for spindle RPM */
	private Integer overrideSpindle;
	/** Current velocity */
	private Speed velocity;
	/** Spindle speed in RPM */
	private Integer spindleSpeed;
	
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
	/**
	 * @return the overrideFeed
	 */
	public Integer getOverrideFeed() {
		return overrideFeed;
	}
	/**
	 * @param overrideFeed the overrideFeed to set
	 */
	public void setOverrideFeed(Integer overrideFeed) {
		this.overrideFeed = overrideFeed;
	}
	/**
	 * @return the overrideRapid
	 */
	public Integer getOverrideRapid() {
		return overrideRapid;
	}
	/**
	 * @param overrideRapid the overrideRapid to set
	 */
	public void setOverrideRapid(Integer overrideRapid) {
		this.overrideRapid = overrideRapid;
	}
	/**
	 * @return the overrideSpindle
	 */
	public Integer getOverrideSpindle() {
		return overrideSpindle;
	}
	/**
	 * @param overrideSpindle the overrideSpindle to set
	 */
	public void setOverrideSpindle(Integer overrideSpindle) {
		this.overrideSpindle = overrideSpindle;
	}
	/**
	 * @return the availablePlannerBuffer
	 */
	public Integer getAvailablePlannerBuffer() {
		return availablePlannerBuffer;
	}
	/**
	 * @param availablePlannerBuffer the availablePlannerBuffer to set
	 */
	public void setAvailablePlannerBuffer(Integer availablePlannerBuffer) {
		this.availablePlannerBuffer = availablePlannerBuffer;
	}
	/**
	 * @return the availableRxBuffer
	 */
	public Integer getAvailableRxBuffer() {
		return availableRxBuffer;
	}
	/**
	 * @param availableRxBuffer the availableRxBuffer to set
	 */
	public void setAvailableRxBuffer(Integer availableRxBuffer) {
		this.availableRxBuffer = availableRxBuffer;
	}
	/**
	 * @return the currentWorkCoordinateOffset
	 */
	public Tuple6b getCurrentWorkCoordinateOffset() {
		return currentWorkCoordinateOffset;
	}
	/**
	 * @param currentWorkCoordinateOffset the currentWorkCoordinateOffset to set
	 */
	public void setCurrentWorkCoordinateOffset(Tuple6b currentWorkCoordinateOffset) {
		this.currentWorkCoordinateOffset = currentWorkCoordinateOffset;
	}
	/**
	 * @return the velocity
	 */
	public Speed getVelocity() {
		return velocity;
	}
	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Speed velocity) {
		this.velocity = velocity;
	}
	/**
	 * @return the spindleSpeed
	 */
	public Integer getSpindleSpeed() {
		return spindleSpeed;
	}
	/**
	 * @param spindleSpeed the spindleSpeed to set
	 */
	public void setSpindleSpeed(Integer spindleSpeed) {
		this.spindleSpeed = spindleSpeed;
	}
	
}
