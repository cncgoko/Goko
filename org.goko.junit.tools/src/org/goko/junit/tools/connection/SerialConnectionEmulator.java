package org.goko.junit.tools.connection;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.serial.ISerialConnectionService;
import org.goko.core.log.GkLog;

public class SerialConnectionEmulator extends ConnectionServiceAdapter implements ISerialConnectionService {
	private static final GkLog LOG = GkLog.getLogger(SerialConnectionEmulator.class);
	/** Connected state */
	private boolean connected;
	/** Output buffer */
	private List<List<Byte>> sentBuffer;
	/** End line character */
	private char endChararacter = '\n';
	private EmulatedSerialConnection currentConnection;
	private boolean debugOutputConsole;
	
	/**
	 * Constructor 
	 */
	public SerialConnectionEmulator() {
		sentBuffer = new ArrayList<List<Byte>>();
		currentConnection = new EmulatedSerialConnection();
	}
	
	/** {@inheritDoc}
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {		
		return "org.goko.junit.serialConnectionEmulator";
	}

	/** {@inheritDoc}
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		// nothing to do
		
	}

	/** {@inheritDoc}
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// nothing to do
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#connect(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void connect(String portName, Integer baudrate, Integer databits, Integer stopBits, Integer parity, Integer flowControl) throws GkException {
		connected = true;
		notifyConnectionListeners(EnumConnectionEvent.CONNECTED);
	}	

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#disconnect()
	 */
	@Override
	public void disconnect() throws GkException {
		connected = false;
		notifyConnectionListeners(EnumConnectionEvent.DISCONNECTED);	
	}

	/** {@inheritDoc}
	 * @see org.goko.core.connection.IConnectionService#isReady()
	 */
	@Override
	public boolean isReady() throws GkException {		
		return true;
	}

	/** {@inheritDoc}
	 * @see org.goko.core.connection.IConnectionService#isConnected()
	 */
	@Override
	public boolean isConnected() throws GkException {
		return connected;
	}

	/** {@inheritDoc}
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List, org.goko.core.connection.DataPriority)
	 */
	@Override
	public void send(List<Byte> data, DataPriority priority) throws GkException {
		sentBuffer.add(data);		
		if(debugOutputConsole){
			LOG.info("Out: ["+GkUtils.toString(data)+"]");
		}
		notifyOutputListeners(data);
	}

	/** {@inheritDoc}
	 * @see org.goko.core.connection.IConnectionService#send(java.util.List)
	 */
	@Override
	public void send(List<Byte> data) throws GkException {
		sentBuffer.add(data);
		notifyOutputListeners(data);	
		if(debugOutputConsole){
			LOG.info("Out: ["+GkUtils.toString(data)+"]");
		}
	}

	/** {@inheritDoc}
	 * @see org.goko.core.connection.IConnectionService#clearOutputBuffer()
	 */
	@Override
	public void clearOutputBuffer() throws GkException {
				
	}
	
	/**
	 * Remove the data in the sent buffer
	 */
	public void clearSentBuffer(){
		sentBuffer.clear();
	}
	/**
	 * Simulate the reception of the given data 
	 * @param data the data to receive 
	 * @throws GkException GkException
	 */
	public void receiveData(String data) throws GkException{
		notifyInputListeners(GkUtils.toBytesList(data)); 
	}
	/**
	 * Simulate the reception of the given data 
	 * @param data the data to receive 
	 * @throws GkException GkException
	 */
	public void receiveDataWithEndChar(String data) throws GkException{
		notifyInputListeners(GkUtils.toBytesList(data+endChararacter)); 
	}

	/**
	 * @return the endChararacter
	 */
	public char getEndChararacter() {
		return endChararacter;
	}

	/**
	 * @param endChararacter the endChararacter to set
	 */
	public void setEndChararacter(char endChararacter) {
		this.endChararacter = endChararacter;
	}

	/**
	 * @return the sentBuffer
	 */
	protected List<List<Byte>> getSentBuffer() {
		return sentBuffer;
	}

	/**
	 * @param sentBuffer the sentBuffer to set
	 */
	protected void setSentBuffer(List<List<Byte>> sentBuffer) {
		this.sentBuffer = sentBuffer;
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#getAvailableSerialPort()
	 */
	@Override
	public List<String> getAvailableSerialPort() throws GkException {		
		return new ArrayList<String>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.serial.ISerialConnectionService#getCurrentConnection()
	 */
	@Override
	public EmulatedSerialConnection getCurrentConnection() throws GkException {
		return currentConnection;
	}

	/**
	 * @param currentConnection the currentConnection to set
	 */
	public void setCurrentConnection(EmulatedSerialConnection currentConnection) {
		this.currentConnection = currentConnection;
	}

	/**
	 * @return the debugOutputConsole
	 */
	public boolean isDebugOutputConsole() {
		return debugOutputConsole;
	}

	/**
	 * @param debugOutputConsole the debugOutputConsole to set
	 */
	public void setDebugOutputConsole(boolean debugOutputConsole) {
		this.debugOutputConsole = debugOutputConsole;
	}

}
