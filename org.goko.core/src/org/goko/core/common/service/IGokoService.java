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
package org.goko.core.common.service;

import org.goko.core.common.exception.GkException;

/**
 * A generic interface for a Goko service 
 * 
 * @author PsyKo
 *
 */
public interface IGokoService {
	/**
	 * Returns the identifier of the service
	 * @return a string to identify the service
	 * @throws GkException an exception
	 */	
	String getServiceId() throws GkException;
	
	/**
	 * Service's start method
	 * @throws GkException an exception
	 */
	void start() throws GkException;
	/**
	 * Service's shutdown method
	 * @throws GkException an exception
	 */
	void stop() throws GkException; 
}
