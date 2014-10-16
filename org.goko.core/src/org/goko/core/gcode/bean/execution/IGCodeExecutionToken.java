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
package org.goko.core.gcode.bean.execution;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.IGCodeProvider;

/**
 * A standard execution pattern
 * @author PsyKo
 *
 */
public interface IGCodeExecutionToken extends IGCodeProvider {
	/**
	 * Check if this execution token has more command
	 * @return <code>true</code> if there is more command, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean hasMoreCommand() throws GkException;
	/**
	 * Returns the total number of command
	 * @return the total number of command
	 * @throws GkException GkException
	 */
	public int getCommandCount() throws GkException;
	/**
	 * Mark a command as being executed
	 * @param idCommand the id of the command to mark as executed
	 * @throws GkException GkException
	 */
	public void markAsExecuted(Integer idCommand) throws GkException;
	/**
	 * Returns the count of executed commands
	 * @return the count of executed commands
	 * @throws GkException GkException
	 */
	public int getExecutedCommandCount() throws GkException;

	/**
	 * Mark a command as being in error state
	 * @param idCommand the id of the command to mark as error
	 * @throws GkException GkException
	 */
	public void markAsError(Integer idCommand) throws GkException;
	/**
	 * Returns the count of error commands
	 * @return the count of error commands
	 * @throws GkException GkException
	 */
	public int getErrorCommandCount() throws GkException;
	/**
	 * Returns the state of the given command
	 * @param idCommand the id of the command
	 * @throws GkException GkException
	 */
	public GCodeCommandState getCommandState(Integer idCommand) throws GkException;
	/**
	 * Returns (but does not remove) the next command to execute in this execution token
	 * @return a GCodeCommand
	 * @throws GkException GkException
	 */
	public GCodeCommand getNextCommand() throws GkException;
	/**
	 * Returns (and remove) the next command to execute in this execution token
	 * @return a GCodeCommand
	 * @throws GkException GkException
	 */
	public GCodeCommand takeNextCommand() throws GkException;
	/**
	 * Notify the execution's start of this token
	 * @throws GkException GkException
	 */
	public void beginExecution() throws GkException;
	/**
	 * Notify the execution's end of this token
	 * @throws GkException GkException
	 */
	public void endExecution() throws GkException;

}
