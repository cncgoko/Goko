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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.log.GkLog;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Jssc Serial Service implementation
 *
 * @author PsyKo
 *
 */
public class JsscSerialConnectionService extends AbstractGokoService implements IJsscSerialConnectionService{
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
	private JsscSerialConnection currentConnection;
	
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
	public void startService() throws GkException {		
		this.inputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.outputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.connectionListeners = new ArrayList<WeakReference<IConnectionListener>>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {
		if(deamon != null){
			deamon.stop();
		}		
		this.inputListeners.clear();
		this.outputListeners.clear();
		this.connectionListeners.clear();
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#connect(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void connect(String portName, Integer baudrate, Integer databits, Integer stopBits, Integer parity, Integer flowControl) throws GkException {
		this.serialPort = new SerialPort(portName);

		try {
			this.serialPort.openPort();
			this.serialPort.setParams(baudrate, databits, stopBits, parity);
			this.serialPort.setFlowControlMode(flowControl);
			
			this.deamon = new JsscSerialListenerDeamon(this);
			this.serialPort.addEventListener(deamon, getEventMask());
			LOG.info("Connection successfully established on "+ portName +", baudrate: "+baudrate+", data bits: "+ databits +", parity: "+ parity +",stop bits: "+stopBits);
			currentConnection = new JsscSerialConnection(portName, baudrate, databits, stopBits, parity, flowControl);
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
	 * @see org.goko.core.connection.serial.ISerialConnectionService#disconnect()
	 */
	@Override
	public void disconnect() throws GkException {
		try {
			this.currentConnection = null;
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
			try {
				getSerialPort().purgePort(SerialPort.PURGE_TXCLEAR | SerialPort.PURGE_TXABORT); 
			} catch (SerialPortException e) {
				throw new GkTechnicalException(e);
			}
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

	protected void notifyInputListeners(List<Byte> data){
		if(CollectionUtils.isNotEmpty(inputListeners)){
			for(WeakReference<IConnectionDataListener> reference : inputListeners){
				if(reference.get() != null){
					try {
						reference.get().onDataReceived(data);
					} catch (GkException e) {
						LOG.error(e);
					} 
				}
			}
		}
	}

	protected void notifyOutputListeners(List<Byte> data){
		if(CollectionUtils.isNotEmpty(outputListeners)){
			for(WeakReference<IConnectionDataListener> reference : outputListeners){
				if(reference.get() != null){
					try {
						reference.get().onDataSent(data);
					} catch (GkException e) {
						LOG.error(e);
					}
				}
			}
		}
	}

	protected int getEventMask(){
        return SerialPort.MASK_RXCHAR + SerialPort.MASK_DSR + SerialPort.MASK_CTS + SerialPort.MASK_ERR + SerialPort.MASK_TXEMPTY;
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

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#getCurrentConnection()
	 */
	@Override
	public JsscSerialConnection getCurrentConnection() throws GkException {
		if(!isConnected()){
			throw new GkFunctionalException("Not connected to any serial device.");
		}
		return currentConnection;
	}
}
