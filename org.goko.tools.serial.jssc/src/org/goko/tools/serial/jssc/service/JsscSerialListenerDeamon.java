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

import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class JsscSerialListenerDeamon implements Runnable, SerialPortEventListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSerialListenerDeamon.class);
	/** Outgoing queue */
	private BlockingQueue<List<Byte>> queue;
	/** The service holding the serial port */
	private JsscSerialConnectionService jsscService;
	/** Stop indicator */
	private boolean stopped;

	public JsscSerialListenerDeamon(JsscSerialConnectionService jsscService) {
		this.queue = new LinkedBlockingQueue<List<Byte>>();
		this.jsscService = jsscService;
	}

	public void addAll(String buffer){
		queue.add(GkUtils.toBytesList(buffer));
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0){ // Data available
			int dataAvailableCount = serialPortEvent.getEventValue();
			try {
				byte buffer[] = jsscService.getSerialPort().readBytes(dataAvailableCount);
				if(buffer != null && buffer.length > 0){
					queue.add( GkUtils.toBytesList(buffer));
				}
			} catch (SerialPortException e) {
				LOG.error(e);
			}
		}else if(serialPortEvent.isERR()){
			LOG.error(serialPortEvent.toString() + " error : " +serialPortEvent.getEventValue());
			try {
				jsscService.disconnect();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		
	}

	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<Byte> notifiedBuffer = new ArrayList<Byte>();
		while(!stopped){
			try {
				notifiedBuffer = queue.take();
				jsscService.notifyInputListeners(notifiedBuffer);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
		}
	}
	public void stop(){
		stopped = true;
	}
}
