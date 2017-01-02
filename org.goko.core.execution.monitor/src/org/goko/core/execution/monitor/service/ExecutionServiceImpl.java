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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheByKey;
import org.goko.core.execution.monitor.event.ExecutionServiceWorkspaceEvent;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.gcode.execution.SystemExecutionQueue;
import org.goko.core.gcode.service.IExecutionQueueListener;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * Default implementation of the GCode execution monitor service
 *
 * @author PsyKo
 *
 */
public class ExecutionServiceImpl extends AbstractGokoService implements IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>, IGokoService, IGCodeProviderRepositoryListener {
	/** Service ID */
	public static final String SERVICE_ID = "org.goko.core.execution.monitor.service.GCodeExecutionMonitorServiceImpl";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionServiceImpl.class);
	/** The list of execution listener */
	private CacheByKey<ExecutionQueueType, List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>>> listenercache;
	/** The list of execution queue listener */
	private List<IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> executionQueuelistenerList;
	/** The current executor */
	private IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executor;
	/** The execution queue */
	private CacheByKey<ExecutionQueueType, ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> cacheExecutionQueue;
	/** The execution queue runnable */
	private ExecutionQueueRunnable<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueueRunnable;
	/** The executor service used to run the execution queue runnable */
	private ExecutorService executorService;
	/** The workspace service */
	private IWorkspaceService workspaceService;
	/** GCode provider repository */
	private List<IGCodeProviderRepository> lstGCcodeRepository;
	
	/**
	 * Constructor
	 * @throws GkException GkException 
	 */
	public ExecutionServiceImpl() throws GkException {
		listenercache 	= new CacheByKey<>(); 
		listenercache.add(ExecutionQueueType.DEFAULT, new CopyOnWriteArrayList<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>>());
		listenercache.add(ExecutionQueueType.SYSTEM, new CopyOnWriteArrayList<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>>());
		cacheExecutionQueue 	= new CacheByKey<>();
		// Creation of the queues
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> defaultQueue = new ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>(ExecutionQueueType.DEFAULT);
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> systemQueue = new SystemExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>(this);
		cacheExecutionQueue.add(ExecutionQueueType.DEFAULT, defaultQueue);
		cacheExecutionQueue.add(ExecutionQueueType.SYSTEM, systemQueue);
		
		executionQueuelistenerList 	= new CopyOnWriteArrayList<IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>>();
		lstGCcodeRepository = new CopyOnWriteArrayList<IGCodeProviderRepository>();
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
	public void startService() throws GkException {		
		

	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {
	}

	
	public ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> getExecutionQueue(ExecutionQueueType type) throws GkException{
		return cacheExecutionQueue.get(type); 
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#getExecutionQueue()
	 */
	@Override
	public List<ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> getExecutionQueue() throws GkException {
		return cacheExecutionQueue.get();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#findRunningExecutionQueue()
	 */
	@Override
	public ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> findRunningExecutionQueue()	throws GkException {
		if(executionQueueRunnable != null){
			return executionQueueRunnable.getExecutionQueue();
		}
		return null;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void addToExecutionQueue(ExecutionToken<ExecutionTokenState> executionToken) throws GkException {
		addToExecutionQueue(ExecutionQueueType.DEFAULT, executionToken);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.execution.ExecutionQueueType, org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void addToExecutionQueue(ExecutionQueueType type, ExecutionToken<ExecutionTokenState> executionToken) throws GkException {
		if(isQueueBeingExecuted(type)){
			throw new GkTechnicalException("Queue "+type+" is already being executed");
		}
		getExecutionQueue(type).add(executionToken);
		workspaceService.notifyWorkspaceEvent(ExecutionServiceWorkspaceEvent.getCreateEvent(executionToken));
		notifyTokenCreate(executionToken);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.execution.ExecutionQueueType, org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> addToExecutionQueue(ExecutionQueueType type, IGCodeProvider gcodeProvider) throws GkException {
		if(isQueueBeingExecuted(type)){
			throw new GkTechnicalException("Queue "+type+" is already being executed");
		}
		if(executor == null){
			throw new GkTechnicalException("ExecutionServiceImpl : cannot add provider to execution queue. Executor is not set.");
		}
		ExecutionToken<ExecutionTokenState> existingToken = findExecutionTokenByGCodeProvider(ExecutionQueueType.DEFAULT, gcodeProvider);
		if(existingToken != null){
			throw new GkTechnicalException("ExecutionServiceImpl : the given GCode provider is already in the execution queue.") ;
		}
		ExecutionToken<ExecutionTokenState> token = executor.createToken(gcodeProvider);
		addToExecutionQueue(type, token);
		return token;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addToExecutionQueue(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> addToExecutionQueue(IGCodeProvider gcodeProvider) throws GkException {
		return addToExecutionQueue(ExecutionQueueType.DEFAULT, gcodeProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#clearExecutionQueue()
	 */
	@Override
	public void clearExecutionQueue(ExecutionQueueType queueType) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstTokens = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : lstTokens) {
				removeFromExecutionQueue(queueType, executionToken);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeFromExecutionQueue(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void removeFromExecutionQueue(ExecutionQueueType queueType, ExecutionToken<ExecutionTokenState> executionToken) throws GkException {
		getExecutionQueue(queueType).delete(executionToken.getId());
		workspaceService.notifyWorkspaceEvent(ExecutionServiceWorkspaceEvent.getDeleteEvent(executionToken));
		notifyTokenDelete(executionToken);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeFromExecutionQueue(java.lang.Integer)
	 */
	@Override
	public void removeFromExecutionQueue(ExecutionQueueType queueType, Integer idExecutionToken) throws GkException {
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> queue = getExecutionQueue(queueType);		
		removeFromExecutionQueue(queueType, queue.getExecutionToken(idExecutionToken));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void addExecutionListener(ExecutionQueueType queueType, IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		if(!listenercache.get(queueType).contains(listener)){
			listenercache.get(queueType).add(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(ExecutionQueueType queueType, IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		if(listenercache.get(queueType).contains(listener)){
			listenercache.get(queueType).remove(listener);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeExecutionListener(org.goko.core.gcode.service.IGCodeExecutionListener)
	 */
	@Override
	public void removeExecutionListener(IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		removeExecutionListener(ExecutionQueueType.DEFAULT, listener);
		removeExecutionListener(ExecutionQueueType.SYSTEM, listener);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#addExecutionQueueListener(org.goko.core.gcode.service.IExecutionQueueListener)
	 */
	@Override
	public void addExecutionQueueListener(IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		if(!executionQueuelistenerList.contains(listener)){
			executionQueuelistenerList.add(listener);
		}		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#removeExecutionQueueListener(org.goko.core.gcode.service.IExecutionQueueListener)
	 */
	@Override
	public void removeExecutionQueueListener( IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener) throws GkException {
		executionQueuelistenerList.remove(listener);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionStart(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenercache.get(queueType))){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenercache.get(queueType)) {
				executionListener.onExecutionStart(token);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionStart()
	 */
	@Override
	public void notifyQueueExecutionStart(ExecutionQueueType queueType) throws GkException {
		if(CollectionUtils.isNotEmpty(listenercache.get(queueType))){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenercache.get(queueType)) {
				executionListener.onQueueExecutionStart();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionComplete()
	 */
	@Override
	public void notifyQueueExecutionComplete(ExecutionQueueType queueType) throws GkException {
		if(CollectionUtils.isNotEmpty(listenercache.get(queueType))){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenercache.get(queueType)) {
				executionListener.onQueueExecutionComplete();
			}
		}
		unlockGCodeProvider(queueType);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyQueueExecutionCanceled()
	 */
	@Override
	public void notifyQueueExecutionCanceled(ExecutionQueueType queueType) throws GkException {
		if(CollectionUtils.isNotEmpty(listenercache.get(queueType))){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenercache.get(queueType)) {
				executionListener.onQueueExecutionCanceled();
			}
		}
		unlockGCodeProvider(queueType);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyCommandStateChanged(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void notifyCommandStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList = listenercache.get(queueType);
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onLineStateChanged(token, idLine);
				LOG.info("Executed line ["+idLine+"]");
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#notifyExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken)
	 */
	@Override
	public void notifyExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList = listenercache.get(queueType);
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
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList = listenercache.get(queueType);
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
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList = listenercache.get(queueType);
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
		ExecutionQueueType queueType = executionQueueRunnable.getExecutionQueue().getType();
		List<IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>> listenerList = listenercache.get(queueType);
		workspaceService.notifyWorkspaceEvent( ExecutionServiceWorkspaceEvent.getUpdateEvent(token) );
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionListener : listenerList) {
				executionListener.onExecutionComplete(token);
			}
		}
	}

	/**
	 * Notifies the registered listener for a token create event 
	 * @param token the created token
	 */
	protected void notifyTokenCreate(ExecutionToken<ExecutionTokenState> token){
		for (IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener : executionQueuelistenerList) {
			listener.onTokenCreate(token);
		}
	}
	/**
	 * Notifies the registered listener for a token delete event 
	 * @param token the deleted token
	 */
	protected void notifyTokenDelete(ExecutionToken<ExecutionTokenState> token){
		for (IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener : executionQueuelistenerList) {
			listener.onTokenDelete(token);
		}
	}
	
	/**
	 * Notifies the registered listener for a token update event 
	 * @param token the updated token
	 */
	protected void notifyTokenUpdate(ExecutionToken<ExecutionTokenState> token){
		for (IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> listener : executionQueuelistenerList) {
			listener.onTokenUpdate(token);
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
	public void beginQueueExecution(ExecutionQueueType type) throws GkException {
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueue = getExecutionQueue(type);
		if(executionQueue != null && executor.isReadyForQueueExecution()){			
			if(executionQueueRunnable != null &&
					(executionQueueRunnable.getState() == ExecutionState.RUNNING ||
					executionQueueRunnable.getState() == ExecutionState.PAUSED ||
					executionQueueRunnable.getState() == ExecutionState.ERROR)){
				throw new GkTechnicalException("Queue is already running");
			}
			lockGCodeProvider(type);
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

	/**
	 * Lock the GCode providers in queue
	 * @throws GkException GkException
	 */
	private void lockGCodeProvider(ExecutionQueueType queueType) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstToken = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstToken)){
			for (ExecutionToken<ExecutionTokenState> executionToken : lstToken) {
				executionToken.getGCodeProvider().lock();
			}
		}
	}

	/**
	 * Unlock the GCode providers in queue
	 * @throws GkException GkException
	 */
	protected void unlockGCodeProvider(ExecutionQueueType queueType) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstToken = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstToken)){
			for (ExecutionToken<ExecutionTokenState> executionToken : lstToken) {
				executionToken.getGCodeProvider().unlock();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#pauseQueueExecution()
	 */
	@Override
	public void pauseQueueExecution() throws GkException {
		if(getExecutionState() != ExecutionState.PAUSED && getExecutionState() != ExecutionState.IDLE){
			if(executionQueueRunnable != null){
				executionQueueRunnable.pause();
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#stopQueueExecution()
	 */
	@Override
	public void stopQueueExecution() throws GkException {
		if(getExecutionState() != ExecutionState.STOPPED){
			if(executionQueueRunnable != null){
				executionQueueRunnable.stop();
				unlockGCodeProvider(executionQueueRunnable.getExecutionQueue().getType());
			}
			
		}
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#resumeQueueExecution()
	 */
	@Override
	public void resumeQueueExecution() throws GkException {
		if(getExecutionState() == ExecutionState.PAUSED){
			if(executionQueueRunnable != null){
				executionQueueRunnable.resume();
			}
		}
	}
	
	protected boolean isQueueBeingExecuted(ExecutionQueueType type){
		if(executionQueueRunnable != null && executionQueueRunnable.getExecutionQueue().getType() == type){
			return executionQueueRunnable.getState() == ExecutionState.ERROR || 
					executionQueueRunnable.getState() == ExecutionState.PAUSED ||
					executionQueueRunnable.getState() == ExecutionState.RUNNING;
		}
		return false;
	}
	
	public boolean isReadyForExecution() throws GkException{
		if(getExecutionState() == ExecutionState.IDLE || 
			getExecutionState() == ExecutionState.COMPLETE ||
			getExecutionState() == ExecutionState.FATAL_ERROR||
			getExecutionState() == ExecutionState.STOPPED){
			return true;
		}
		return false;
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

	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 * @throws GkException GkException
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}

	/**
	 * @param gcodeRepository the gcodeRepository to set
	 * @throws GkException GkException 
	 */
	public void addGCodeRepository(IGCodeProviderRepository gcodeRepository) throws GkException {
		this.lstGCcodeRepository.add(gcodeRepository);
		gcodeRepository.addListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		// Reset the data for the updated token
		
		ExecutionToken<ExecutionTokenState> tokenToRemove = null;
		ExecutionQueueType type = null;
		
		for (ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueue : cacheExecutionQueue.get()) {
			ExecutionToken<ExecutionTokenState> token = findExecutionTokenByGCodeProvider(executionQueue.getType(), provider);
			if(token != null){
				token.reset();
				notifyTokenUpdate(token);					
				break;
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#beforeGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		ExecutionToken<ExecutionTokenState> tokenToRemove = null;
		ExecutionQueueType type = null;
		
		for (ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionQueue : cacheExecutionQueue.get()) {
			ExecutionToken<ExecutionTokenState> token = findExecutionTokenByGCodeProvider(executionQueue.getType(), provider);
			if(token != null){
				tokenToRemove = token;
				type = executionQueue.getType();
				break;
			}
		}

		if(tokenToRemove != null){
			removeFromExecutionQueue(type, tokenToRemove);
		}		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#afterGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		// Nothing yet
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub

	}

	
	@Override
	public ExecutionToken<ExecutionTokenState> getExecutionTokenByGCodeProvider(ExecutionQueueType queueType, IGCodeProvider gcodeProvider) throws GkException {
		ExecutionToken<ExecutionTokenState> token = findExecutionTokenByGCodeProvider(queueType, gcodeProvider);
		if(token == null){
			throw new GkTechnicalException("No Execution token for GCode Provider ["+gcodeProvider.getId()+"]");
		}
		return token;
	}

	

	@Override
	public ExecutionToken<ExecutionTokenState> findExecutionTokenByGCodeProvider(ExecutionQueueType queueType, IGCodeProvider gcodeProvider) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstTokens = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : lstTokens) {
				if(ObjectUtils.equals(gcodeProvider, executionToken.getGCodeProvider())){
					return executionToken;
				}
			}
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#findExecutionTokenAfter(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> findExecutionTokenAfter(ExecutionQueueType queueType, ExecutionToken<ExecutionTokenState> token) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstTokens = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			boolean isNext = false;
			for (ExecutionToken<ExecutionTokenState> executionToken : lstTokens) {
				if(isNext){
					return executionToken;
				}
				if(ObjectUtils.equals(token.getId(), executionToken.getId())){
					isNext = true;
				}				
			}
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionService#findExecutionTokenBefore(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> findExecutionTokenBefore(ExecutionQueueType queueType, ExecutionToken<ExecutionTokenState> token) throws GkException {
		List<ExecutionToken<ExecutionTokenState>> lstTokens = getExecutionQueue(queueType).getExecutionToken();
		if(CollectionUtils.isNotEmpty(lstTokens)){
			ExecutionToken<ExecutionTokenState> previous = null;
			for (ExecutionToken<ExecutionTokenState> executionToken : lstTokens) {				
				if(ObjectUtils.equals(token.getId(), executionToken.getId())){
					return previous;
				}				
				previous = executionToken;
			}
		}
		return null;
	}
	
	
}
