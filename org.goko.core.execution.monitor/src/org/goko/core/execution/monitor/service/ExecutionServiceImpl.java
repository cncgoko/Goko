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

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.execution.monitor.event.ExecutionServiceWorkspaceEvent;
import org.goko.core.execution.monitor.uiprovider.ExecutionQueueContainerUiProvider;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.core.workspace.service.IWorkspaceUIService;

/**
 * Default implementation of the GCode execution monitor service
 *
 * @author PsyKo
 *
 */
public class ExecutionServiceImpl implements IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>, IGokoService {
	/** Service ID */
	public static final String SERVICE_ID = "org.goko.core.execution.monitor.service.GCodeExecutionMonitorServiceImpl";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionServiceImpl.class);
	/** The list of listener */
	private List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList;
	/** The current executor */
	private IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executor;
	/** The execution queue */
	private ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueue;
	/** The execution queue runnable */
	private ExecutionQueueRunnable<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueueRunnable;
	/** The executor service used to run the execution queue runnable */
	private ExecutorService executorService;
	/** The workspace service */
	private IWorkspaceService workspaceService;
	/** Workspace UI Service */
	private IWorkspaceUIService workspaceUiService;
	
	/**
	 * Constructor
	 */
	public ExecutionServiceImpl() {		
		listenerList 	= new ArrayList<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>>();
		executionQueue 	= new ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>();
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
		// FIXME : a sortir de ce service pour le mettre dans un service UI ? Nouveau projet ?
		workspaceUiService.addProjectContainerUiProvider(new ExecutionQueueContainerUiProvider(this));
		
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
	public void addToExecutionQueue(ExecutionToken<ExecutionTokenState> executionToken) throws GkException {
		executionQueue.add(executionToken);	
		workspaceService.notifyWorkspaceEvent(ExecutionServiceWorkspaceEvent.getCreateEvent(executionToken));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> addToExecutionQueue(IGCodeProvider gcodeProvider) throws GkException {
		if(executor == null){
			throw new GkTechnicalException("ExecutionServiceImpl : cannot add provider to execution queue. Executor is not set.");
		}
		ExecutionToken<ExecutionTokenState> token = executor.createToken(gcodeProvider);
		addToExecutionQueue(token);		
		return token;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#clearExecutionQueue()
	 */
	@Override
	public void clearExecutionQueue() throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstTokens = getExecutionQueue().getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : lstTokens) {
				removeFromExecutionQueue(executionToken);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeFromExecutionQueue(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void removeFromExecutionQueue(ExecutionToken<ExecutionTokenState> executionToken) throws GkException {
		executionQueue.delete(executionToken.getId());	
		workspaceService.notifyWorkspaceEvent(ExecutionServiceWorkspaceEvent.getDeleteEvent(executionToken));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeFromExecutionQueue(java.lang.Integer)
	 */
	@Override
	public void removeFromExecutionQueue(Integer idExecutionToken) throws GkException {
		removeFromExecutionQueue(executionQueue.getExecutionToken(idExecutionToken));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void addExecutionListener(IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		if(!listenerList.contains(listener)){
			listenerList.add(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		if(listenerList.contains(listener)){
			listenerList.remove(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionStart(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionStart(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionStart()
	 */
	@Override
	public void notifyQueueExecutionStart() throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onQueueExecutionStart();
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionComplete()
	 */
	@Override
	public void notifyQueueExecutionComplete() throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onQueueExecutionComplete();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionCanceled()
	 */
	@Override
	public void notifyQueueExecutionCanceled() throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onQueueExecutionCanceled();
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyCommandStateChanged(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void notifyCommandStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onLineStateChanged(token, idLine);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionCanceled(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionPause(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionPause(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void notifyExecutionResume(ExecutionToken<ExecutionTokenState> token) throws GkException {
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionResume(token);
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionComplete(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionComplete(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#setExecutor(org.goko.core.gcode.execution.IExecutor)
	 */
	@Override
	public void setExecutor(IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executor) throws GkException {
		this.executor = executor;		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#beginQueueExecution()
	 */
	@Override
	public void beginQueueExecution() throws GkException {
		if(executionQueue != null){
			if(executionQueueRunnable != null && executionQueueRunnable.getState() == ExecutionState.RUNNING){
				throw new GkTechnicalException("Queue is already running");
			}			
			executionQueue.reset(); 
			
			executionQueueRunnable = new ExecutionQueueRunnable<>(this);
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
			executionQueueRunnable.pause();			
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#stopQueueExecution()
	 */
	@Override
	public void stopQueueExecution() throws GkException {
		if(executionQueueRunnable != null){
			executionQueueRunnable.stop();
		}
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#resumeQueueExecution()
	 */
	@Override
	public void resumeQueueExecution() throws GkException {
		if(executionQueueRunnable != null){
			executionQueueRunnable.resume();			
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#getExecutionState()
	 */
	@Override
	public ExecutionState getExecutionState() throws GkException {
		if(executionQueueRunnable == null){
			return ExecutionState.IDLE;
		}
		return executionQueueRunnable.getState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#getExecutionQueue()
	 */
	@Override
	public ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> getExecutionQueue() throws GkException {		
		return executionQueue;
	}
 
	/**
	 * @return the workspaceUiService
	 */
	public IWorkspaceUIService getWorkspaceUiService() {
		return workspaceUiService;
	}

	/**
	 * @param workspaceUiService the workspaceUiService to set
	 */
	public void setWorkspaceUiService(IWorkspaceUIService workspaceUiService) {
		this.workspaceUiService = workspaceUiService;
	}

	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}
}
