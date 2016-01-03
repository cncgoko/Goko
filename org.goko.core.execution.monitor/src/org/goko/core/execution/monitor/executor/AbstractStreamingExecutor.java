package org.goko.core.execution.monitor.executor;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.execution.IExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;

/**
 * Defines an executor that streams the content of its provider to a distant executor.
 *
 * @author Psyko
 *
 * @param <S> the template for the IExecutionState
 * @param <T> the template for the IExecutionToken
 */
public abstract class AbstractStreamingExecutor<S extends IExecutionTokenState, T extends IExecutionToken<S>> implements IExecutor<S, T> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractStreamingExecutor.class);
	/** Lock object for token pause monitoring */
	private Object executionPausedLock = new Object();
	/** Time to wait for pause check in milliseconds */
	private int tokenPauseTimeout = 500;
	/** The execution service */
	private IExecutionService<S, T> executionService;
	/** Weak reference to the use token */
	private WeakReference<T> tokenWeakReference;
	/** The lock indicating that the executor is waiting to be able to execute the next line */
	private final Lock readyForNextLineLock = new ReentrantLock();
	/** The condition indicating that the executor is waiting to be able to execute the next line */
	private final Condition readyForNextLineCondition  = readyForNextLineLock.newCondition();

	/** The lock indicating that the executor is waiting for the token execution to be complete */
	private final Lock tokenCompleteLock = new ReentrantLock();
	/** The condition indicating that the executor is waiting for the token execution to be complete */
	private final Condition tokenCompleteCondition  = tokenCompleteLock.newCondition();
	/** Token complete indicator */
	private boolean tokenComplete;
	/** The state of the execution */
	private ExecutionState state;

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#executeToken(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void executeToken(T token) throws GkException {
		tokenWeakReference = new WeakReference<T>(token);
		setTokenComplete(false);

		updateState(ExecutionState.RUNNING);
		getExecutionService().notifyExecutionStart(token);

		while(token.hasMoreLine()){
			waitReadyForNextLine();
			waitExecutionUnpaused(token);
			// We have a stop
			if(state == ExecutionState.STOPPED){
				break;
			}
			GCodeLine nextLine = token.takeNextLine();
			send(nextLine);
			getExecutionService().notifyCommandStateChanged(getToken(), nextLine.getId());
		}
		waitTokenComplete();

		if(state == ExecutionState.STOPPED){
			getExecutionService().notifyExecutionCanceled(getToken());
		}else{
			updateState(ExecutionState.COMPLETE);
			getExecutionService().notifyExecutionComplete(getToken());
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#waitTokenComplete()
	 */
	@Override
	public void waitTokenComplete() throws GkException {
		if(!tokenComplete && state != ExecutionState.STOPPED){
			tokenCompleteLock.lock();
			try{
				tokenCompleteCondition.await();
			} catch (InterruptedException e) {
				LOG.error(e);
			}finally{
				tokenCompleteLock.unlock();
			}
		}
	}

	/**
	 * Notify this object that the token is completely executed
	 */
	protected final void notifyTokenComplete(){
		tokenCompleteLock.lock();
		try{
			setTokenComplete(true);
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
	private void waitExecutionUnpaused(T token) throws GkException{
		while(state == ExecutionState.PAUSED){
			try {
				synchronized (executionPausedLock) {
					// We don't need extra fast reaction on this so synchronization will wait for the next check (after at most, tokenPauseTimeout milliseconds)
					executionPausedLock.wait(tokenPauseTimeout);
				}
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
	}

	/**
	 * Wait until the executor is ready for sending the next line.
	 * Can be interrupted by calling <code>notifyReadyForNextLine</code> method
	 * @throws GkException GkException
	 */
	private void waitReadyForNextLine() throws GkException{
		if(!isReadyForNextLine() && getToken().hasMoreLine()){
			readyForNextLineLock.lock();
			try{
				readyForNextLineCondition.await();
			} catch (InterruptedException e) {
				LOG.error(e);
			}finally{
				readyForNextLineLock.unlock();
			}
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

	/**
	 *
	 * @param state
	 * @throws GkException
	 */
	private void updateState(ExecutionState state) throws GkException{
		setState(state);
		getToken().setState(state);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#getToken()
	 */
	@Override
	public T getToken() throws GkException {
		return tokenWeakReference.get();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#getExecutionService()
	 */
	@Override
	public IExecutionService<S, T> getExecutionService() throws GkException{
		return executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#setExecutionService(org.goko.core.gcode.service.IExecutionService)
	 */
	@Override
	public void setExecutionService(IExecutionService<S, T> executionService) throws GkException{
		this.executionService = executionService;
	}

	/**
	 * @return the tokenComplete
	 */
	@Override
	public boolean isTokenComplete() throws GkException{
		return tokenComplete;
	}

	/**
	 * @param tokenComplete the tokenComplete to set
	 */
	public void setTokenComplete(boolean tokenComplete) {
		this.tokenComplete = tokenComplete;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionControl#start()
	 */
	@Override
	public void start() throws GkException {
		updateState(ExecutionState.RUNNING);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionControl#resume()
	 */
	@Override
	public void resume() throws GkException {
		updateState(ExecutionState.RUNNING);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionControl#pause()
	 */
	@Override
	public void pause() throws GkException {
		updateState(ExecutionState.PAUSED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionControl#stop()
	 */
	@Override
	public void stop() throws GkException {
		updateState(ExecutionState.STOPPED);
		notifyReadyForNextLine();
	}

	/**
	 * @return the state
	 */
	@Override
	public ExecutionState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(ExecutionState state) {
		this.state = state;
	}
}
