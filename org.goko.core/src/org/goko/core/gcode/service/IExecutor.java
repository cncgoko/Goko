/**
 * 
 */
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.IExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;

/**
 * Interface for a token execution mechanism
 * 
 * @author PsyKo
 * @date 15 nov. 2015
 */
public interface IExecutor<S extends IExecutionState, T extends IExecutionToken<S>> {
	
	/**
	 * Executes the given token
	 * 
	 * @param token the IExecutionToken to run
	 * @throws GkException GkException
	 */
	public void executeToken(T token) throws GkException;
	
}
