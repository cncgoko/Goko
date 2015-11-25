package org.goko.controller.tinyg.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executor.AbstractStreamingExecutor;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.IExecutor;

public class DebugExecutor extends AbstractStreamingExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> implements IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{

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
		getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
		//System.out.println(line);
		if(getToken().getLineByState(ExecutionTokenState.NONE).size() == 0){
			notifyTokenComplete();			
		}
	}

	/** (inheritDoc) 
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isTokenComplete()
	 */
	@Override
	public boolean isTokenComplete() throws GkException {
		return getToken().getLineByState(ExecutionTokenState.NONE).size() == 0;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isReadyForNextLine()
	 */
	@Override
	protected boolean isReadyForNextLine() throws GkException {
		return true;
	}
	
}
