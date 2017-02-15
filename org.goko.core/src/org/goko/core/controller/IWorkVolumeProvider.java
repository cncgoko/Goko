package org.goko.core.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.listener.IWorkVolumeUpdateListener;
import org.goko.core.math.Tuple6b;

public interface IWorkVolumeProvider {
	/**
	 * Returns the name of this provider 
	 * @return String
	 */
	String getWorkVolumeProviderName();
	
	/**
	 * Register the given listener for update events
	 * @param listener the listener
	 */
	void addUpdateListener(IWorkVolumeUpdateListener listener);
		
	/**
	 * Unregister the given listener for update events
	 * @param listener the listener
	 */
	void removeUpdateListener(IWorkVolumeUpdateListener listener);
		
	/**
	 * The minimal point that the machine can reach
	 * Cannot return null; If no volume is defined, an exception is thrown
	 * @return the work volume minimal position
	 * @throws GkException GkException
	 */
	Tuple6b getWorkVolumeMinimalPosition() throws GkException;
	
	/**
	 * The minimal point that the machine can reach
	 * @return the work volume minimal position or <code>null</code> if no volume currently defined
	 * @throws GkException GkException  
	 */
	Tuple6b findWorkVolumeMinimalPosition() throws GkException;
	
	/**
	 * The maximal point that the machine can reach
	 * Cannot return null; If no volume is defined, an exception is thrown
	 * @return the work volume maximal position
	 * @throws GkException GkException	  
	 */
	Tuple6b getWorkVolumeMaximalPosition() throws GkException;
	
	/**
	 * The maximal point that the machine can reach
	 * @return the work volume maximal position or <code>null</code> if no volume currently defined
	 * @throws GkException GkException  
	 */
	Tuple6b findWorkVolumeMaximalPosition() throws GkException;
}
