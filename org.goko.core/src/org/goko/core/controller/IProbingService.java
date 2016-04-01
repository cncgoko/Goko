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
package org.goko.core.controller;

import java.util.List;
import java.util.concurrent.CompletionService;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;

/**
 * Interface defining a probing service
 * @author PsyKo
 *
 */
public interface IProbingService extends IGokoService {

	/**
	 * Initiate a probe sequence with the given parameters
	 * @param probeRequest the ProbeRequest
	 * @return a future Tuple6b
	 * @throws GkException GkException
	 */
	CompletionService<ProbeResult> probe(ProbeRequest probeRequest) throws GkException;
	
	/**
	 * Initiate a probe sequence with the given parameters
	 * @param probeRequest the ProbeRequest
	 * @return a future Tuple6b
	 * @throws GkException GkException
	 */
	CompletionService<ProbeResult> probe(List<ProbeRequest> probeRequest) throws GkException;
	
	/**
	 * Check if this probing service is ready to start a probe sequence. Throws an exception if not ready.
	 * @throws GkException exception if not ready
	 */
	void checkReadyToProbe() throws GkException;
	
	/**
	 * Check if this probing service is ready to start a probe sequence. 
	 * @return <code>true</code> if the service is ready for a probe sequence, <code>false</code> otherwise
	 */
	boolean isReadyToProbe();
}
