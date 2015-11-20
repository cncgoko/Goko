/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.execution;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class ExecutionQueue<S extends IExecutionState, T extends IExecutionToken<S>> implements IExecutionQueue<S, T> {
	/** The list of execution tokens */
	private LinkedBlockingQueue<T> executionTokens;
	/** The current token */
	private T currentToken;
	/** The running state of this queue */
	private boolean running;
	
	/**
	 * Constructor
	 */
	public ExecutionQueue() {
		executionTokens = new LinkedBlockingQueue<T>();
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#hasNext()
	 */
	@Override
	public boolean hasNext(){
		return CollectionUtils.isNotEmpty(executionTokens);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#add(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void add(T token){
		executionTokens.offer(token);
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#beginNextTokenExecution()
	 */
	@Override
	public void beginNextTokenExecution() throws GkException{
		try {
			currentToken = executionTokens.take();
			currentToken.beginExecution();
		} catch (InterruptedException e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#endCurrentTokenExecution()
	 */
	@Override
	public void endCurrentTokenExecution() throws GkException{
		if(currentToken != null){
		//	throw new GkFunctionalException("ExecutionQueue : no execution token currently started.");
			currentToken.endExecution();
			currentToken = null;
		}
	}
	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#setPaused(boolean)
	 */
	@Override
	public void setPaused(boolean paused) throws GkException{
		if(currentToken != null){
			currentToken.setExecutionPaused(paused);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#getCurrentToken()
	 */
	@Override
	public T getCurrentToken() {
		return currentToken;
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#clear()
	 */
	@Override
	public void clear() throws GkException{
		endCurrentTokenExecution();
		executionTokens.clear();
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#waitNext()
	 */
	@Override
	public T waitNext() throws GkException {
		try {
			return executionTokens.take();
		} catch (InterruptedException e) {
			throw new GkTechnicalException(e);
		}
	}
	
	public void delete(Integer idExecutionToken) throws GkException{
		if(CollectionUtils.isNotEmpty(executionTokens)){
			T tokenToRemove = null;
			for (T token : executionTokens) {
				if(ObjectUtils.equals(token.getId(), idExecutionToken)){
					tokenToRemove = token;
				}
			}
			if(tokenToRemove != null){
				executionTokens.remove(tokenToRemove);
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
