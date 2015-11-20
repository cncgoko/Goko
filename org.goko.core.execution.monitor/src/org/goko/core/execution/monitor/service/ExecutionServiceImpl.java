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

package org.goko.core.execution.monitor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;

/**
 * Default implementation of the GCode execution monitor service
 *
 * @author PsyKo
 *
 */
public class ExecutionServiceImpl implements IExecutionService<ExecutionState, ExecutionToken<ExecutionState>>, IGokoService {
	/** Service ID */
	public static final String SERVICE_ID = "org.goko.core.execution.monitor.service.GCodeExecutionMonitorServiceImpl";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionServiceImpl.class);
	/** The list of listener*/
	private List<IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>>> listenerList;
	/** The current executor */
	private IExecutor<ExecutionState, ExecutionToken<ExecutionState>> executor;
	/** The execution queue */
	private ExecutionQueue<ExecutionState, ExecutionToken<ExecutionState>> executionQueue;
	/** The execution queue runnable */
	private ExecutionQueueRunnable<ExecutionState, ExecutionToken<ExecutionState>> executionQueueRunnable;
	/** The executor service used to run the execution queue runnable */
	private ExecutorService executorService;
	
	/**
	 * Constructor
	 */
	public ExecutionServiceImpl() {
		listenerList 	= new ArrayList<IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>>>();
		executionQueue 	= new ExecutionQueue<ExecutionState, ExecutionToken<ExecutionState>>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+getServiceId());
		LOG.info("Successfully started "+getServiceId());

	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping  "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void addToExecutionQueue(ExecutionToken<ExecutionState> executionToken) throws GkException {
		executionQueue.add(executionToken);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionState> addToExecutionQueue(IGCodeProvider gcodeProvider) throws GkException {
		if(executor == null){
			throw new GkTechnicalException("ExecutionServiceImpl : cannot add provider to execution queue. Executor is not set.");
		}
		ExecutionToken<ExecutionState> token = executor.createToken(gcodeProvider);
		addToExecutionQueue(token);
		return token;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeFromExecutionQueue(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void removeFromExecutionQueue(ExecutionToken<ExecutionState> executionToken) throws GkException {
		executionQueue.delete(executionToken.getId());		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void addExecutionListener(IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> listener) throws GkException {
		if(!listenerList.contains(listener)){
			listenerList.add(listener);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> listener) throws GkException {
		if(listenerList.contains(listener)){
			listenerList.remove(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionStart(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionStart(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionStart(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyCommandStateChanged(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void notifyCommandStateChanged(ExecutionToken<ExecutionState> token, Integer idLine) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onLineStateChanged(token, idLine);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionCanceled(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionCanceled(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionPause(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionPause(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionPause(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionComplete(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionComplete(ExecutionToken<ExecutionState> token) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionState, ExecutionToken<ExecutionState>> executionListener : listenerList) {
				executionListener.onExecutionComplete(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#setExecutor(org.goko.core.gcode.execution.IExecutor)
	 */
	@Override
	public void setExecutor(IExecutor<ExecutionState, ExecutionToken<ExecutionState>> executor) throws GkException {
		this.executor = executor;		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#beginQueueExecution()
	 */
	@Override
	public void beginQueueExecution() throws GkException {
		if(executionQueue != null){
			if(executionQueue.isRunning()){
				throw new GkTechnicalException("Queue is already running");
			}
			executionQueueRunnable = new ExecutionQueueRunnable<>();
			executionQueueRunnable.setExecutor(executor);
			executionQueueRunnable.setExecutionQueue(executionQueue);
			
			if(executorService != null){
				executorService.shutdown();
			}
			executorService = Executors.newSingleThreadExecutor();			
			executorService.execute(executionQueueRunnable);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#pauseQueueExecution()
	 */
	@Override
	public void pauseQueueExecution() throws GkException {
		if(executionQueueRunnable != null){
			executionQueueRunnable.getExecutionQueue().setPaused(true);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#stopQueueExecution()
	 */
	@Override
	public void stopQueueExecution() throws GkException {
		// TODO Auto-generated method stub
		a implementer + debrancher la synchro par le token ?
	}
}
