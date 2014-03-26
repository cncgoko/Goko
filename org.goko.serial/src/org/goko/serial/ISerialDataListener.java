package org.goko.serial;

import java.util.EventListener;

import org.goko.core.common.exception.GkException;

/**
 * Listen for Serial data
 * @author PsyKo
 *
 */
public interface ISerialDataListener extends EventListener{
	/**
	 * Invoked when data are received
	 * @param data the received data
	 */
	void onDataReceived(byte[] data) throws GkException;

}
