/**
 * 
 */
package org.goko.core.gcode.execution;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 17 oct. 2015
 */
public interface IExecutionQueue<S extends IExecutionState, T extends IExecutionToken<S>> {

	boolean hasNext();

	void add(T token);

	void beginNextTokenExecution() throws GkException;

	void endCurrentTokenExecution() throws GkException;

	void setPaused(boolean paused) throws GkException;

	T getCurrentToken();

	void clear() throws GkException;

	T waitNext() throws GkException;

}