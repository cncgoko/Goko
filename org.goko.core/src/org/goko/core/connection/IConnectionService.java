/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.connection;

import java.util.List;

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
	 * Remove a listener for incoming data
	 * @param listener the listener to remove
	 * @throws GkException an exception
	 */
	void removeInputDataListener(IConnectionDataListener listener) throws GkException;
	/**
	 * Register a listener for outgoing data
	 * @param listener the listener to register
	 * @throws GkException an exception
	 */
	void addOutputDataListener(IConnectionDataListener listener) throws GkException;
	/**
	 * Remove a listener for outgoing data
	 * @param listener the listener to remove
	 * @throws GkException an exception
	 */
	void removeOutputDataListener(IConnectionDataListener listener) throws GkException;
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
