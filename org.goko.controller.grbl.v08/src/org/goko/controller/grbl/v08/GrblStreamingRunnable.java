/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v08;

import org.apache.commons.lang3.StringUtils;
import org.goko.common.events.GCodeCommandSelectionEvent;
import org.goko.controller.grbl.v08.executionqueue.GrblGCodeExecutionToken;
import org.goko.core.common.event.GokoEventBus;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.log.GkLog;

/**
 * Thread for streaming GCode command
 * @author PsyKo
 *
 */
public class GrblStreamingRunnable  implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(GrblStreamingRunnable.class);
	/** The execution queue */
	private ExecutionQueue<ExecutionState, GrblGCodeExecutionToken> executionQueue;
	/** The GrblService running */
	private GrblControllerService grblService;

	private Object bufferSpaceMutex = new Object();

	/**
	 * @param queue
	 * @param grblService
	 */
	GrblStreamingRunnable(ExecutionQueue<ExecutionState, GrblGCodeExecutionToken> queue, GrblControllerService grblService) {
		super();
		this.executionQueue = queue;
		this.grblService = grblService;
	}

	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){
			try{
				LOG.info("Waiting for an execution token");
				executionQueue.beginNextTokenExecution();
				LOG.info("Begin execution token");
				runExecutionToken();
				// Let's wait until the execution token is complete				
				waitTokenComplete();				
				executionQueue.endCurrentTokenExecution();
			}catch(GkException e){
				LOG.error(e);
			}
		}
	}


	protected void runExecutionToken() throws GkException{
		try{
			while(executionQueue.getCurrentToken() != null && executionQueue.getCurrentToken().hasMoreLine()){
				GrblGCodeExecutionToken token = executionQueue.getCurrentToken();
				waitTokenUnpaused();
				if(token.hasMoreLine()){
					GCodeLine command = token.getNextLine();
					waitBufferSpace(StringUtils.length(command.getStringCommand()));
					command = token.takeNextLine();
					if(command.getType() == EnumGCodeCommandType.COMMENT){
						// Skip comments
						token.markAsExecuted(command.getId());
					}else{
						grblService.sendCommand(command);
						token.markAsSent( command.getId() );
					}
					GokoEventBus.getInstance().post(new GCodeCommandSelectionEvent(command));
				}
			}
			LOG.info("runExecutionToken ends");
		}catch(GkException e){
			LOG.error(e);
		}
	}
	/**
	 * Wait until the current token is complete
	 * @throws GkException GkException
	 */
	private void waitTokenComplete() throws GkException {
		while(executionQueue.getCurrentToken() != null && !executionQueue.getCurrentToken().isComplete()){
			synchronized ( executionQueue.getCurrentToken() ) {
				try {
					// Timeout of 500ms in case the token get cancelled or remove from queue
					executionQueue.getCurrentToken().wait(500);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}
	/**
	 * Wait while the current token is paused.
	 * @throws GkException GkException
	 */
	private void waitTokenUnpaused() throws GkException {
		while(executionQueue.getCurrentToken() != null && executionQueue.getCurrentToken().isPaused()){
			synchronized ( executionQueue.getCurrentToken() ) {
				try {
					executionQueue.getCurrentToken().wait(500);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}
	
	/**
	 * Wait until there is enough space in Grbl buffer
	 * @throws GkException GkException
	 */
	private void waitBufferSpace(int size) throws GkException {
		while(executionQueue.getCurrentToken() != null && (grblService.getUsedGrblBuffer() + size) > Grbl.GRBL_BUFFER_SIZE){
			synchronized ( bufferSpaceMutex ) {
				try {
					bufferSpaceMutex.wait();
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	protected void releaseBufferSpaceMutex(){
		synchronized (bufferSpaceMutex) {
			bufferSpaceMutex.notify();
		}
	}
}
