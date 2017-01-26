/**
 * 
 */
package org.goko.core.gcode.execution;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class ExecutionTokenState implements IExecutionTokenState{
	/** Object value of NONE state  */
	public static final ExecutionTokenState NONE = new ExecutionTokenState(NONE_STATE, "NONE_STATE");

	/** Object value of SENT state  */
	public static final ExecutionTokenState SENT = new ExecutionTokenState(SENT_STATE, "SENT_STATE");
	
	/** Object value of EXECUTED state  */
	public static final ExecutionTokenState EXECUTED = new ExecutionTokenState(EXECUTED_STATE, "EXECUTED_STATE");

	/** Object value of CONFIRMED state  */
	public static final ExecutionTokenState CONFIRMED = new ExecutionTokenState(CONFIRMED_STATE, "CONFIRMED_STATE");

	/** Object value of IGNORED state  */
	public static final ExecutionTokenState IGNORED = new ExecutionTokenState(IGNORED_STATE, "IGNORED_STATE");

	/** Object value of ERROR state  */
	public static final ExecutionTokenState ERROR = new ExecutionTokenState(ERROR_STATE, "ERROR_STATE");
	
	/** Object value of RUNNING state  */
	public static final ExecutionTokenState RUNNING = new ExecutionTokenState(RUNNING_STATE, "RUNNING_STATE");
	
	/** Object value of STOPPED state  */
	public static final ExecutionTokenState STOPPED = new ExecutionTokenState(STOPPED_STATE, "STOPPED_STATE");
	
	/** Object value of PAUSED state  */
	public static final ExecutionTokenState PAUSED = new ExecutionTokenState(PAUSED_STATE, "PAUSED_STATE");
	
	/** Execution state */
	private int state;
	/** Satate label*/
	private String label;

	
	/**
	 * Constructor
	 * @param state the state 
	 */
	public ExecutionTokenState(int state, String label) {
		super();
		this.state = state;
		this.label = label;
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
		ExecutionTokenState other = (ExecutionTokenState) obj;
		if (state != other.state)
			return false;
		return true;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		return label;
	}
}
