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

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.service.IExecutionService;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class SystemExecutionQueue<S extends IExecutionTokenState, T extends IExecutionToken<S>> extends ExecutionQueue<S, T> {
	private IExecutionService<S, T> executionService;
	/**
	 * Constructor
	 */
	public SystemExecutionQueue(IExecutionService<S, T> executionService) {
		super(ExecutionQueueType.SYSTEM);
		this.executionService = executionService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.ExecutionQueue#endCurrentTokenExecution()
	 */
	@Override
	public void endCurrentTokenExecution() throws GkException {
		executionService.removeFromExecutionQueue(getType(), getCurrentToken());
		super.endCurrentTokenExecution();
	}
	
}
