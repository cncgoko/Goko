package org.goko.serial;

import gnu.io.CommPort;
import gnu.io.SerialPort;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;
import org.goko.serial.bean.SerialPortHandler;


/**
 * Serial connection service implementation
 *
 * @author PsyKo
 *
 */
public class SerialConnectionService implements IConnectionService, ISerialDataListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(SerialConnectionService.class);

	/** Service ID */
	private static final String SERVICE_ID = "Serial connection service";

	/**
	 * The list of currently opened serial ports
	 */
	SerialPortHandler serialPortHandler;
	/**
	 * The data listeners
	 */
	List<WeakReference<IConnectionDataListener>> inputListeners;
	List<WeakReference<IConnectionDataListener>> outputListeners;
	List<WeakReference<IConnectionListener>> connectionListeners;

	/** XON character */
	private static final byte XON = 0x11;
	private static final byte XOFF = 0x13;
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#connect(java.util.Map)
	 */
	@Override
	public void connect(Map<String, Object> param) throws GkException {
		// Let's make sure the Serial port is available
		String portName = SerialConnectionManager.checkPortName(param);
		Integer baudrate = SerialConnectionManager.checkBaudrate(param);

		if(!SerialConnectionManager.isSerialPortAvailable(portName)){
			throw new GkFunctionalException("Error while connecting : Port "+portName+" is currently owned by another application.");
		}

		serialPortHandler = SerialConnectionManager.openPort("Goko", portName, baudrate);
		serialPortHandler.addSerialDataListener(this);

		notifyConnectionListeners(EnumConnectionEvent.CONNECTED);
		LOG.info("Connection successfully established on "+ portName +", baudrate "+baudrate);
		List<Byte> lst = new ArrayList<Byte>();
		lst.add(XON);
		send(lst);
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#disconnect(java.util.Map)
	 */
	@Override
	public void disconnect(Map<String, Object> parameters) throws GkException {
		if(!isConnected()){
			throw new GkFunctionalException("Cannot disconnect Serial : not connected yet.");
		}

		CommPort commPort = serialPortHandler.getCommPort();
		try{
			if ( commPort instanceof SerialPort )			{
			    SerialPort serialPort = (SerialPort) commPort;
			    serialPortHandler.getInStream().close();
			    serialPortHandler.getOutStream().close();
			    serialPortHandler.clearOutputBuffer();


			    serialPort.close();
			    notifyConnectionListeners(EnumConnectionEvent.DISCONNECTED);
			}
		}catch(IOException e){
			throw new GkTechnicalException(e);
		}
	}

	public List<String> getAvailableSerialPorts() throws GkException{
		return SerialConnectionManager.getAvailableConnection();
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#isReady()
	 */
	@Override
	public boolean isReady() throws GkException {
		return isConnected();
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List, org.goko.core.connection.DataPriority)
	 */
	@Override
	public void send(List<Byte> data, DataPriority priority) throws GkException {
		if(!isConnected()){
			throw new GkFunctionalException("Serial service not connected...");
		}
		if(priority == DataPriority.IMPORTANT){
			serialPortHandler.sendDataImmediatly(data);
		}else{
			serialPortHandler.sendData(data);
		}
		notifyOutputListeners(data);
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List)
	 */
	@Override
	public void send(List<Byte> data) throws GkException {
		if(!isConnected()){
			throw new GkFunctionalException("Serial service not connected...");
		}
		serialPortHandler.sendData(data);
		notifyOutputListeners(data);
	}

	@Override
	public void clearOutputBuffer() throws GkException { // TODO Auto-generated method stub
		serialPortHandler.clearOutputBuffer();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		System.loadLibrary("rxtxSerial");
		inputListeners = new ArrayList<WeakReference<IConnectionDataListener>>();
		outputListeners = new ArrayList<WeakReference<IConnectionDataListener>>();
		connectionListeners = new ArrayList<WeakReference<IConnectionListener>>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#isConnected()
	 */
	@Override
	public boolean isConnected() throws GkException {
		return serialPortHandler != null && serialPortHandler.getInStream() != null;
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
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addInputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addInputDataListener(IConnectionDataListener listener) throws GkException {
		inputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.serial.ISerialDataListener#onDataReceived(byte[])
	 */
	@Override
	public void onDataReceived(byte[] data) throws GkException {
		List<Byte> listData = new ArrayList<Byte>();
		for(byte d : data){
			if(d == XOFF){
				serialPortHandler.setXOff();
				LOG.info(" Setting Xoff");
			}else if(d == XON){
				serialPortHandler.setXOn();
				LOG.info(" Setting Xon");
			}else{
				listData.add(d);
			}
		}
		notifyInputListeners(listData);
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addOutputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addOutputDataListener(IConnectionDataListener listener) throws GkException {
		outputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addConnectionListener(org.goko.core.connection.IConnectionListener)
	 */
	@Override
	public void addConnectionListener(IConnectionListener listener) throws GkException {
		this.connectionListeners.add(new WeakReference<IConnectionListener>(listener));
	}




}
