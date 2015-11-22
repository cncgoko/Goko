/**
 * 
 */
package org.goko.core.gcode.execution;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class ExecutionState implements IExecutionState{
	/** Object value of NONE state  */
	public static final ExecutionState NONE = new ExecutionState(NONE_STATE);

	/** Object value of SENT state  */
	public static final ExecutionState SENT = new ExecutionState(SENT_STATE);
	
	/** Object value of EXECUTED state  */
	public static final ExecutionState EXECUTED = new ExecutionState(EXECUTED_STATE);

	/** Object value of CONFIRMED state  */
	public static final ExecutionState CONFIRMED = new ExecutionState(CONFIRMED_STATE);

	/** Object value of IGNORED state  */
	public static final ExecutionState IGNORED = new ExecutionState(IGNORED_STATE);

	/** Object value of ERROR state  */
	public static final ExecutionState ERROR = new ExecutionState(ERROR_STATE);
	
	/** Object value of RUNNING state  */
	public static final ExecutionState RUNNING = new ExecutionState(RUNNING_STATE);
	
	/** Object value of STOPPED state  */
	public static final ExecutionState STOPPED = new ExecutionState(STOPPED_STATE);
	
	/** Object value of PAUSED state  */
	public static final ExecutionState PAUSED = new ExecutionState(PAUSED_STATE);
	
	/** Execution state */
	private int state;

	
	/**
	 * Constructor
	 * @param state the state 
	 */
	public ExecutionState(int state) {
		super();
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + state;
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecutionState other = (ExecutionState) obj;
		if (state != other.state)
			return false;
		return true;
	}
	
	
}
