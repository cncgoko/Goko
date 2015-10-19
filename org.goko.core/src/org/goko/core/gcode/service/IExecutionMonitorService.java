/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.IExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;

public interface IExecutionMonitorService<S extends IExecutionState, T extends IExecutionToken<S>> {

	void addExecutionListener(IGCodeExecutionListener<S,T> listener) throws GkException;

	void removeExecutionListener(IGCodeExecutionListener<S,T> listener) throws GkException;

	void notifyExecutionStart(T token) throws GkException;

	void notifyCommandStateChanged(T token, Integer idCommand) throws GkException;

	void notifyExecutionCanceled(T token) throws GkException;

	void notifyExecutionPause(T token) throws GkException;

	void notifyExecutionComplete(T token) throws GkException;
}
