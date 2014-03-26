package org.goko.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.serial.bean.SerialConnectionParameter;
import org.goko.serial.bean.SerialPortHandler;

/**
 * Serial connection management class
 *
 * @author PsyKo
 *
 */
public class SerialConnectionManager {

	/**
	 * Open the specified port
	 * @param portName the name of the port
	 * @param baud the desired baud rate
	 * @return
	 * @throws GkException GkException
	 */
	protected static SerialPortHandler openPort(String owner, String portName, int baud) throws GkException{
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

			CommPort commPort = portIdentifier.open(owner ,2000);

			if ( commPort instanceof SerialPort )
			{
			    SerialPort serialPort = (SerialPort) commPort;
			    serialPort.setSerialPortParams(baud,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			    serialPort.setFlowControlMode( SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT | SerialPort.FLOWCONTROL_XONXOFF_OUT | SerialPort.FLOWCONTROL_XONXOFF_IN );
			    serialPort.setRTS(true);
			    InputStream in = serialPort.getInputStream();
			    OutputStream out = serialPort.getOutputStream();

			    SerialPortHandler pHandler = new SerialPortHandler(portName, in, out);
			    pHandler.setCommPort(commPort);
			    serialPort.setOutputBufferSize(2048);
			    serialPort.setInputBufferSize(2048);
			    serialPort.addEventListener(pHandler);
                serialPort.notifyOnDataAvailable(true);

			    pHandler.start();
			    return pHandler;
			}else {
			   throw new GkFunctionalException(" Only serial ports are handled.");
			}
		} catch (NoSuchPortException e) {
			throw new GkFunctionalException("The Serial port '"+portName+"' does not exist.");
		} catch (PortInUseException e) {
			throw new GkFunctionalException("The Serial port '"+portName+"' is already in use.");
		} catch (UnsupportedCommOperationException e) {
			throw new GkFunctionalException("Serial port '"+portName+"' : Unsupported comm operation.");
		} catch (IOException e) {
			throw new GkFunctionalException("Serial port '"+portName+"' : unable to get input/output stream.");
		} catch (TooManyListenersException e) {
			throw new GkFunctionalException("Serial port '"+portName+"' : unable to add more than 1 listener.");
		}
	}

	/**
	 * Disconnect from the given connection
	 * @param pHandler the handler of the connection
	 * @throws GkException GkException
	 */
	public static void disconnect(SerialPortHandler pHandler) throws GkException {
		CommPort commPort = pHandler.getCommPort();
		try{
			if ( commPort instanceof SerialPort )			{
			    SerialPort serialPort = (SerialPort) commPort;
			    serialPort.removeEventListener();
			    pHandler.getInStream().close();
			    pHandler.getOutStream().close();
			    serialPort.close();
			}
		}catch(IOException e){
			throw new GkTechnicalException(e);
		}
	}

	/**
	 * Check the availability of a port
	 * @param portName the name of the port
	 * @return <code>true</code> if the port is available, <code>false</code> otherwise
	 * @throws AbstractException exception
	 */
	protected static boolean isSerialPortAvailable(String portName) throws GkException{
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
			if(portId.isCurrentlyOwned()){
				return false;
			}
		} catch (NoSuchPortException e) {
			throw new GkFunctionalException(e.getLocalizedMessage());
		}
		return true;
	}

	/**
	 * Check the validity of the port name parameters
	 * @param param the connection parameters
	 * @throws GkException GkException
	 */
	protected static String checkPortName(Map<String,Object> param) throws GkException{
		if(param == null){
			throw new GkFunctionalException("Error while connecting : no parameters");
		}
		Object portNameObj = param.get(SerialConnectionParameter.PORTNAME.toString());
		if(portNameObj == null ||
				portNameObj != null && !(portNameObj instanceof String)	){
			throw new GkFunctionalException("Error while connecting : Port name is not set");
		}
		String portName = (String) portNameObj;
		if(StringUtils.isBlank(portName)){
			throw new GkFunctionalException("Error while connecting : parameter PortName is not valid.");
		}
		return portName;
	}
	/**
	 * Check the validity of the baud rate parameters
	 * @param param the connection parameters
	 * @throws GkException GkException
	 */
	protected static Integer checkBaudrate(Map<String,Object> param) throws GkException{
		if(param == null){
			throw new GkFunctionalException("Error while connecting : no parameters");
		}
		Object baudRateObj = param.get(SerialConnectionParameter.BAUDRATE.toString());
		if(baudRateObj == null ||
				baudRateObj != null && !(baudRateObj instanceof Integer)	){
			throw new GkFunctionalException("Error while connecting : Baudrate is not set or is not an integer");
		}
		Integer baudrate = (Integer) baudRateObj;

		return baudrate;
	}
	/**
	 * Check the if the requested connection is available
	 * @param param the connection parameters
	 * @return <code>true</code> if the connection is available, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	protected boolean isConnectionAvailable(Map<String, Object> param) throws GkException {
		// Checking port name
		Object portNameObj = param.get(SerialConnectionParameter.PORTNAME.toString());
		if(portNameObj == null || portNameObj != null && !(portNameObj instanceof String)	){
			return false;
		}
		String portName = (String) portNameObj;
		if(StringUtils.isBlank(portName)){
			return false;
		}
		// Checking baudrate
		Object baudRateObj = param.get(SerialConnectionParameter.BAUDRATE.toString());
		if(baudRateObj == null || baudRateObj != null && !(baudRateObj instanceof Integer)	){
			return false;
		}

		return isSerialPortAvailable(portName);
	}

	/**
	 * Returns the name of the available connections
	 * @return the name of the available ports
	 * @throws GkException GkException
	 */
	@SuppressWarnings("rawtypes")
	protected static  List<String> getAvailableConnection() throws GkException {
		List<String> ports = new ArrayList<>();

		Enumeration portsEnum = CommPortIdentifier.getPortIdentifiers();
		while(portsEnum.hasMoreElements()){
			CommPortIdentifier cpi = (CommPortIdentifier)portsEnum.nextElement();
			ports.add(cpi.getName());
		}
		return ports;
	}
}
