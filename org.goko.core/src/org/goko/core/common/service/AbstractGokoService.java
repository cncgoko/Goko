/**
 * 
 */
package org.goko.core.common.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 * @date 14 mars 2016
 */
public abstract class AbstractGokoService implements IGokoService {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(AbstractGokoService.class);
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public final void start() throws GkException {
		LOG.info("Starting service ["+getServiceId()+"] using implementation ["+getClass()+"]");
		startService();
		LOG.info("Service ["+getServiceId()+"] is running");
	}
	
	/**
	 * Internal start
	 * @throws GkException exception
	 */
	protected abstract void startService() throws GkException;
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public final void stop() throws GkException {
		LOG.info("Stopping service ["+getServiceId()+"]");
		stopService();
		LOG.info("Service ["+getServiceId()+"] stopped.");
	}
	
	/**
	 * Internal stop
	 * @throws GkException exception
	 */
	protected abstract void stopService() throws GkException;

}
