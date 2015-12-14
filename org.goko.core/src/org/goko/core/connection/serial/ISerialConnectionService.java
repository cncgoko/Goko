/**
 * 
 */
package org.goko.core.connection.serial;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;

/**
 * Interface for serial connection service
 * 
 * @author PsyKo
 *
 */
public interface ISerialConnectionService extends IConnectionService {
	
	/**
	 * Connect using the given parameters 
	 * @param portName the port name
	 * @param baudrate the baud rate 
	 * @param databits the data bits
	 * @param stopBits the stop bits
	 * @param parity the parity 
	 * @param flowControl the flow control
	 * @throws GkException GkException
	 */
	void connect(String portName, Integer baudrate, Integer databits, Integer stopBits, Integer parity, Integer flowControl) throws GkException;
	
	ISerialConnection getCurrentConnection() throws GkException;
	
	/**
	 * Disconnect the current connection
	 * @throws GkException GkException
	 */
	void disconnect()  throws GkException;
	
	/**
	 * Return a list of available serial ports
	 * @return a list of port name
	 * @throws GkException GkException
	 */
	List<String> getAvailableSerialPort() throws GkException;
}
