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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;

/**
 * @author PsyKo
 * @date 18 oct. 2015
 */
public class ExecutionQueue<S extends IExecutionTokenState, T extends IExecutionToken<S>> implements IExecutionQueue<S, T> {
	/** The list of execution tokens */
	private LinkedBlockingQueue<T> remainingExecutionTokens;
	/** The list of execution tokens */
	private CacheById<T> executionTokens;
	/** The current token */
	private T currentToken;
		
	/**
	 * Constructor
	 */
	public ExecutionQueue() {
		remainingExecutionTokens = new LinkedBlockingQueue<T>();
		executionTokens 		 = new CacheById<T>(new SequentialIdGenerator());
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#hasNext()
	 */
	@Override
	public boolean hasNext(){
		return CollectionUtils.isNotEmpty(remainingExecutionTokens);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#add(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void add(T token)throws GkException{
		token.setExecutionOrder(executionTokens.size());
		executionTokens.add(token);
		remainingExecutionTokens.offer(token);		
	}

	/** (inheritDoc)
	 * @see oldorg.goko.core.gcode.bean.execution.IExecutionQueue#beginNextTokenExecution()
	 */
	@Override
	public void beginNextTokenExecution() throws GkException{
		try {
			currentToken = remainingExecutionTokens.take();			
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
			currentToken = null;
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
		remainingExecutionTokens.clear();
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#waitNext()
	 */
	@Override
	public T waitNext() throws GkException {
		try {
			return remainingExecutionTokens.take();
		} catch (InterruptedException e) {
			throw new GkTechnicalException(e);
		}
	}
	
	public void delete(Integer idExecutionToken) throws GkException{		
		T tokenToRemove = executionTokens.get(idExecutionToken);
		remainingExecutionTokens.remove(tokenToRemove);
		executionTokens.remove(tokenToRemove);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionQueue#getExecutionToken()
	 */
	@Override
	public List<T> getExecutionToken() throws GkException {
		List<T> lst = executionTokens.get();
		Collections.sort(lst, new ExecutionOrderComparator());
		return lst;
	}
	
	/**
	 * Reset the execution queue for execution
	 * @throws GkException GkException 
	 */
	public void reset() throws GkException{
		remainingExecutionTokens = new LinkedBlockingQueue<T>();
		remainingExecutionTokens.addAll(getExecutionToken());
		for (T token : getExecutionToken()) {
			token.reset();
		}
	}
	
	public T getExecutionToken(Integer idExecutionToken) throws GkException{		
		return executionTokens.get(idExecutionToken);
	}
}
