/**
 * 
 */
package org.goko.controller.tinyg.commons.schedule;

import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.core.controller.schedule.Scheduler;

/**
 * A scheduler used to monitor MachineValue on a controller and trigger action when expected state is met
 * 
 * @author Psyko
 * @date 11 janv. 2017
 */
public class TinyGScheduler  extends Scheduler<TinyGScheduler, ITinyGControllerService<?>> {
	
	/**
	 * Constructor
	 * @param controllerService the used controller service 
	 */
	public TinyGScheduler(ITinyGControllerService<?> controllerService){
		super(controllerService);		
	}
	
	/**
	 * Creates default runnable to send the given data 
	 * @param data the data to send 
	 * @return scheduler
	 */
	public TinyGScheduler send(String data){
		setRunnable( new SendDataRunnable<ITinyGControllerService<?>>(getControllerService(), data, false) );
		return this;
	}
	
	/**
	 * Creates default runnable to send the given data with high priority
	 * @param data the data to send 
	 * @return scheduler
	 */
	public TinyGScheduler sendImmediately(String data){
		new SendDataRunnable<ITinyGControllerService<?>>(getControllerService(), data, true);
		return this;
	}
	
}
