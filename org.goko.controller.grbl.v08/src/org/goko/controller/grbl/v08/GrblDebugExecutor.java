/*******************************************************************************
 * Goko - Copyright (C) 2016  PsyKo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.controller.grbl.v08;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executor.AbstractStreamingExecutor;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;

public class GrblDebugExecutor extends AbstractStreamingExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> implements IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	private Object lock = new Object();

	/**
	 * Constructor
	 * @param gcodeService the gcode service
	 */
	public GrblDebugExecutor() {
		super();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#isReadyForQueueExecution()
	 */
	@Override
	public boolean isReadyForQueueExecution() throws GkException {
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#createToken(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> createToken(IGCodeProvider provider) throws GkException {
		return new ExecutionToken<ExecutionTokenState>(provider, ExecutionTokenState.NONE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	protected void send(GCodeLine line) throws GkException {

		if(Math.random() > -10.002){
			getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
			System.out.println(line);
		}else{
			getToken().setLineState(line.getId(), ExecutionTokenState.ERROR);
			System.err.println(line);
			getExecutionService().pauseQueueExecution();
		}
		try {
			synchronized (lock) {
				lock.wait(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(getToken().getLineCountByState(ExecutionTokenState.NONE) == 0){
			notifyTokenComplete();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isTokenComplete()
	 */
	@Override
	public boolean isTokenComplete() throws GkException {
		return getToken().getLineCountByState(ExecutionTokenState.NONE) == 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isReadyForNextLine()
	 */
	@Override
	protected boolean isReadyForNextLine() throws GkException {
		return true;
	}

}
