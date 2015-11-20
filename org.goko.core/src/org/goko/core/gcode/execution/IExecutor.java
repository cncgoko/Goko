/**
 * 
 */
package org.goko.core.gcode.execution;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;

/**
 * Interface for a token execution mechanism
 * 
 * @author PsyKo
 */
public interface IExecutor<S extends IExecutionState, T extends IExecutionToken<S>> {
	
	/**
	 * Executes the given token
	 * 
	 * @param token the IExecutionToken to run
	 * @throws GkException GkException
	 */
	void executeToken(T token) throws GkException;

	/**
	 * Creates an execution token for the given GCodeProvider
	 * @param the target GCodeProvider
	 * @return T an execution token
	 * @throws GkException GkException
	 */
	T createToken(IGCodeProvider provider) throws GkException;
	
	
	void waitTokenComplete() throws GkException;
}
