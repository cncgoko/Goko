package org.goko.core.execution.monitor.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.IExecutionQueue;
import org.goko.core.gcode.execution.IExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.log.GkLog;

/**
 * Describes a runnable performing the execution of an ExecutionQueue
 * 
 * @author Psyko
 *
 */
public class ExecutionQueueRunnable<S extends IExecutionState, T extends IExecutionToken<S>>  implements Runnable{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionQueueRunnable.class);
	/** The current executor */
	private IExecutor<S, T> executor;
	/** The execution queue*/
	private IExecutionQueue<S, T> executionQueue;
	
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){ // TODO : investigate if this runnable should be a singleton or not...
			try{
				executionQueue.beginNextTokenExecution();
				runExecutionToken();
				waitTokenComplete();
				executionQueue.endCurrentTokenExecution();
			}catch(GkException e){
				LOG.error(e);
			}
		}
	}
	
	/**
	 * Execute the given execution token
	 * @param token the token to execute
	 * @throws GkException GkException
	 */
	protected void runExecutionToken() throws GkException{
		while(executionQueue.getCurrentToken() != null && executionQueue.getCurrentToken().hasMoreLine()){
			T token = executionQueue.getCurrentToken();
			executor.executeToken(token);
		}
	}
	
	private void waitTokenComplete() throws GkException {
		while (executionQueue.getCurrentToken() != null && !executionQueue.getCurrentToken().isComplete()) {
			executor.waitTokenComplete();
		}
	}

	/**
	 * @return the executor
	 */
	public IExecutor<S, T> getExecutor() {
		return executor;
	}

	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(IExecutor<S, T> executor) {
		this.executor = executor;
	}

	/**
	 * @return the executionQueue
	 */
	public IExecutionQueue<S, T> getExecutionQueue() {
		return executionQueue;
	}

	/**
	 * @param executionQueue the executionQueue to set
	 */
	public void setExecutionQueue(IExecutionQueue<S, T> executionQueue) {
		this.executionQueue = executionQueue;
	}

}
