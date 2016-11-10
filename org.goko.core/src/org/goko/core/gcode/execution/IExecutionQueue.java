/**
 * 
 */
package org.goko.core.gcode.execution;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * Interface describing an execution queue
 * 
 * @author PsyKo
 * @date 17 oct. 2015
 */
public interface IExecutionQueue<S extends IExecutionTokenState, T extends IExecutionToken<S>> {

	boolean hasNext();

	void add(T token) throws GkException;

	void beginNextTokenExecution() throws GkException;

	void endCurrentTokenExecution() throws GkException;

	T getCurrentToken();

	void clear() throws GkException;

	T waitNext() throws GkException;
	
	List<T> getExecutionToken() throws GkException;
	
	ExecutionQueueType getType();

}