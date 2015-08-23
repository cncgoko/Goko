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
package org.goko.tools.serial.jssc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

import jssc.SerialPortException;

public class JsscSender implements Runnable {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSender.class);
	private BlockingQueue<List<Byte>> queue;
	/** Important datas */
	private BlockingQueue<List<Byte>> importantQueue;
	/** The service holding the serial port */
	private JsscSerialConnectionService jsscService;
	/** Lock waiting for send permission */
	private Object clearToSendLock;
	private boolean stopped;

	/**
	 * Constructor
	 * @param serialPort the serial port to use
	 */
	public JsscSender(JsscSerialConnectionService jsscService) {

		this.queue = new LinkedBlockingQueue<List<Byte>>();
		this.importantQueue = new LinkedBlockingQueue<List<Byte>>();
		this.jsscService = jsscService;

		this.clearToSendLock = new Object();

	}

	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public  void run() {

		while(!stopped){
			if(jsscService.getSerialPort().isOpened()){
				List<Byte> lst = null;
				try {
					if(CollectionUtils.isNotEmpty(queue)){
						lst = queue.take();
					}else if(CollectionUtils.isNotEmpty(importantQueue)){
						lst = importantQueue.take();
					}
				}catch (InterruptedException e) {
					LOG.error(e);
				}

				if(lst != null){
					try {
						jsscService.getSerialPort().writeString( GkUtils.toString(lst) );
						jsscService.notifyOutputListeners(lst);
					} catch (SerialPortException e) {
						LOG.error(e);
					} catch (GkException e) {
						LOG.error(e);
					}
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
		queue.add(new ArrayList<Byte>(bytes));
	}

	/**
	 * Add Bytes to the output queue as important. They will be sent event if the ClearToSend flag is <code>false</code>
	 * @param bytes the list of {@link Byte} to add
	 */
	protected void sendBytesImmediately(List<Byte> bytes){
		importantQueue.add( new ArrayList<Byte>(bytes) );
		synchronized (clearToSendLock) {
			clearToSendLock.notify();
		}
	}

	/**
	 * Empty the output queue
	 */
	protected void clearOutputBuffer(){
		queue.clear();
	}
}
