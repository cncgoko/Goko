/**
 * 
 */
package org.goko.controller.grbl.commons.schedule;

import org.goko.controller.grbl.commons.IGrblControllerService;
import org.goko.core.controller.schedule.Scheduler;

/**
 * A scheduler used to monitor MachineValue on a controller and trigger action when expected state is met
 * 
 * @author Psyko
 * @date 11 janv. 2017
 */
public class GrblScheduler extends Scheduler<GrblScheduler, IGrblControllerService<?, ?>> {
	
	/**
	 * Constructor
	 * @param controllerService the used controller service 
	 */
	public GrblScheduler(IGrblControllerService<?, ?> controllerService){
		super(controllerService);		
	}

	/**
	 * Creates default runnable to send the given data 
	 * @param data the data to send 
	 * @return scheduler
	 */
	public GrblScheduler send(String data){
		setRunnable( new SendDataRunnable<IGrblControllerService<?, ?>>(getControllerService(), data, false) );
		return this;
	}
	
	/**
	 * Creates default runnable to send the given data with high priority
	 * @param data the data to send 
	 * @return scheduler
	 */
	public GrblScheduler sendImmediately(String data){
		new SendDataRunnable<IGrblControllerService<?, ?>>(getControllerService(), data, true);
		return this;
	}
}
