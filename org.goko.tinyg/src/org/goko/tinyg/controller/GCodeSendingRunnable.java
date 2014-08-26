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
import org.goko.core.gcode.bean.provider.GCodeExecutionQueue;
import org.goko.core.log.GkLog;

public class GCodeSendingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(GCodeSendingRunnable.class);
	private static final int BUFFER_AVAILABLE_REQUIRED_COUNT = 5;
	private GCodeExecutionQueue executionQueue;
	private TinyGControllerService tinyGControllerService;
	private Object ackMutex = new Object();
	private Object qrMutex = new Object();
	private int pendingCommands;

	public GCodeSendingRunnable(GCodeExecutionQueue status, TinyGControllerService controllerService) {
		this.executionQueue = status;
		this.tinyGControllerService = controllerService;
		pendingCommands = 0;

	}

	public void confirmCommand(){
		synchronized (ackMutex) {
			pendingCommands = Math.max(0,pendingCommands - 1);
			ackMutex.notify();
			//System.err.println("acknowledged "+pendingCommands);
		}
	}

	public void notifyBufferSpace() {
		synchronized (qrMutex) {
			qrMutex.notify();
			//System.err.println("acknowledged buffer planner with "+tinyGControllerService.getAvailableBuffer());

		}
	}

	@Override
	public void run() {
		while(executionQueue.hasNext()){
			try{
				int nbCommandToSend = computeNumberCommandToSend();

				ArrayList<GCodeCommand> lstCommand = new ArrayList<GCodeCommand>();
				while(executionQueue.hasNext() && nbCommandToSend > 0){
					GCodeCommand currentCommand = executionQueue.unstackNextCommand();
					LOG.info("     "+nbCommandToSend+" - "+ pendingCommands+", QR="+tinyGControllerService.getAvailableBuffer()+" - Sending a command : '"+currentCommand.toString()+"'");
					lstCommand.add(currentCommand);
					GokoEventBus.getInstance().post(new GCodeCommandSelectionEvent(currentCommand));
					nbCommandToSend--;
					currentCommand.setState(new GCodeCommandState(GCodeCommandState.SENT));
					executionQueue.setCommandState(currentCommand, GCodeCommandState.SENT);
					pendingCommands++;
				}
				if(CollectionUtils.isNotEmpty(lstCommand)){
					LOG.info("    Sending a group of "+lstCommand.size()+" command");
					tinyGControllerService.sendTogether(lstCommand);
				}

				waitLastCommandAcknowledgement();
				waitPlannerBufferSpaceAvailable();
			}catch(GkException e){
				LOG.error(e);
			}
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
