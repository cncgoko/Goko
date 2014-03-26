package org.goko.core.connection;

import java.util.List;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;


/**
 * Interface definition for the connection.
 * The connection is in charge of transmitting the order to the machine
 *
 * @author PsyKo
 */
public interface IConnectionService extends IGokoService{
	/**
	 * Connect using the given parameters
	 * @param parameters the connection parameters
	 * @throws GkException GkException
	 */
	void connect(Map<String, Object> parameters) throws GkException;
	/**
	 * Disconnect using the given parameters
	 * @param parameters the connection parameters
	 * @throws GkException GkException
	 */
	void disconnect(Map<String, Object> parameters) throws GkException;
	/**
	 * Determines if the service is ready to send data
	 * @return <code>true</code> if the service is ready, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isReady() throws GkException;
	/**
	 * Determines if the service is connected
	 * @return <code>true</code> if the service is connected, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isConnected() throws GkException;
	/**
	 * Sends the data using the given priority.
	 * @param data the data to send
	 * @param priority the priority level
	 * @throws GkException an exception
	 */
	void send(List<Byte> data, DataPriority priority) throws GkException;

	/**
	 * Sends the data using the Normal priority.
	 * @param data the data to send
	 * @throws GkException an exception
	 */
	void send(List<Byte> data) throws GkException;

	/**
	 * Register a listener for incoming data
	 * @param listener the listener to register
	 * @throws GkException an exception
	 */
	void addInputDataListener(IConnectionDataListener listener) throws GkException;

	/**
	 * Register a listener for outgoing data
	 * @param listener the listener to register
	 * @throws GkException an exception
	 */
	void addOutputDataListener(IConnectionDataListener listener) throws GkException;

	/**
	 * Register a listener for connection events
	 * @param listener the listener to register
	 * @throws GkException an exception
	 */
	void addConnectionListener(IConnectionListener listener) throws GkException;

	/**
	 * Clear the buffer of outgoing data
	 * @throws GkException GkException
	 */
	void clearOutputBuffer() throws GkException;
}
