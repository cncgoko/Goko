package org.goko.core.execution.monitor.executionpart;

import java.util.Date;

import org.goko.common.bindings.AbstractModelObject;

/**
 * Model object for execution part 
 * @author Psyko
 *
 */
public class ExecutionPartModel extends AbstractModelObject {
	public static final String PROPERTY_COMPLETED_TOKEN_COUNT 	= "completedTokenCount";
	public static final String PROPERTY_TOTAL_TOKEN_COUNT 		= "totalTokenCount";
	public static final String PROPERTY_COMPLETED_LINE_COUNT 	= "completedLineCount";
	public static final String PROPERTY_TOTAL_LINE_COUNT 		= "totalLineCount";
	public static final String PROPERTY_TOKEN_LINE_COUNT 		= "tokenLineCount";
	public static final String PROPERTY_START_BUTTON_ENABLED	= "buttonStartEnabled";
	public static final String PROPERTY_PAUSE_BUTTON_ENABLED	= "buttonPauseEnabled";
	public static final String PROPERTY_STOP_BUTTON_ENABLED		= "buttonStopEnabled";
	public static final String PROPERTY_ELAPSED_TIME_STRING		= "elapsedTimeString";
	public static final String PROPERTY_ESTIMATED_TIME_STRING	= "estimatedTimeString";
	public static final String PROPERTY_TOTAL_PROGRESS_BAR_STATE= "progressBarState";
	public static final String PROPERTY_TOKEN_PROGRESS_BAR_STATE= "tokenProgressBarState";
		
	/** The number of completed token in the current execution queue */
	private int completedTokenCount;
	/** The number token in the current execution queue */
	private int totalTokenCount;
	/** The number completed line in the current token */
	private int completedLineCount;
	/** The number line in the entire execution queue */
	private int totalLineCount;
	/** The number line in the current token */
	private int tokenLineCount;
	/** The state of the start button */
	private boolean buttonStartEnabled;
	/** The state of the pause button */
	private boolean buttonPauseEnabled;
	/** The state of the stop button */
	private boolean buttonStopEnabled;
	/** The date of the execution start */
	private Date executionQueueStartDate;
	/** Boolean to enable/disable the execution timer*/
	private boolean executionTimerActive;
	/** Formatted elapsed time */
	private String elapsedTimeString;
	/** Formatted estimated execution time */
	private String estimatedTimeString;
	/** Displayed state of the progress bar */
	private int progressBarState;
	/** Displayed state of the current token progress bar */
	private int tokenProgressBarState;
	/** Store the number of line completed during execution of all tokens before the current one */
	private int lineCompleteFromCompleteToken;
	/** Number of line complete in the current token */
	private int lineCompleteInCurrentToken;
	
