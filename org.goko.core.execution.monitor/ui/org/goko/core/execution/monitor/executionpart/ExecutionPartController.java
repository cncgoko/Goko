package org.goko.core.execution.monitor.executionpart;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.event.ExecutionServiceWorkspaceEvent;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.workspace.service.IWorkspaceEvent;
import org.goko.core.workspace.service.IWorkspaceListener;
import org.goko.core.workspace.service.IWorkspaceService;

public class ExecutionPartController extends AbstractController<ExecutionPartModel> implements IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>, IWorkspaceListener {
	/** The execution service */
	@Inject
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;
	/** The underlying workspace service */
	@Inject
	private IWorkspaceService workspaceService;
	
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
		workspaceService.addWorkspaceListener(this);
		updateTokenQueueData();
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {		
		this.getDataModel().setCompletedLineCount(0);
		this.getDataModel().setTotalLineCount(token.getLineCount());
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		updateButtonState();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		updateButtonState();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {
		updateTokenQueueData();
		updateButtonState();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		int executedLineCount = CollectionUtils.size(token.getLineByState(ExecutionTokenState.EXECUTED));		
		this.getDataModel().setCompletedLineCount(executedLineCount);		
	}

	/**
	 * @param event
	 * @throws GkException
	 */
	@Override
	public void onWorkspaceEvent(IWorkspaceEvent event) throws GkException {
		updateButtonState();
		if(StringUtils.equalsIgnoreCase(event.getType(), ExecutionServiceWorkspaceEvent.TYPE)){
			updateTokenQueueData();
		}
	}

	public void beginQueueExecution() throws GkException {
		executionService.beginQueueExecution();
	}
	
	public void pauseResumeQueueExecution() throws GkException {
		if(executionService.getExecutionState() == ExecutionState.PAUSED){
			executionService.resumeQueueExecution();
		}else if(executionService.getExecutionState() == ExecutionState.RUNNING){
			executionService.pauseQueueExecution();
		}
	}
	
	public void stopQueueExecution() throws GkException {		
		executionService.stopQueueExecution();		
	}

	private void updateButtonState() throws GkException{		
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
		case RUNNING: buttonStartEnabled = false;
					  buttonPauseEnabled = true;
					  buttonStopEnabled = true;
				break;		
		}
		
		getDataModel().setButtonStartEnabled(buttonStartEnabled);
		getDataModel().setButtonPauseEnabled(buttonPauseEnabled);
		getDataModel().setButtonStopEnabled( buttonStopEnabled);
	}
	
	private void updateTokenQueueData() throws GkException {		
		List<ExecutionToken<ExecutionTokenState>> lstToken = executionService.getExecutionQueue().getExecutionToken();
		int tokenCount = CollectionUtils.size(lstToken);
		this.getDataModel().setTotalTokenCount(tokenCount);
		
		int totalTokenCount 	= 0;
		int completedTokenCount = 0;
		for (ExecutionToken<ExecutionTokenState> executionToken : lstToken) {			
			totalTokenCount += 1;
			if(executionToken.getState() == ExecutionState.COMPLETE){
				completedTokenCount += 1;	
			}
		}
		this.getDataModel().setCompletedTokenCount(completedTokenCount);
		this.getDataModel().setTotalTokenCount(totalTokenCount);
	}
}
