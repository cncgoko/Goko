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
package org.goko.common.bindings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.goko.core.common.event.Event;

public class ErrorEvent implements Event {
	private static String PLUGIN_ID = "Goko Error";
	private String message;
	private String description;
	private IStatus status;

	public ErrorEvent(String message, String pluginId) {
		this.message = message;
		status = new Status(IStatus.ERROR, pluginId, message);
	}

	public ErrorEvent(Exception e, String pluginId){
		createStatus(e);
	}

	public void createStatus(Exception e){
		 // Temp holder of child statuses
	    List<Status> childStatuses = new ArrayList<>();

	    // Split output by OS-independend new-line
	    for (StackTraceElement elt: e.getStackTrace()) {
	        // build & add status
	        childStatuses.add(new Status(IStatus.ERROR, PLUGIN_ID, elt.toString()));
	    }
		status = new MultiStatus(PLUGIN_ID, IStatus.ERROR, childStatuses.toArray(new Status[]{}), e.getLocalizedMessage(), e);
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(IStatus status) {
		this.status = status;
	}

}
