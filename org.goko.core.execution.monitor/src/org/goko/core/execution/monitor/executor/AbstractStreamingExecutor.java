package org.goko.core.execution.monitor.executor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.execution.IExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.log.GkLog;

/**
 * Defines an executor that streams the content of its provider to a distant executor.
 * 
 * @author Psyko
 *
 * @param <S> the template for the IExecutionState
 * @param <T> the template for the IExecutionToken
 */
public abstract class AbstractStreamingExecutor<S extends IExecutionState, T extends IExecutionToken<S>> implements IExecutor<S, T> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractStreamingExecutor.class);
	/** Lock object for token pause monitoring */
	private Object tokenPausedLock;
	/** Time to wait for pause check in milliseconds */
	private int tokenPauseTimeout = 500;
	
	/** The lock indicating that the executor is waiting to be able to execute the next line */
	final Lock readyForNextLineLock = new ReentrantLock();
	/** The condition indicating that the executor is waiting to be able to execute the next line */
	final Condition readyForNextLineCondition  = readyForNextLineLock.newCondition();
	
	/** The lock indicating that the executor is waiting for the token execution to be complete */
	final Lock tokenCompleteLock = new ReentrantLock();
	/** The condition indicating that the executor is waiting for the token execution to be complete */
	final Condition tokenCompleteCondition  = tokenCompleteLock.newCondition();
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#executeToken(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void executeToken(T token) throws GkException {
		while(token.hasMoreLine()){
			waitReadyForNextLine();
			waitTokenUnpaused(token);
			GCodeLine nextLine = token.getNextLine();
			send(nextLine);
		}	
		waitTokenComplete();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#waitTokenComplete()
	 */
	@Override
	public void waitTokenComplete() throws GkException {
		tokenCompleteLock.lock();
		try{
			tokenCompleteCondition.await();
		} catch (InterruptedException e) {
			LOG.error(e);
		}finally{
			tokenCompleteLock.unlock();
		}
	}
	
	/**
	 * Notify this object that the token is completely executed 
	 */
	protected final void notifyTokenComplete(){
		tokenCompleteLock.lock();
		try{
			tokenCompleteCondition.signal();		
		}finally{
			tokenCompleteLock.unlock();
		}
	}
	
	/**
	 * Sends the given line for execution
	 * @param line the line to send
	 * @throws GkException GkException
	 */
	protected abstract void send(GCodeLine line) throws GkException;
	
	/**
	 * Test if the executor is ready for the execution of the next line. 
	 * @return <code>true</code> if the executor is ready, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	protected abstract boolean isReadyForNextLine() throws GkException;
	
	/**
	 * Wait while the current token is paused.
	 */
	private void waitTokenUnpaused(T token) throws GkException{
		while(token.isExecutionPaused()){
			try {
				// We don't need extra fast reaction on this so synchronization will wait for the next check (after at most, tokenPauseTimeout milliseconds) 
				tokenPausedLock.wait(tokenPauseTimeout); 
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
	}
	
	/**
	 * Wait until the executor is ready for sending the next line.
	 * Can be interrupted by calling <code>notifyReadyForNextLine</code> method
	 */
	private void waitReadyForNextLine(){
		readyForNextLineLock.lock();
		try{
			readyForNextLineCondition.await();
		} catch (InterruptedException e) {
			LOG.error(e);
		}finally{
			readyForNextLineLock.unlock();
		}
	}
	
	/**
	 * Notify this object that the it's ready for the execution of the next line
	 */
	protected final void notifyReadyForNextLine(){
		readyForNextLineLock.lock();
		try{
			readyForNextLineCondition.signal();		
		}finally{
			readyForNextLineLock.unlock();
		}
	}
	
}