	/**
	 * @return the completedTokenCount
	 */
	public int getCompletedTokenCount() {
		return completedTokenCount;
	}
	/**
	 * @param completedTokenCount the completedTokenCount to set
	 */
	public void setCompletedTokenCount(int completedTokenCount) {
		firePropertyChange(PROPERTY_COMPLETED_TOKEN_COUNT, this.completedTokenCount, this.completedTokenCount = completedTokenCount);
	}
	/**
	 * @return the totalTokenCount
	 */
	public int getTotalTokenCount() {
		return totalTokenCount;
	}
	/**
	 * @param totalTokenCount the totalTokenCount to set
	 */
	public void setTotalTokenCount(int totalTokenCount) {
		firePropertyChange(PROPERTY_TOTAL_TOKEN_COUNT, this.totalTokenCount, this.totalTokenCount = totalTokenCount);
	}
	/**
	 * @return the completedLineCount
	 */
	public int getCompletedLineCount() {
		return completedLineCount;
	}
	/**
	 * @param completedLineCount the completedLineCount to set
	 */
	public void setCompletedLineCount(int completedLineCount) {
		firePropertyChange(PROPERTY_COMPLETED_LINE_COUNT, this.completedLineCount, this.completedLineCount = completedLineCount);
	}
	/**
	 * @return the totalLineCount
	 */
	public int getTotalLineCount() {
		return totalLineCount;
	}
	/**
	 * @param totalLineCount the totalLineCount to set
	 */
	public void setTotalLineCount(int totalLineCount) {
		firePropertyChange(PROPERTY_TOTAL_LINE_COUNT, this.totalLineCount, this.totalLineCount = totalLineCount);
	}
	/**
	 * @return the buttonStartEnabled
	 */
	public boolean isButtonStartEnabled() {
		return buttonStartEnabled;
	}
	/**
	 * @param buttonStartEnabled the buttonStartEnabled to set
	 */
	public void setButtonStartEnabled(boolean buttonStartEnabled) {
		firePropertyChange(PROPERTY_START_BUTTON_ENABLED, this.buttonStartEnabled, this.buttonStartEnabled = buttonStartEnabled);
	}
	/**
	 * @return the buttonPauseEnabled
	 */
	public boolean isButtonPauseEnabled() {
		return buttonPauseEnabled;
	}
	/**
	 * @param buttonPauseEnabled the buttonPauseEnabled to set
	 */
	public void setButtonPauseEnabled(boolean buttonPauseEnabled) {
		firePropertyChange(PROPERTY_PAUSE_BUTTON_ENABLED, this.buttonPauseEnabled, this.buttonPauseEnabled = buttonPauseEnabled);
	}
	/**
	 * @return the buttonStopEnabled
	 */
	public boolean isButtonStopEnabled() {
		return buttonStopEnabled;
	}
	/**
	 * @param buttonStopEnabled the buttonStopEnabled to set
	 */
	public void setButtonStopEnabled(boolean buttonStopEnabled) {
		firePropertyChange(PROPERTY_STOP_BUTTON_ENABLED, this.buttonStopEnabled, this.buttonStopEnabled = buttonStopEnabled);
	}
	/**
	 * @return the executionQueueStartDate
	 */
	public Date getExecutionQueueStartDate() {
		return executionQueueStartDate;
	}
	/**
	 * @param executionQueueStartDate the executionQueueStartDate to set
	 */
	public void setExecutionQueueStartDate(Date executionQueueStartDate) {
		this.executionQueueStartDate = executionQueueStartDate;
	}
	/**
	 * @return the executionTimerActive
	 */
	public boolean isExecutionTimerActive() {
		return executionTimerActive;
	}
	/**
	 * @param executionTimerActive the executionTimerActive to set
	 */
	public void setExecutionTimerActive(boolean executionTimerActive) {
		this.executionTimerActive = executionTimerActive;
	}
	/**
	 * @return the elapsedTimeString
	 */
	public String getElapsedTimeString() {
		return elapsedTimeString;
	}
	/**
	 * @param elapsedTimeString the elapsedTimeString to set
	 */
	public void setElapsedTimeString(String elapsedTimeString) {
		firePropertyChange(PROPERTY_ELAPSED_TIME_STRING, this.elapsedTimeString, this.elapsedTimeString = elapsedTimeString);		
	}
	/**
	 * @return the tokenLineCount
	 */
	public int getTokenLineCount() {
		return tokenLineCount;
	}
	/**
	 * @param tokenLineCount the tokenLineCount to set
	 */
	public void setTokenLineCount(int tokenLineCount) {
		firePropertyChange(PROPERTY_TOKEN_LINE_COUNT, this.tokenLineCount, this.tokenLineCount = tokenLineCount);
	}
	/**
	 * @return the estimatedTimeString
	 */
	public String getEstimatedTimeString() {
		return estimatedTimeString;
	}
	/**
	 * @param estimatedTimeString the estimatedTimeString to set
	 */
	public void setEstimatedTimeString(String estimatedTimeString) {
		firePropertyChange(PROPERTY_ESTIMATED_TIME_STRING, this.estimatedTimeString, this.estimatedTimeString = estimatedTimeString);
	}
	/**
	 * @return the progressBarState
	 */
	public int getProgressBarState() {
		return progressBarState;
	}
	/**
	 * @param progressBarState the progressBarState to set
	 */
	public void setProgressBarState(int progressBarState) {
		firePropertyChange(PROPERTY_TOTAL_PROGRESS_BAR_STATE	, this.progressBarState, this.progressBarState = progressBarState);
	}
	/**
	 * @return the tokenProgressBarState
	 */
	public int getTokenProgressBarState() {
		return tokenProgressBarState;
	}
	/**
	 * @param tokenProgressBarState the tokenProgressBarState to set
	 */
	public void setTokenProgressBarState(int tokenProgressBarState) {
		firePropertyChange(PROPERTY_TOKEN_PROGRESS_BAR_STATE	, this.tokenProgressBarState, this.tokenProgressBarState = tokenProgressBarState);
	}
	/**
	 * @return the lineCompleteFromCompleteToken
	 */
	public int getLineCompleteFromCompleteToken() {
		return lineCompleteFromCompleteToken;
	}
	/**
	 * @param lineCompleteFromCompleteToken the lineCompleteFromCompleteToken to set
	 */
	public void setLineCompleteFromCompleteToken(int lineCompleteFromCompleteToken) {
		this.lineCompleteFromCompleteToken = lineCompleteFromCompleteToken;
	}
	/**
	 * @return the lineCompleteInCurrentToken
	 */
	public int getLineCompleteInCurrentToken() {
		return lineCompleteInCurrentToken;
	}
	/**
	 * @param lineCompleteInCurrentToken the lineCompleteInCurrentToken to set
	 */
	public void setLineCompleteInCurrentToken(int lineCompleteInCurrentToken) {
		this.lineCompleteInCurrentToken = lineCompleteInCurrentToken;
	}	
	
}
