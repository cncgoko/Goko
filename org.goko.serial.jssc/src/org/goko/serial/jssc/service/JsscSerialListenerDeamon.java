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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class JsscSerialListenerDeamon implements Runnable{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSerialListenerDeamon.class);
	/** Outgoing queue */
	private BlockingQueue<Byte> queue;
	/** The service holding the serial port */
	private JsscSerialConnectionService jsscService;
	private boolean stopped;
	private Object emptyBufferLock = new Object();

	public JsscSerialListenerDeamon(JsscSerialConnectionService jsscService) {
		this.queue = new LinkedBlockingQueue<Byte>();
		this.jsscService = jsscService;
	}

	public void addAll(String buffer){
		synchronized (queue) {
			queue.addAll(GkUtils.toBytesList(buffer));
			synchronized (emptyBufferLock) {
				emptyBufferLock.notify();
			}
		}
	}
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(!stopped){
			waitDataInBuffer();
			List<Byte> notifiedBuffer = new ArrayList<Byte>();
			synchronized (queue) {
				queue.drainTo(notifiedBuffer);
				queue.clear();
			}
			try {
				jsscService.notifyInputListeners(notifiedBuffer);
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}
	public void stop(){
		stopped = true;
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
}
