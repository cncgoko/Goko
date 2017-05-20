/**
 * 
 */
package org.goko.controller.grbl.commons;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 9 avr. 2017
 */
public class GrblStatusPoller {
	private static final GkLog LOG = GkLog.getLogger(GrblStatusPoller.class);
	/** Status polling */
	private Timer statusPollingTimer;
	/** Target service*/
	private IGrblControllerService<?, ?> grblControllerService;
	/** The polling period*/
	private Time period = Time.valueOf(100, TimeUnit.MILLISECOND);
	/** Running state */
	private boolean running;
	
	/**
	 * Constructor
	 * @param grblControllerService the target service
	 */
	public GrblStatusPoller(IGrblControllerService<?, ?> grblControllerService) {
		super();
		this.grblControllerService = grblControllerService;
	}

	/**
	 * Stops the polling 
	 */
	public void stop(){
		if(statusPollingTimer != null){
			statusPollingTimer.cancel();
		}
		running = false;
	}

	/**
	 * Starts the polling
	 */
	public void start() {
		stop();
		statusPollingTimer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				try {
					running = true;
					grblControllerService.requestStatus();
				} catch (GkException e) {					
					stop();
					LOG.error(e);
				}
			}

		};		
		statusPollingTimer.scheduleAtFixedRate(task, new Date(), period.value(TimeUnit.MILLISECOND).longValue());
	}

	/**
	 * @return the period
	 */
	public Time getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Time period) {
		this.period = period;
		if(isRunning()){
			stop();
			start();
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
}
