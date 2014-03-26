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
