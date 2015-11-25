/**
 * 
 */
package org.goko.core.gcode.execution;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IExecutionService;

/**
 * Interface for a token execution mechanism
 * 
 * @author PsyKo
 */
public interface IExecutor<S extends IExecutionTokenState, T extends IExecutionToken<S>> extends IExecutionControl {
	
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
	
	
	boolean isTokenComplete() throws GkException;
	
	void waitTokenComplete() throws GkException;
	
	/**
	 * Return the target token 
	 * @return the execution token
	 * @throws GkException GkException
	 */
	T getToken() throws GkException;
	
	/**
	 * Setter for the running IExecutionService
	 * @param executionService the IExecutionService running this executor
	 * @throws GkException GkException
	 */
	void setExecutionService(IExecutionService<S, T> executionService) throws GkException;

	/**
	 * Getter for the running Setter for the running IExecutionService
	 * @return the IExecutionService
	 * @throws GkException GkException
	 */
	IExecutionService<S, T> getExecutionService() throws GkException;

}
