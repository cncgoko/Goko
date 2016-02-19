package org.goko.tools.autoleveler.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.autoleveler.io.xml.AbstractXmlHeightMap;
import org.goko.tools.autoleveler.io.xml.GridAutoLevelerModifierExporter;
import org.goko.tools.autoleveler.io.xml.GridAutoLevelerModifierLoader;
import org.goko.tools.autoleveler.io.xml.GridHeightMapExporter;
import org.goko.tools.autoleveler.io.xml.GridHeightMapLoader;
import org.goko.tools.autoleveler.io.xml.XmlGridAutoLevelerModifier;
import org.goko.tools.autoleveler.io.xml.XmlGridHeightMap;

public class AutoLevelerServiceImpl implements IAutoLevelerService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AutoLevelerServiceImpl.class);
	/** Service Id */
	private static final String SERVICE_ID = "org.goko.autoleveler.service.AutoLevelerServiceImpl";
	private IMapperService mapperService;
	private IXmlPersistenceService xmlPersistenceService;
	
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
		mapperService.addExporter(new GridHeightMapExporter());
		mapperService.addExporter(new GridAutoLevelerModifierExporter());
		mapperService.addLoader(new GridAutoLevelerModifierLoader());
		mapperService.addLoader(new GridHeightMapLoader());
		
		xmlPersistenceService.register(XmlGridAutoLevelerModifier.class);
		xmlPersistenceService.register(XmlGridHeightMap.class);
		xmlPersistenceService.register(AbstractXmlHeightMap.class);
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping "+getServiceId());
	}

	/**
	 * @return the mapperService
	 */
	public IMapperService getMapperService() {
		return mapperService;
	}

	/**
	 * @param mapperService the mapperService to set
	 */
	public void setMapperService(IMapperService mapperService) {
		this.mapperService = mapperService;
	}

	/**
	 * @return the xmlPersistenceService
	 */
	public IXmlPersistenceService getXmlPersistenceService() {
		return xmlPersistenceService;
	}

	/**
	 * @param xmlPersistenceService the xmlPersistenceService to set
	 */
	public void setXmlPersistenceService(IXmlPersistenceService xmlPersistenceService) {
		this.xmlPersistenceService = xmlPersistenceService;
	}
}
