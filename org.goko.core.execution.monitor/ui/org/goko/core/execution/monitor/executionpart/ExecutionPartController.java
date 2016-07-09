package org.goko.core.execution.monitor.executionpart;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionQueue;
import org.goko.core.gcode.service.IExecutionQueueListener;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;

/**
 * Part displaying execution state and allowing to control it
 *
 * @author Psyko
 */
public class ExecutionPartController extends AbstractController<ExecutionPartModel> implements 	IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>,																								
																								IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	private static final GkLog LOG = GkLog.getLogger(ExecutionPartController.class);
	/** The execution service */
	@Inject
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;
	/** The optional execution time evaluation service */
	@Inject
	@Optional
	private IGCodeExecutionTimeService executionTimeService;
	/** Job for execution time estimation */
	private Job executionUpdater;
	/** Map of execution time by token id */
	private Map<Integer, Time> executionTimeByToken;
	
	/** Constructor */
	public ExecutionPartController() {
		super(new ExecutionPartModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		executionService.addExecutionListener(this);
		executionService.addExecutionQueueListener(this);
		updateTokenQueueData();
		updateButtonState();
		executionTimeByToken = new HashMap<Integer, Time>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {		
		this.getDataModel().setCompletedLineCount(0);		
		this.getDataModel().setTokenLineCount(token.getLineCount());
		this.getDataModel().setLineCompleteInCurrentToken(0);
		updateButtonState();
		updateCompletedLineCount();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		updateButtonState();
		this.getDataModel().setExecutionTimerActive(false);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		updateEstimatedExecutionTime();
		updateButtonState();
		this.getDataModel().setExecutionQueueStartDate(new Date());
		this.getDataModel().setLineCompleteFromCompleteToken(0);
		this.getDataModel().setExecutionTimerActive(true);
		updateQueueExecutionState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {		
		updateButtonState();
		updateQueueExecutionState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		updateButtonState();
		updateQueueExecutionState();
		this.getDataModel().setExecutionTimerActive(false);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {
		updateButtonState();
		updateQueueExecutionState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(ExecutionToken<ExecutionTokenState> token) throws GkException {
		updateButtonState();
		updateQueueExecutionState();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {
		getDataModel().setLineCompleteInCurrentToken(0);		
		updateTokenQueueData();
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {		
		if(token.getState() == ExecutionState.RUNNING){
			getDataModel().setLineCompleteInCurrentToken(token.getLineCountByState(ExecutionTokenState.EXECUTED));
		}else if(token.getState() == ExecutionState.COMPLETE){
			getDataModel().setLineCompleteInCurrentToken(0);
		}
		updateCompletedLineCount();
	}
	
	/**
	 * Updates the total number of completed lines 
	 */
	protected void updateCompletedLineCount(){
		int executedLineCount = getDataModel().getLineCompleteInCurrentToken() + getDataModel().getLineCompleteFromCompleteToken();
		this.getDataModel().setCompletedLineCount(executedLineCount);
	}

	/**
	 * Starts the execution queue
	 * @throws GkException GkException
	 */
	public void beginQueueExecution() throws GkException {
		executionService.beginQueueExecution();
		this.getDataModel().setExecutionQueueStartDate(new Date());
		this.getDataModel().setExecutionTimerActive(true);
	}

	/**
	 * Pauses the execution queue
	 * @throws GkException GkException
	 */
	public void pauseResumeQueueExecution() throws GkException {
		if(executionService.getExecutionState() == ExecutionState.PAUSED){
			executionService.resumeQueueExecution();
		}else if(executionService.getExecutionState() == ExecutionState.RUNNING){
			executionService.pauseQueueExecution();
		}
	}

	/**
	 * Stops the execution queue
	 * @throws GkException GkException
	 */
	public void stopQueueExecution() throws GkException {
		executionService.stopQueueExecution();
	}

	/**
	 * Update the state of the buttons according to the execution state
	 * @throws GkException GkException
	 */
	private void updateButtonState(){
		try{
			boolean buttonStopEnabled = false;
			boolean buttonStartEnabled= false;
			boolean buttonPauseEnabled = false;
	
			switch(executionService.getExecutionState()){
			case IDLE:
			case STOPPED:
			case COMPLETE:  buttonStartEnabled = true;
						buttonPauseEnabled = false;
						buttonStopEnabled = false;
					break;
			case PAUSED:
			case ERROR:
			case RUNNING: buttonStartEnabled = false;
						  buttonPauseEnabled = true;
						  buttonStopEnabled = true;
					break;
			}
				
			if(executionService.getExecutionQueue() != null){
				if(CollectionUtils.isEmpty(executionService.getExecutionQueue().getExecutionToken())){
					buttonStartEnabled = false;
				}else{
					buttonStartEnabled = !isErrorInQueue(executionService.getExecutionQueue());
				}
			}
			getDataModel().setButtonStartEnabled(buttonStartEnabled);
			getDataModel().setButtonPauseEnabled(buttonPauseEnabled);
			getDataModel().setButtonStopEnabled( buttonStopEnabled);
		}catch(GkException e){
			LOG.error(e);
			getDataModel().setButtonStartEnabled(false);
			getDataModel().setButtonPauseEnabled(false);
			getDataModel().setButtonStopEnabled( true );
		}
	}
	
	/**
	 * Detects error in the given queue
	 * @param iExecutionQueue the queue to check
	 * @return <code>true</code> if there is any error in the queue, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	private boolean isErrorInQueue(IExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> iExecutionQueue) throws GkException{
		if(iExecutionQueue == null){
			return false;
		}
		List<ExecutionToken<ExecutionTokenState>> tokens = iExecutionQueue.getExecutionToken();
		if(CollectionUtils.isNotEmpty(tokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : tokens) {
				if(executionToken.hasErrors()){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Update the data about the execution of the queue
	 * @throws GkException GkException
	 */
	private void updateQueueExecutionState() throws GkException{
		if(executionService.getExecutionState() == ExecutionState.RUNNING){
			getDataModel().setProgressBarState(SWT.NORMAL);
			getDataModel().setTokenProgressBarState(SWT.NORMAL);
		}else if(executionService.getExecutionState() == ExecutionState.PAUSED){
			getDataModel().setProgressBarState(SWT.PAUSED);
			getDataModel().setTokenProgressBarState(SWT.PAUSED);
		}else if(executionService.getExecutionState() == ExecutionState.STOPPED){
			getDataModel().setProgressBarState(SWT.ERROR);
			getDataModel().setTokenProgressBarState(SWT.ERROR);
		}
	}
	/**
	 * Update the estimated execution time
	 * @throws GkException GkException
	 */
	private void updateEstimatedExecutionTime() throws GkException{
		if(executionUpdater == null || executionUpdater.getState() == Job.NONE){
			scheduleExecutionTimeEstimationJob();
		}
	}

	private void scheduleExecutionTimeEstimationJob() throws GkException{
		scheduleExecutionTimeEstimationJob(null);
	}
	
	private void scheduleExecutionTimeEstimationJob(final Integer idToken) {
		executionUpdater = new Job("Updating execution time..."){
			/** (inheritDoc)
			 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if(executionTimeService != null){
					try {
						List<ExecutionToken<ExecutionTokenState>> lstToken = executionService.getExecutionQueue().getExecutionToken();
						Time estimatedTime = Time.ZERO;
						if(CollectionUtils.isNotEmpty(lstToken)){
							SubMonitor subMonitor = SubMonitor.convert(monitor,"Computing time...", lstToken.size());
							// Refresh the unitary times first
							for (ExecutionToken<ExecutionTokenState> executionToken : lstToken) {
								Time tokenTime = executionTimeByToken.get(executionToken.getId());
								if(tokenTime == null || idToken == null || ObjectUtils.equals(idToken, executionToken.getId())){
									tokenTime = executionTimeService.evaluateExecutionTime(executionToken.getGCodeProvider());
									executionTimeByToken.put(executionToken.getId(), tokenTime);
								}
								subMonitor.worked(1);
								estimatedTime = estimatedTime.add(tokenTime);
							}							
							subMonitor.done();
						}
						long estimatedMs = (long)estimatedTime.doubleValue(TimeUnit.MILLISECOND);
						ExecutionPartController.this.getDataModel().setEstimatedTimeString(DurationFormatUtils.formatDuration(estimatedMs, "HH:mm:ss"));
					} catch (GkException e) {
						LOG.error(e);
					}
				}
				return Status.OK_STATUS;
			}
		};
		executionUpdater.setUser(true);
		executionUpdater.schedule(100);
	}

	/**
	 * Update the data about the execution queue
	 * @throws GkException GkException
	 */
	private void updateTokenQueueData() {
		try{
			List<ExecutionToken<ExecutionTokenState>> lstToken = executionService.getExecutionQueue().getExecutionToken();
			int tokenCount = CollectionUtils.size(lstToken);
			this.getDataModel().setTotalTokenCount(tokenCount);
	
			int totalTokenCount 	= 0;
			int completedTokenCount = 0;
			int totalLineCount		= 0;
			int completedLineCount  = 0;
			
			for (ExecutionToken<ExecutionTokenState> executionToken : lstToken) {
				totalTokenCount += 1;
				totalLineCount += executionToken.getLineCount();
				if(executionToken.getState() == ExecutionState.COMPLETE){
					completedTokenCount += 1;
					completedLineCount += executionToken.getLineCount();					
				}				
			}
			this.getDataModel().setCompletedTokenCount(completedTokenCount);
			this.getDataModel().setTotalTokenCount(totalTokenCount);
			this.getDataModel().setTotalLineCount(totalLineCount);
			this.getDataModel().setLineCompleteFromCompleteToken(completedLineCount);
			updateCompletedLineCount();
			updateEstimatedExecutionTime();
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/**
	 * @return the executionService
	 */
	public IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> getExecutionService() {
		return executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenCreate(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenCreate(ExecutionToken<ExecutionTokenState> token) {
		updateButtonState();
		updateTokenQueueData();
		scheduleExecutionTimeEstimationJob(token.getId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenDelete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenDelete(ExecutionToken<ExecutionTokenState> token) {
		updateButtonState();
		updateTokenQueueData();
		executionTimeByToken.remove(token.getId());
		scheduleExecutionTimeEstimationJob(token.getId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenUpdate(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenUpdate(ExecutionToken<ExecutionTokenState> token) {
		updateButtonState();
		scheduleExecutionTimeEstimationJob(token.getId());
	}

	
}
