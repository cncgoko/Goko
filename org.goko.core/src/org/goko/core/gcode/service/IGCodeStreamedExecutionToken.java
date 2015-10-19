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
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.IExecutionToken;

/**
 * Defines an execution process which is send to a distant executer (TinyG, Grbl, etc...) and which requires "network" statistics
 * @author PsyKo
 *
 */
public interface IGCodeStreamedExecutionToken extends IExecutionToken {
	/**
	 * Mark a command as being sent to the distant machine
	 * @param idCommand the id of the command to mark as sent
	 * @throws GkException GkException
	 */
	public void markAsSent(Integer idCommand) throws GkException;
	/**
	 * Returns the count of sent commands
	 * @return the count of sent commands
	 * @throws GkException GkException
	 */
	public int getSentCommandCount() throws GkException;

	/**
	 * Mark a command as being confirmed by the distant machine
	 * @param idCommand the id of the command to mark as confirmed
	 * @throws GkException GkException
	 */
	public void markAsConfirmed(Integer idCommand) throws GkException;
	/**
	 * Returns the count of confirmed commands
	 * @return the count of confirmed commands
	 * @throws GkException GkException
	 */
	public int getConfirmedCommandCount() throws GkException;

}
