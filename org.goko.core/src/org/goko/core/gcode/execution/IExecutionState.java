/**
 * 
 */
package org.goko.core.gcode.execution;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public interface IExecutionState {
	/** Integer value of NONE state  */
	public static final int NONE_STATE = 0;
	
	/** Integer value of SENT state  */
	public static final int SENT_STATE = 1;
	
	/** Integer value of EXECUTED state  */
	public static final int EXECUTED_STATE = 2;
	
	/** Integer value of CONFIRMED state  */
	public static final int CONFIRMED_STATE = 3;
	
	/** Integer value of IGNORED state  */
	public static final int IGNORED_STATE = 4;
	
	/** Integer value of ERROR state  */
	public static final int ERROR_STATE = 5;
}
