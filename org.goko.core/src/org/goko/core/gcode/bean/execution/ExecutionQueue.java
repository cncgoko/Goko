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
package org.goko.core.gcode.bean.execution;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.provider.GCodeExecutionToken;

public class ExecutionQueue<T extends GCodeExecutionToken> {
	private LinkedBlockingQueue<T> executionTokens;
	private T currentToken;

	public ExecutionQueue() {
		executionTokens = new LinkedBlockingQueue<T>();
	}

	public boolean hasNext(){
		return CollectionUtils.isNotEmpty(executionTokens);
	}

	public void add(T token){
		executionTokens.offer(token);
	}

	public void beginNextTokenExecution() throws GkException{
		try {
			currentToken = executionTokens.take();
			currentToken.beginExecution();
		} catch (InterruptedException e) {
			throw new GkTechnicalException(e);
		}
	}

	public void endCurrentTokenExecution() throws GkException{
		if(currentToken != null){
		//	throw new GkFunctionalException("ExecutionQueue : no execution token currently started.");
			currentToken.endExecution();
			currentToken = null;
		}
	}

	/**
	 * @return the currentToken
	 */
	public T getCurrentToken() {
		return currentToken;
	}

	public void clear() throws GkException{
		endCurrentTokenExecution();
		executionTokens.clear();
	}

	public T waitNext() throws GkException {
		try {
			return executionTokens.take();
		} catch (InterruptedException e) {
			throw new GkTechnicalException(e);
		}
	}
}
