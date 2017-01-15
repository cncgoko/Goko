/**
 * 
 */
package org.goko.controller.tinyg.commons.schedule;

import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.core.common.exception.GkException;

/**
 * Convenient runnable to send data with scheduler
 * 
 * @author Psyko
 * @date 12 janv. 2017
 */
public class SendDataRunnable<T extends ITinyGControllerService> implements Runnable {
	/** The controller service */
	private T controllerService;
	/** The data to send */
	private String data;
	
	/**
	 * Constructor 
	 */
	public SendDataRunnable(T controllerService, String data) {
		this.controllerService = controllerService;
		this.data = data;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			controllerService.getCommunicator().send(data, true);
		} catch (GkException e) {			
			e.printStackTrace();
		}		
	}

}
