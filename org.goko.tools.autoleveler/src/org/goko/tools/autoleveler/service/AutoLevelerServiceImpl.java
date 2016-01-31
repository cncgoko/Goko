package org.goko.tools.autoleveler.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class AutoLevelerServiceImpl implements IAutoLevelerService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AutoLevelerServiceImpl.class);
	/** Service Id */
	private static final String SERVICE_ID = "org.goko.autoleveler.service.AutoLevelerServiceImpl";
	/** Offset map builder cache */
	//private CacheById<IOffsetMapBuilder> cacheOffsetMapBuilders;

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting  "+getServiceId());
		//this.cacheOffsetMapBuilders = new CacheById<IOffsetMapBuilder>(new SequentialIdGenerator());
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping "+getServiceId());
	}
}
