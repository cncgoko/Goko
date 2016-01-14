package org.goko.autoleveler.service;

import java.util.List;

import org.goko.autoleveler.bean.IHeightMapBuilder;
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

	/** (inheritDoc)
	 * @see org.goko.autoleveler.service.IAutoLevelerService#getOffsetMapBuilder()
	 */
	@Override
	public List<IHeightMapBuilder> getOffsetMapBuilder() throws GkException {
		return null;//cacheOffsetMapBuilders.get();
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.service.IAutoLevelerService#getOffsetMapBuilder(java.lang.Integer)
	 */
	@Override
	public IHeightMapBuilder getOffsetMapBuilder(Integer id) throws GkException {
		return null;//cacheOffsetMapBuilders.get(id);
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.service.IAutoLevelerService#addOffsetMapBuilder(org.goko.autoleveler.bean.IHeightMapBuilder)
	 */
	@Override
	public void addOffsetMapBuilder(IHeightMapBuilder builder) throws GkException {
	//	cacheOffsetMapBuilders.add(builder);
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.service.IAutoLevelerService#deleteOffsetMapBuilder(org.goko.autoleveler.bean.IHeightMapBuilder)
	 */
	@Override
	public void deleteOffsetMapBuilder(IHeightMapBuilder builder) throws GkException {
	//	cacheOffsetMapBuilders.remove(builder.getId());
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.service.IAutoLevelerService#deleteOffsetMapBuilder(java.lang.Integer)
	 */
	@Override
	public void deleteOffsetMapBuilder(Integer idBuilder) throws GkException {
	//	cacheOffsetMapBuilders.remove(idBuilder);
	}
}
