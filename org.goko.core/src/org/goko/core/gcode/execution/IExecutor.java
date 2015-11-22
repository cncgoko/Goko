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
	 * Allow to implement controls before execution of the complete queue by this executor
	 * @return <code>true</code> if the executor is ready for execution, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isReadyForQueueExecution() throws GkException;
		
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
	
	/**
	 * Return the target token 
	 * @return the execution token
	 * @throws GkException GkException
	 */
	T getToken() throws GkException;
}
