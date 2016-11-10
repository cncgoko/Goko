/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.gcode.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.execution.IExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;

public interface IExecutionService<S extends IExecutionTokenState, T extends IExecutionToken<S>> {

	void setExecutor(IExecutor<S, T> executor) throws GkException;
	
	T addToExecutionQueue(IGCodeProvider gcodeProvider) throws GkException;
	
	T addToExecutionQueue(ExecutionQueueType type, IGCodeProvider gcodeProvider) throws GkException;
	
	void addToExecutionQueue(T executionToken) throws GkException;	
	
	void addToExecutionQueue(ExecutionQueueType type, T executionToken) throws GkException;
	
	void clearExecutionQueue(ExecutionQueueType queueType) throws GkException;
	
	void removeFromExecutionQueue(ExecutionQueueType queueType, Integer idExecutionToken) throws GkException;
	
	void removeFromExecutionQueue(ExecutionQueueType queueType, T executionToken) throws GkException;
	
	void addExecutionListener(ExecutionQueueType queueType, IGCodeExecutionListener<S,T> listener) throws GkException;

	void removeExecutionListener(IGCodeExecutionListener<S,T> listener) throws GkException;
	
	void removeExecutionListener(ExecutionQueueType queueType, IGCodeExecutionListener<S,T> listener) throws GkException;

	void notifyQueueExecutionStart(ExecutionQueueType queueType) throws GkException;
	
	void notifyExecutionStart(T token) throws GkException;
	
	void notifyCommandStateChanged(T token, Integer idCommand) throws GkException;

	void notifyExecutionCanceled(T token) throws GkException;

	void notifyExecutionPause(T token) throws GkException;
	
	void notifyExecutionResume(T token) throws GkException;

	void notifyExecutionComplete(T token) throws GkException;
	
	void notifyQueueExecutionComplete(ExecutionQueueType queueType) throws GkException;
	
	void notifyQueueExecutionCanceled(ExecutionQueueType queueType) throws GkException;
	
	// Controls over the execution queue
	
	void beginQueueExecution(ExecutionQueueType queueType) throws GkException;
	
	void pauseQueueExecution() throws GkException;
	
	void resumeQueueExecution() throws GkException;
	
	void stopQueueExecution() throws GkException;

	ExecutionState getExecutionState() throws GkException;
	
	ExecutionQueue<S, T> findRunningExecutionQueue() throws GkException;
	
	List<ExecutionQueue<S, T>> getExecutionQueue() throws GkException;
	
	ExecutionQueue<S, T> getExecutionQueue(ExecutionQueueType type) throws GkException;
	
	void addExecutionQueueListener(IExecutionQueueListener<S, T> listener) throws GkException;
	
	void removeExecutionQueueListener(IExecutionQueueListener<S, T> listener) throws GkException;
	
	boolean isReadyForExecution() throws GkException;
	// Accessibility
	
	T getExecutionTokenByGCodeProvider(ExecutionQueueType queueType, IGCodeProvider gcodeProvider) throws GkException;
	
	T findExecutionTokenByGCodeProvider(ExecutionQueueType queueType, IGCodeProvider gcodeProvider) throws GkException;
	
	T findExecutionTokenAfter(ExecutionQueueType queueType, T token) throws GkException;
	
	T findExecutionTokenBefore(ExecutionQueueType queueType, T token) throws GkException;
}
