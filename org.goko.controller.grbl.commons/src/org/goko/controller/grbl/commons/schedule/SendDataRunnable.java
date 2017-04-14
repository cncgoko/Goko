/**
 * 
 */
package org.goko.controller.grbl.commons.schedule;

import org.goko.controller.grbl.commons.IGrblControllerService;
import org.goko.core.common.exception.GkException;

/**
 * Convenient runnable to send data with scheduler
 * 
 * @author Psyko
 * @date 12 janv. 2017
 */
public class SendDataRunnable<T extends IGrblControllerService<?, ?>> implements Runnable {
	/** The controller service */
	private T controllerService;
	/** The data to send */
	private String data;
	/** High priority flag */
	private boolean highPriority;
	
	/**
	 * Constructor 
	 */
	public SendDataRunnable(T controllerService, String data, boolean highPriority) {
		this.controllerService = controllerService;
		this.data = data;
		this.highPriority = highPriority;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if(highPriority){
				controllerService.getCommunicator().sendImmediately(data, true);
			}else{
				controllerService.getCommunicator().send(data, true);
			}
		} catch (GkException e) {			
			e.printStackTrace();
		}		
	}

}
