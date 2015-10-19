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
package org.goko.common.events;

import java.lang.ref.WeakReference;

import org.goko.core.gcode.element.GCodeLine;


/**
 * Event for GCodeCommand selection event
 *
 * @author PsyKo
 *
 */
public class GCodeCommandSelectionEvent {
	/** Weak reference to the command */
	private WeakReference<GCodeLine> command;

	/**
	 * @param command
	 */
	public GCodeCommandSelectionEvent(GCodeLine command) {
		super();
		this.command = new WeakReference<GCodeLine>(command);
	}

	public boolean isValid(){
		return command != null && command.get() != null;
	}

	public GCodeLine getGCodeLine(){
		return command.get();
	}

}
