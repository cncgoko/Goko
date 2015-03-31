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
package org.goko.core.workspace.service;

public class GCodeProviderEvent {
	public enum GCodeProviderEventType{
		INSERT,
		DELETE,
		UPDATE,
		CURRENT_UPDATE
	}
	/** The type of the event */
	private GCodeProviderEventType type;
	/** The id of the target provider */
	private Integer targetId;

	/**
	 * Constructor
	 * @param type the type of this event
	 * @param targetId the target id
	 */
	public GCodeProviderEvent(GCodeProviderEventType type, Integer targetId) {
		super();
		this.type = type;
		this.targetId = targetId;
	}
	/**
	 * @return the type
	 */
	public GCodeProviderEventType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(GCodeProviderEventType type) {
		this.type = type;
	}
	/**
	 * @return the targetId
	 */
	public Integer getTargetId() {
		return targetId;
	}
	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
}
