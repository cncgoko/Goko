/**
 * 
 */
package org.goko.controller.grbl.commons;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.goko.core.common.exception.GkException;
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
		statusPollingTimer.cancel();
	}

	/**
	 * Starts the polling
	 */
	public void start() {
		statusPollingTimer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				try {
					grblControllerService.requestStatus();
				} catch (GkException e) {					
					stop();
					LOG.error(e);
				}
			}

		};
		statusPollingTimer.scheduleAtFixedRate(task, new Date(), 100);
	}
}
