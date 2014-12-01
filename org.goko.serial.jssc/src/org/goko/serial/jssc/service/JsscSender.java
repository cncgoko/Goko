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
package org.goko.serial.jssc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import jssc.SerialPortException;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class JsscSender implements Runnable {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSender.class);
	/** Outgoing queue */
	private ConcurrentLinkedDeque<Byte> queue;
	//private BlockingQueue<Byte> queue;
	/** Clear to send flag */
	private boolean clearToSend;
	/** The service holding the serial port */
	private JsscSerialConnectionService jsscService;
	/** Empty buffer lock */
	private Object emptyBufferLock;
	/** ock waiting for send permission */
	private Object clearToSendLock;
	private int importantBytes;
	private boolean stopped;

	/**
	 * Constructor
	 * @param serialPort the serial port to use
	 */
	public JsscSender(JsscSerialConnectionService jsscService) {
		this.queue = new ConcurrentLinkedDeque<Byte>();
		this.jsscService = jsscService;
		this.clearToSend = true;
		this.emptyBufferLock = new Object();
		this.clearToSendLock = new Object();

	}

	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public  void run() {
		List<Byte> lst = new ArrayList<Byte>();
		while(!stopped){
			waitDataInBuffer();
			waitClearToSend();
			if(isClearToSend() || importantBytes > 0){
				if(CollectionUtils.isNotEmpty(queue)){
					if(jsscService.getSerialPort().isOpened()){
						try {
							Byte b = queue.poll();
							jsscService.getSerialPort().writeByte( b );
							if(importantBytes > 0){
								importantBytes--;
							}
							lst.add(b);
							jsscService.notifyOutputListeners(lst);
							lst.clear();
						} catch (SerialPortException e) {
							LOG.error(e);
						} catch (GkException e) {
							LOG.error(e);
						}
					}
				}
			}
		}
	}


	/**
	 * @return the clearToSend
	 */
	protected boolean isClearToSend() {
		return clearToSend;
	}

	/**
	 * @param clearToSend the clearToSend to set
	 */
	protected void setClearToSend(boolean clearToSend) {
		this.clearToSend = clearToSend;
		synchronized(clearToSendLock){
			clearToSendLock.notify();
		}
	}

	/**
	 * Wait until there is data in the output queue
	 */
	private void waitDataInBuffer(){
		while(CollectionUtils.isEmpty(queue)){
			synchronized(emptyBufferLock){
				try {
					emptyBufferLock.wait();
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	/**
	 * Wait until the serial is clear to receive data or if there is important data to send
	 * Important data go through the Xon/Xoff control
	 */
	private void waitClearToSend(){
		while(!isClearToSend() && importantBytes <= 0){
			synchronized(clearToSendLock){
				try {
					clearToSendLock.wait();
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}
	}

	public void stop() {
		this.stopped = true;
	}

	public void start() {
		this.stopped = false;
	}
	/**
	 * Add Bytes to the output queue
	 * @param bytes the list of {@link Byte} to add
	 */
	protected void sendBytes(List<Byte> bytes){
		synchronized(queue){
			queue.addAll(bytes);
		}
		synchronized (emptyBufferLock) {
			emptyBufferLock.notify();
		}
	}

	/**
	 * Add Bytes to the output queue as important. They will be sent event if the ClearToSend flag is <code>false</code>
	 * @param bytes the list of {@link Byte} to add
	 */
	protected void sendBytesImmediately(List<Byte> bytes){
		if(CollectionUtils.isNotEmpty(bytes)){
			synchronized(queue){
				//System.err.println("sendBytesImmediately queue size before "+CollectionUtils.size(queue));
				for(int i = CollectionUtils.size(bytes) - 1 ; i >=0 ; i--){
					queue.addFirst(bytes.get(i));
				}

			//System.err.println("sendBytesImmediately queue size after "+CollectionUtils.size(queue));
			//System.err.println("Preparing to send "+CollectionUtils.size(bytes)+" important bytes");
			}
			importantBytes += CollectionUtils.size(bytes);
			synchronized (emptyBufferLock) {
			//	System.err.println("emptyBufferLock.notify();");
				emptyBufferLock.notify();
			}
			synchronized (clearToSendLock) {
			//	System.err.println("clearToSendLock.notify();");
				clearToSendLock.notify();
			}
		}
	}

	/**
	 * Empty the output queue
	 */
	protected void clearOutputBuffer(){
		queue.clear();
	}
}
