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
package org.goko.tinyg.controller;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.goko.common.events.GCodeCommandSelectionEvent;
import org.goko.core.common.event.GokoEventBus;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken;
import org.goko.core.log.GkLog;

public class GCodeSendingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(GCodeSendingRunnable.class);
	private static final int BUFFER_AVAILABLE_REQUIRED_COUNT = 5;
	private ExecutionQueue executionQueue;
	private TinyGControllerService tinyGControllerService;
	private Object ackMutex = new Object();
	private Object qrMutex = new Object();
	private int pendingCommands;

	public GCodeSendingRunnable(ExecutionQueue queue, TinyGControllerService controllerService) {
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
				runExecutionToken(executionQueue.getCurrentToken());
				executionQueue.endCurrentTokenExecution();
			}catch(GkException e){
				LOG.error(e);
			}
		}
	}

	/**
	 * Execute the given execution token
	 * @param token the token to execute
	 * @throws GkException GkException
	 */
	protected void runExecutionToken(GCodeStreamedExecutionToken token) throws GkException{
		while(token.hasNext()){
			int nbCommandToSend = computeNumberCommandToSend();

			ArrayList<GCodeCommand> lstCommand = new ArrayList<GCodeCommand>();

			while(token.hasNext() && nbCommandToSend > 0){
				GCodeCommand currentCommand = token.unstackNextCommand();
				lstCommand.add(currentCommand);
				nbCommandToSend--;
				currentCommand.setState(new GCodeCommandState(GCodeCommandState.SENT));
				token.setCommandState(currentCommand, GCodeCommandState.SENT);
				GokoEventBus.getInstance().post(new GCodeCommandSelectionEvent(currentCommand));
				pendingCommands++;
			}

			if(CollectionUtils.isNotEmpty(lstCommand)){
				tinyGControllerService.sendTogether(lstCommand);
			}

			waitLastCommandAcknowledgement();
			waitPlannerBufferSpaceAvailable();
		}
	}

	protected int computeNumberCommandToSend(){
		int nb = 1;
		if(tinyGControllerService.getAvailableBuffer() > 4){
			nb = Math.max(1, tinyGControllerService.getAvailableBuffer() - 4);
		}
		return nb;
	}


	protected void waitLastCommandAcknowledgement(){
		while(pendingCommands > 0){
			synchronized ( ackMutex ) {
				try {
					ackMutex.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void waitPlannerBufferSpaceAvailable(){
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

	public void stop(){
		pendingCommands = 0;
	}
}
