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

import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.execution.IExecutionTokenState;

/**
 * Listener for queue operations 
 * 
 * @author Psyko
 * @date 8 juin 2016
 */
public interface IExecutionQueueListener<S extends IExecutionTokenState, T extends IExecutionToken<S>> {

	/**
	 * Get notified when a token is added to the execution queue
	 * @param token the added token
	 */
	void onTokenCreate(T token);
	
	/**
	 * Get notified when a token is deleted the execution queue
	 * @param token the deleted token
	 */
	void onTokenDelete(T token);
	
	/**
	 * Get notified when a token is updated in the execution queue
	 * @param token the updated token
	 */
	void onTokenUpdate(T token);
}
