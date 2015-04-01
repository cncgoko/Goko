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
package org.goko.core.gcode.bean.provider;

import org.goko.core.common.event.Event;
import org.goko.core.gcode.bean.GCodeCommand;

/**
 * An event for GCodeCommand state modification notification
 *
 * @author PsyKo
 *
 */
public class GCodeCommandExecutionEvent implements Event{
	/**
	 * The execution queue
	 */
	private GCodeExecutionToken executionToken;
	/**
	 * The command that changed
	 */
	private GCodeCommand command;
	/**
	 * The state of the command
	 */
	private int state;

	/**
	 * Constructor
	 * @param command the target command
	 * @param state the state
	 */
	public GCodeCommandExecutionEvent(GCodeExecutionToken queue, GCodeCommand command, int state) {
		super();
		this.executionToken = queue;
		this.command = command;
		this.state = state;
	}
	/**
	 * @return the command
	 */
	public GCodeCommand getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(GCodeCommand command) {
		this.command = command;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * @return the executionQueue
	 */
	public GCodeExecutionToken getExecutionToken() {
		return executionToken;
	}

}
