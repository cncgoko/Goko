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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.log.GkLog;

/**
 * Jssc Serial Service implementation
 *
 * @author PsyKo
 *
 */
public class JsscSerialConnectionService implements IJsscSerialConnectionService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSerialConnectionService.class);
	/** Service ID */
	private static final String SERVICE_ID = "JSSC Serial connection service";
	/** Current used SerialPort */
	private SerialPort serialPort;
	/** Incoming data listeners */
	private List<WeakReference<IConnectionDataListener>> inputListeners;
	/** Outgoing data listeners */
	private List<WeakReference<IConnectionDataListener>> outputListeners;
	/** Connection event listener */
	private List<WeakReference<IConnectionListener>> connectionListeners;
	/** Jssc Sender runnable*/
	private JsscSender jsscSender;
	private JsscSerialListenerDeamon deamon;
	private Thread deamonThread;
	private Thread senderThread;
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		this.inputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.outputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.connectionListeners = new ArrayList<WeakReference<IConnectionListener>>();


	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#connect(java.util.Map)
	 */
	@Override
	public void connect(Map<String, Object> parameters) throws GkException {
		String  portName  = JsscUtils.getParameterPortName(parameters);
		Integer baudrate = JsscUtils.getParameterBaudrate(parameters);
		Integer databits = JsscUtils.getParameterDatabits(parameters);
		Integer stopbits = JsscUtils.getParameterStopbits(parameters);
		Integer parity 	 = JsscUtils.getParameterParity(parameters);
		Integer flowControl	 = JsscUtils.getParameterFlowControl(parameters);

		this.serialPort = new SerialPort(portName);

		try {
			this.serialPort.openPort();
			this.serialPort.setParams(baudrate, databits, stopbits, parity);
			this.serialPort.setFlowControlMode(flowControl);
			this.deamon = new JsscSerialListenerDeamon(this);
			this.serialPort.addEventListener(deamon, getEventMask());
			LOG.info("Connection successfully established on "+ portName +", baudrate: "+baudrate+", data bits: "+ databits +", parity: "+ parity +",stop bits: "+stopbits);
			// Start the sender thread
			jsscSender = new JsscSender(this);
			deamonThread = new Thread(deamon);
			senderThread = new Thread(jsscSender);
			jsscSender.start();
			deamonThread.start();
			senderThread.start();
			notifyConnectionListeners(EnumConnectionEvent.CONNECTED);
		} catch (SerialPortException e) {
			throw new GkTechnicalException(e);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#disconnect(java.util.Map)
	 */
	@Override
	public void disconnect(Map<String, Object> parameters) throws GkException {
		try {
			if(serialPort != null){// && serialPort.isOpened()){
				serialPort.closePort();
			}
			deamon.stop();
			jsscSender.stop();
			notifyConnectionListeners(EnumConnectionEvent.DISCONNECTED);
		} catch (SerialPortException e) {
			throw new GkTechnicalException(e);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#isReady()
	 */
	@Override
	public boolean isReady() throws GkException {
		return this.serialPort != null && this.serialPort.isOpened();
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#isConnected()
	 */
	@Override
	public boolean isConnected() throws GkException {
		return this.serialPort != null && this.serialPort.isOpened();
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List, org.goko.core.connection.DataPriority)
	 */
	@Override
	public void send(List<Byte> data, DataPriority priority) throws GkException {
		if(isConnected()){
			if(priority == DataPriority.IMPORTANT){
				jsscSender.sendBytesImmediately(data);
			}else{
				send(data);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List)
	 */
	@Override
	public void send(List<Byte> data) throws GkException {
		if(!isConnected()){
			throw new GkFunctionalException("Not connected to any serial device.");
		}
		jsscSender.sendBytes(data);
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addInputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addInputDataListener(IConnectionDataListener listener) throws GkException {
		inputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#removeInputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void removeInputDataListener(IConnectionDataListener listener) throws GkException {
		WeakReference<IConnectionDataListener> reference = findListener(inputListeners, listener);
		if(reference != null){
			inputListeners.remove(reference);
		}

	}
	protected <T> WeakReference<T> findListener(List<WeakReference<T>> listenerList, T listener) throws GkException{
		WeakReference<T> reference = null;
		if(CollectionUtils.isNotEmpty(inputListeners)){
			for (WeakReference<T> weakReference : listenerList) {
				if(weakReference.get() == listener){
					reference = weakReference;
				}
			}
		}
		return reference;
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addOutputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addOutputDataListener(IConnectionDataListener listener) throws GkException {
		outputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#removeOutputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void removeOutputDataListener(IConnectionDataListener listener) throws GkException {
		WeakReference<IConnectionDataListener> reference = findListener(outputListeners, listener);
		if(reference != null){
			outputListeners.remove(reference);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addConnectionListener(org.goko.core.connection.IConnectionListener)
	 */
	@Override
	public void addConnectionListener(IConnectionListener listener) throws GkException {
		connectionListeners.add(new WeakReference<IConnectionListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#clearOutputBuffer()
	 */
	@Override
	public void clearOutputBuffer() throws GkException {
		if(isConnected()){
			jsscSender.clearOutputBuffer();
		}
	}

	protected void notifyConnectionListeners(EnumConnectionEvent event) throws GkException{
		if(CollectionUtils.isNotEmpty(connectionListeners)){
			for(WeakReference<IConnectionListener> reference : connectionListeners){
				if(reference.get() != null){
					reference.get().onConnectionEvent(event);
				}
			}
		}
	}

	protected void notifyInputListeners(List<Byte> data) throws GkException{
		if(CollectionUtils.isNotEmpty(inputListeners)){
			for(WeakReference<IConnectionDataListener> reference : inputListeners){
				if(reference.get() != null){
					reference.get().onDataReceived(data);
				}
			}
		}
	}

	protected void notifyOutputListeners(List<Byte> data) throws GkException{
		if(CollectionUtils.isNotEmpty(outputListeners)){
			for(WeakReference<IConnectionDataListener> reference : outputListeners){
				if(reference.get() != null){
					reference.get().onDataSent(data);
				}
			}
		}
	}

	protected int getEventMask(){
        return SerialPort.MASK_RXCHAR + SerialPort.MASK_DSR + SerialPort.MASK_CTS + SerialPort.MASK_ERR;
    }

	/**
	 * @return the serialPort
	 */
	protected SerialPort getSerialPort() {
		return serialPort;
	}


	@Override
	public List<String> getAvailableSerialPort() throws GkException {
		return Arrays.asList(SerialPortList.getPortNames());
	}
}
