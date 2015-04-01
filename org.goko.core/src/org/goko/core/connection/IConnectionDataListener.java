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

/**
 * Interface for connection data listeners
 * 
 * @author PsyKo
 *
 */
public interface IConnectionDataListener {
	
	/**
	 * Callback method
	 * @param data the received data
	 * @throws GkException an exception
	 */
	void onDataReceived(List<Byte> data) throws GkException;
	
	/**
	 * Callback method
	 * @param data the sent data
	 * @throws GkException an exception
	 */
	void onDataSent(List<Byte> data) throws GkException;
}
