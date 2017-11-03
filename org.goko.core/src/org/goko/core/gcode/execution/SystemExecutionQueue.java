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

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class SystemExecutionQueue<S extends IExecutionTokenState, T extends IExecutionToken<S>> extends ExecutionQueue<S, T> {

	/**
	 * Constructor
	 */
	public SystemExecutionQueue() {
		super(ExecutionQueueType.SYSTEM);
	}

	/**
	 * Clears the execution queue
	 * @throws GkException 
	 */
	private void clearExecutionQueue() throws GkException{
		List<T> tokens = getExecutionToken();
		for (T token : tokens) {
			delete(token.getId());
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.ExecutionQueue#onComplete()
	 */
	@Override
	public void onComplete() throws GkException {
		clearExecutionQueue();
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.ExecutionQueue#onCanceled()
	 */
	@Override
	public void onCanceled() throws GkException {
		clearExecutionQueue();
	}
}
