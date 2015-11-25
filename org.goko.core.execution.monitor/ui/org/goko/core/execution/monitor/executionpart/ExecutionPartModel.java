package org.goko.core.execution.monitor.executionpart;

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
	public static final String PROPERTY_START_BUTTON_ENABLED	= "buttonStartEnabled";
	public static final String PROPERTY_PAUSE_BUTTON_ENABLED	= "buttonPauseEnabled";
	public static final String PROPERTY_STOP_BUTTON_ENABLED		= "buttonStopEnabled";
	
	/** The number of completed token in the current execution queue */
	private int completedTokenCount;
	/** The number token in the current execution queue */
	private int totalTokenCount;
	/** The number completed line in the current token */
	private int completedLineCount;
	/** The number line in the current token */
	private int totalLineCount;
	/** The state of the start button */
	private boolean buttonStartEnabled;
	/** The state of the pause button */
	private boolean buttonPauseEnabled;
	/** The state of the stop button */
	private boolean buttonStopEnabled;
	
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
	
	
}
