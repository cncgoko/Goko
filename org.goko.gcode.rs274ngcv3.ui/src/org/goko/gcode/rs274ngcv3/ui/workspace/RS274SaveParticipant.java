package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.IMapperService;
import org.goko.core.workspace.service.IProjectSaveParticipant;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlRotateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlScaleModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlSegmentizeModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlTranslateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlWrapModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.source.XmlExternalFileGCodeSource;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.source.XmlGCodeProviderSource;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.source.XmlResourceLocationGCodeSource;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.source.XmlStringGCodeSource;

public class RS274SaveParticipant implements IProjectSaveParticipant<XmlRS274GContent> , IGokoService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274SaveParticipant.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.ui.workspace.RS274SaveParticipant";
	/** Type of the RS274 content container  */
	private static final String RS274_CONTENT_TYPE = "rs274Content";
	/** Name of the RS274 content file  */
	private static final String RS274_CONTENT_FILE_NAME = "rs274Content.xml";
	/** Workspace service */
	private IRS274NGCService gcodeService;
	/** Workspace service */
	private IWorkspaceService workspaceService;
	/** XML persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	private IMapperService mapperService;
	/** Dirty state of the content */
	private boolean dirty;

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
		xmlPersistenceService.register(XmlRS274GContent.class);		
		xmlPersistenceService.register(XmlExternalFileGCodeSource.class);		
		xmlPersistenceService.register(XmlResourceLocationGCodeSource.class);
		xmlPersistenceService.register(XmlStringGCodeSource.class);
		xmlPersistenceService.register(XmlSegmentizeModifier.class);		
		xmlPersistenceService.register(XmlTranslateModifier.class);		
		xmlPersistenceService.register(XmlWrapModifier.class);		
		xmlPersistenceService.register(XmlScaleModifier.class);		
		xmlPersistenceService.register(XmlRotateModifier.class);		
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
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#save(org.goko.core.workspace.io.IProjectOutputLocation)
	 */
	@Override
	public List<XmlProjectContainer> save(IProjectLocation output) throws GkException {
		List<XmlProjectContainer> containers = new ArrayList<XmlProjectContainer>();		
		XmlRS274GContent content = persistContent(/*new File(context.getResourcesFolder(), RS274_CONTENT_FILE_NAME)*/);
		containers.add(content);
		return containers;
	}
	
	private XmlRS274GContent persistContent() throws GkException{
		XmlRS274GContent content = new XmlRS274GContent();
		List<IGCodeProvider> lstProviders = gcodeService.getGCodeProvider();
		ArrayList<XmlGCodeProvider> lstXmlProvider = new ArrayList<XmlGCodeProvider>();

		for (IGCodeProvider igCodeProvider : lstProviders) {
			XmlGCodeProvider xmlProvider = new XmlGCodeProvider();	
			xmlProvider.setCode(igCodeProvider.getCode());
			xmlProvider.setSource(mapperService.export(igCodeProvider.getSource(), XmlGCodeProviderSource.class));
			// Persist the modifiers of this provider 
			persistModifiers(xmlProvider, igCodeProvider);
			
			lstXmlProvider.add(xmlProvider);
		}
		content.setLstGCodeProvider(lstXmlProvider);
		return content;
		
	}

	private void persistModifiers(XmlGCodeProvider xmlProvider, IGCodeProvider provider) throws GkException{
		List<IModifier<GCodeProvider>> lstModifiers = gcodeService.getModifierByGCodeProvider(provider.getId());
		ArrayList<XmlGCodeModifier> modifiers = null;
		
		if(CollectionUtils.isNotEmpty(lstModifiers)){
			modifiers = new ArrayList<>();
			for (IModifier<GCodeProvider> iModifier : lstModifiers) {
				XmlGCodeModifier modifier = mapperService.export(iModifier, XmlGCodeModifier.class);
				modifiers.add(modifier);
			}
		}		
		
		xmlProvider.setModifiers(modifiers);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#rollback()
	 */
	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#saveComplete()
	 */
	@Override
	public void saveComplete() {
		this.dirty = false;
	}

	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
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

}
