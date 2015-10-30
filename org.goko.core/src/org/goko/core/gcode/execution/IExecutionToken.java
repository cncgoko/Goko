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
package org.goko.core.gcode.execution;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.element.GCodeLine;

/**
 * A standard execution pattern
 * @author PsyKo
 *
 */
public interface IExecutionToken<T extends IExecutionState> extends IIdBean {	
	/**
	 * Check if this execution token has more command
	 * @return <code>true</code> if there is more command, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean hasMoreLine() throws GkException;
	
	/**
	 * Returns the total number of command
	 * @return the total number of command
	 * @throws GkException GkException
	 */
	public int getLineCount() throws GkException;
	
	/**
	 * Returns the state of the given command
	 * @param idCommand the id of the command
	 * @throws GkException GkException
	 */
	public T getLineState(Integer idLine) throws GkException;
	
	/**
	 * Returns the list of line in the given state 
	 * @param state the target state 
	 * @return a list of GCodeLine
	 * @throws GkException GkException
	 */
	public List<GCodeLine> getLineByState(T state) throws GkException;
	
	/**
	 * Sets the state of the given command 
	 * @param idCommand id of the command 
	 * @param state the state 
	 * @throws GkException GkException
	 */
	public void setLineState(Integer idLine, T state) throws GkException;
	
	/**
	 * Returns (but does not remove) the next command to execute in this execution token
	 * @return a GCodeCommand
	 * @throws GkException GkException
	 */
	public GCodeLine getNextLine() throws GkException;
	
	/**
	 * Returns (and remove) the next line to execute in this execution token
	 * @return a GCodeCommand
	 * @throws GkException GkException
	 */
	public GCodeLine takeNextLine() throws GkException;
	
	/**
	 * Notify the execution's start of this token
	 * @throws GkException GkException
	 */
	public void beginExecution() throws GkException;
	
	/**
	 * Pause/unpause the execution of this token 
	 * @param paused the pause state 
	 * @throws GkException GkException
	 */
	public void setExecutionPaused(boolean paused) throws GkException;
	
	/**
	 * Returns the pause state of this token
	 * @return <code>true</code> if the token is paused, <code>false</code> otherwise
	 * @throws GkException
	 */
	public boolean isExecutionPaused() throws GkException;
	
	/**
	 * Notify the execution's end of this token
	 * @throws GkException GkException
	 */
	public void endExecution() throws GkException;
	
	/**
	 * Returns <code>true</code> if this token is fully executed, <code>false</code> otherwise
	 * @return <code>true</code> if this token is fully executed, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean isComplete() throws GkException;
	
}
