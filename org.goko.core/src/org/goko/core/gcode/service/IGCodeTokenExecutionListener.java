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
import org.goko.core.gcode.execution.IExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;

public interface IGCodeTokenExecutionListener<S extends IExecutionTokenState, T extends IExecutionToken<S>> {
	
	void onQueueExecutionStart() throws GkException;
	
	void onExecutionStart(T token) throws GkException;

	void onExecutionCanceled(T token) throws GkException;

	void onExecutionPause(T token) throws GkException;

	void onExecutionResume(T token) throws GkException;
	
	void onExecutionComplete(T token) throws GkException;
	
	void onQueueExecutionComplete() throws GkException;
	
	void onQueueExecutionCanceled() throws GkException;
}
