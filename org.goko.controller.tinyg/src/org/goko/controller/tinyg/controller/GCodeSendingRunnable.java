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
package org.goko.controller.tinyg.controller;

import java.util.ArrayList;

import org.goko.common.events.GCodeCommandSelectionEvent;
import org.goko.core.common.event.GokoEventBus;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.execution.ExecutionQueue;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.log.GkLog;

public class GCodeSendingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(GCodeSendingRunnable.class);
	private static final int BUFFER_AVAILABLE_REQUIRED_COUNT = 5;
	private ExecutionQueue<TinyGExecutionToken> executionQueue;
	private TinyGControllerService tinyGControllerService;
	private IGCodeExecutionMonitorService executionMonitorService;
	private Object ackMutex = new Object();
	private Object qrMutex = new Object();
	private int pendingCommands;

	public GCodeSendingRunnable(ExecutionQueue<TinyGExecutionToken> queue, TinyGControllerService controllerService) {
		this.executionQueue = queue;
		this.tinyGControllerService = controllerService;
		pendingCommands = 0;

	}

	public void confirmCommand(){
		synchronized (ackMutex) {
			pendingCommands = Math.max(0,pendingCommands - 1);
			ackMutex.notify();
		}
	}

	public void notifyBufferSpace() {
		synchronized (qrMutex) {
			qrMutex.notify();
		}
	}

	@Override
	public void run() {
		while(true){
			try{
				executionQueue.beginNextTokenExecution();
				runExecutionToken();
				waitTokenComplete();
				executionQueue.endCurrentTokenExecution();
			}catch(GkException e){
				LOG.error(e);
			}
		}
	}

	private void waitTokenComplete() throws GkException {
		while (executionQueue.getCurrentToken() != null && !executionQueue.getCurrentToken().isComplete()) {
			synchronized (executionQueue.getCurrentToken()) {
				try {
					// Timeout of 500ms in case the token get cancelled or
					// remove from queue
					executionQueue.getCurrentToken().wait(500);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	/**
	 * Execute the given execution token
	 * @param token the token to execute
	 * @throws GkException GkException
	 */
	protected void runExecutionToken() throws GkException{
		while(executionQueue.getCurrentToken() != null && executionQueue.getCurrentToken().hasMoreCommand()){
			TinyGExecutionToken token = executionQueue.getCurrentToken();
			int nbCommandToSend = computeNumberCommandToSend();

			ArrayList<GCodeCommand> lstCommand = new ArrayList<GCodeCommand>();

			while(token.hasMoreCommand() && nbCommandToSend > 0){
				waitTokenUnpaused();
				GCodeCommand currentCommand = token.takeNextCommand();
				lstCommand.add(currentCommand);
				nbCommandToSend--;
				token.markAsSent(currentCommand.getId());
				
				GokoEventBus.getInstance().post(new GCodeCommandSelectionEvent(currentCommand));
				tinyGControllerService.send(currentCommand);
				pendingCommands++;
			}
			waitLastCommandAcknowledgement();
			waitPlannerBufferSpaceAvailable();
		}
	}

	protected int computeNumberCommandToSend() throws GkException{
		int nb = 1;
		if(tinyGControllerService.isPlannerBufferSpaceCheck() && tinyGControllerService.getAvailableBuffer() > 4){
			nb = Math.max(1, tinyGControllerService.getAvailableBuffer() - 4);
		}	 
		return nb;
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
	
	protected void waitLastCommandAcknowledgement(){
		if(!tinyGControllerService.isPlannerBufferSpaceCheck()){
			return;
		}
		synchronized ( ackMutex ) {
			while(pendingCommands > 0){
				try {
					ackMutex.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void waitPlannerBufferSpaceAvailable() throws GkException{
		if(!tinyGControllerService.isPlannerBufferSpaceCheck()){
			return;
		}
		do{
			synchronized ( qrMutex ) {
				try {
					qrMutex.wait(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while(tinyGControllerService.getAvailableBuffer() < BUFFER_AVAILABLE_REQUIRED_COUNT);
	}

	private void clearAllPendingCommands(){
		synchronized (ackMutex) {
			pendingCommands = 0;
			ackMutex.notify();
		}
	}
	public void stop() throws GkException{
		clearAllPendingCommands();
		executionQueue.clear();
	}
}
