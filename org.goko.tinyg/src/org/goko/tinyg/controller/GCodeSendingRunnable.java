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
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.StreamStatus;
import org.goko.core.controller.bean.StreamStatusUpdate;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.log.GkLog;

public class GCodeSendingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(GCodeSendingRunnable.class);
	private static final int BUFFER_AVAILABLE_REQUIRED_COUNT = 5;
	private StreamStatus status;
	private TinyGControllerService tinyGControllerService;
	private Object ackMutex = new Object();
	private Object qrMutex = new Object();
	private int pendingCommands;

	public GCodeSendingRunnable(StreamStatus status, TinyGControllerService controllerService) {
		this.status = status;
		this.tinyGControllerService = controllerService;
		pendingCommands = 0;

	}

	public void ack(){
		//System.err.print("synchronized mutex { ");
		synchronized (ackMutex) {
			ackMutex.notify();
			Math.max(0,pendingCommands--);
			System.err.println("acknowledged "+pendingCommands);
		}
		//System.err.println(" }mutex");
	}

	public void ackBuffer() {
		//System.err.print("synchronized bufferMutex { ");
		synchronized (qrMutex) {
			qrMutex.notify();
			System.err.println("acknowledged buffer planner with "+tinyGControllerService.getAvailableBuffer());

		}
		//System.err.println(" }buffermutex");
	}

	@Override
	public void run() {
//		while(status.hasMoreCommands()){
//			synchronized (ackMutex) {
//				//if(controllerService.getAvailableBuffer() > BUFFER_AVAILABLE_REQUIRED_COUNT){
//				while(pendingCommands <= 0 && tinyGControllerService.getAvailableBuffer() > BUFFER_AVAILABLE_REQUIRED_COUNT){
//					GCodeCommand currentCommand = status.consumeNextCommand();
//					if(currentCommand != null && CollectionUtils.isNotEmpty(currentCommand.getGCodeWords())){
//						try {
//							System.err.println("PC = "+pendingCommands+"  QR ="+tinyGControllerService.getAvailableBuffer()+" --- sending a command : '"+currentCommand.toString()+"'");
//
//							tinyGControllerService.sendCommand(currentCommand);
//							status.addSentCommand(currentCommand);
//							pendingCommands++;
//							System.err.println("Attente mutex");
//							ackMutex.wait();
//							qrMutex.wait(20);
//							System.err.println("Mutex passé");
//						} catch (GkException e) {
//							LOG.error(e);
//						}catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//			}
//			synchronized (qrMutex) {
//				try {
//					System.err.println("Wait sur le mutex buffer");
//					qrMutex.wait(50);
//					System.err.println("Wait sur le mutex buffer passé");
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
		while(status.hasMoreCommands()){
			try{
				int nbCommandToSend = computeNumberCommandToSend();
				LOG.info("******* Preparing to send "+nbCommandToSend+" commands");
				int i = nbCommandToSend;
				ArrayList<GCodeCommand> lstCommand = new ArrayList<GCodeCommand>();
				while(status.hasMoreCommands() && nbCommandToSend > 0){
					GCodeCommand currentCommand = status.consumeNextCommand();
					LOG.info("     "+nbCommandToSend+"/"+i+" - "+ pendingCommands+", QR="+tinyGControllerService.getAvailableBuffer()+" - Sending a command : '"+currentCommand.toString()+"'");
					lstCommand.add(currentCommand);
					//sendCommand(currentCommand);
					nbCommandToSend--;
					status.addSentCommand(currentCommand);
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
		if(tinyGControllerService.getAvailableBuffer() > 10){
			nb = Math.max(0, tinyGControllerService.getAvailableBuffer() - 4);
		}
		return nb;
	}

	public void clearStreamingStatus() {
		status.getGCodeCommandToSend().clear();
	}

	protected void sendCommand(GCodeCommand command){
		if(command != null && CollectionUtils.isNotEmpty(command.getGCodeWords())){
			try {
				tinyGControllerService.sendCommand(command);
				tinyGControllerService.notifyListeners(new StreamStatusUpdate(status));
				status.addSentCommand(command);
				command.setState(new GCodeCommandState(GCodeCommandState.SENT));
				pendingCommands++;
			} catch (GkException e) {
				LOG.error(e);
			}
		}
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
		while(tinyGControllerService.getAvailableBuffer() < BUFFER_AVAILABLE_REQUIRED_COUNT){
			synchronized ( qrMutex ) {
				try {
					qrMutex.wait(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
