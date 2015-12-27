package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.IProjectLoadParticipant;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;

public class RS274LoadParticipant implements IGokoService, IProjectLoadParticipant<XmlRS274GContent> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274LoadParticipant.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.ui.workspace.RS274LoadParticipant";
	/** Type of the RS274 content container  */
	private static final String RS274_CONTENT_TYPE = "rs274Content";
	/** Workspace service */
	private IRS274NGCService gcodeService;
	/** XML persistence service */
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
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.goko.core.workspace.io.LoadContext, org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public void load(LoadContext context, XmlProjectContainer container) throws GkException {
		String filePath = container.getPath();
		File file = new File(context.getResourcesFolder().getParentFile(), filePath);

		XmlRS274GContent content = xmlPersistenceService.read(XmlRS274GContent.class, file);
		load(content);
	}

	/**
	 * Load the content of the RS274 services
	 * @param content the content to load
	 * @throws GkException GkException
	 */
	protected void load(XmlRS274GContent content) throws GkException {
		gcodeService.clearAll();

		// Load the GCodeProvider
		List<XmlGCodeProvider> lstGCodeProvider = content.getLstGCodeProvider();
		if(CollectionUtils.isNotEmpty(lstGCodeProvider)){
			for (XmlGCodeProvider xmlGCodeProvider : lstGCodeProvider) {
				IGCodeProvider provider = gcodeService.parse(xmlGCodeProvider.getSource().getSource(), null);
				provider.setCode(xmlGCodeProvider.getCode());
				gcodeService.addGCodeProvider(provider);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#canLoad(org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public boolean canLoad(XmlProjectContainer container) throws GkException {
		return StringUtils.equals(RS274_CONTENT_TYPE, container.getType());
	}

	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
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
