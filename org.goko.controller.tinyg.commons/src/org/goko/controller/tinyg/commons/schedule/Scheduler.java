/**
 * 
 */
package org.goko.controller.tinyg.commons.schedule;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.event.MachineValueUpdateEvent;

/**
 * A scheduler used to monitor MachineValue on a controller and trigger action when expected state is met
 * 
 * @author Psyko
 * @date 11 janv. 2017
 */
public class Scheduler<T extends ITinyGControllerService>  {
	/** The controller service */
	private T controllerService;
	/** The expected machine value identifier*/
	private String machineValueId;
	/** The expected value */
	private Object expectedMachineValue;
	/** Internal - Time for timeout*/
	private Timer timer;
	/** The runnable to run */
	private Runnable runnable;
	
	/**
	 * Constructor
	 * @param controllerService the used controller service 
	 */
	public Scheduler(T controllerService){
		this.controllerService = controllerService;		
	}
	
	/**
	 * Creates default runnable to send the given data 
	 * @param data the data to send 
	 * @return scheduler
	 */
	public Scheduler<T> send(String data){
		runnable = new SendDataRunnable<ITinyGControllerService>(controllerService, data);
		return this;
	}
	
	/**
	 * Set the given runnable as the one to run 
	 * @param runnable the runnable to run
	 * @return scheduler
	 */
	public Scheduler<T> execute(Runnable runnable){
		this.runnable = runnable;
		return this;
	}
	
	/**
	 * Defines the expected value as the expected state 
	 * @param state the expected state 
	 * @return scheduler
	 */
	public Scheduler<T> whenState(MachineState state){
		return when(DefaultControllerValues.STATE, state);
	}
	
	/**
	 * Defines the id and value of the expected machine value that will trigger the action 
	 * @param id the id of the value
	 * @param value the expected value
	 * @return scheduler
	 */
	public Scheduler<T> when(String id, Object value){
		machineValueId = id;
		expectedMachineValue = value;
		return this;
	}
	
	/**
	 * Defines a timeout
	 * @param delay the delay 
	 * @param unit the unit of the delay
	 * @return scheduler
	 */
	public Scheduler<T> timeout(int delay, Unit<Time> unit){
		this.timer = new Timer();
		TimerTask action = new TimerTask() {
			
			@Override
			public void run() {
				onTimeout();				
			}
		};
		this.timer.schedule(action, Time.valueOf(delay, unit).value(TimeUnit.MILLISECOND).longValue());
		return this;
	}
	
	/**
	 * Timeout callback
	 */
	protected void onTimeout() {
		System.err.println("Timeout occured");
		if(timer != null){
			timer.cancel();
		}
		controllerService.removeListener(this);
	}

	/**
	 * Starts listening for machine value change
	 */
	public void begin(){
		try{
			MachineValue<?> currentValue = controllerService.getMachineValue(machineValueId, controllerService.getMachineValueType(machineValueId));
			if(currentValue != null && ObjectUtils.equals(currentValue.getValue(), expectedMachineValue)){
				executeRunnable();				
			}else{
				System.out.println("Adding listener");
				controllerService.addListener(this);
			}
		}catch(GkException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * MachineValueUpdate callback
	 * @param evt the event
	 */
	@EventListener(MachineValueUpdateEvent.class)
	public void onEvent(MachineValueUpdateEvent evt){
		System.out.println("void onEvent()");
		MachineValue<?> newMachineValue = evt.getTarget();
		if( StringUtils.equals(evt.getTarget().getIdDescriptor(), machineValueId) 
				&& ObjectUtils.equals(newMachineValue.getValue(), expectedMachineValue)){
			System.out.println("void onEvent() equals");
			executeRunnable();
		}
	}

	/**
	 * Executes the runnable 
	 */
	private void executeRunnable() {		
		if(timer != null){
			timer.cancel();
		}
		controllerService.removeListener(this);
		runnable.run();
	}
	
}
