package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.service.AbstractProjectLoadParticipant;
import org.goko.core.workspace.service.IMapperService;
import org.goko.core.workspace.service.IProjectLoadParticipant;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.FileGCodeSourceExporter;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.SegmentizeModifierExporter;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.TranslateModifierExporter;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.FileGCodeSourceLoader;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.SegmentizeModifierLoader;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.TranslateModifierLoader;

public class RS274LoadParticipant extends AbstractProjectLoadParticipant<XmlRS274GContent> implements IGokoService, IProjectLoadParticipant {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274LoadParticipant.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.ui.workspace.RS274LoadParticipant";
	/** Type of the RS274 content container  */
	private static final String RS274_CONTENT_TYPE = "rs274Content";
	/** Load priority */
	private static final int LOAD_PRIORITY = 100;
	/** Workspace service */
	private IRS274NGCService gcodeService;
	/** XML persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	/** Mapper service */
	private IMapperService mapperService;

	/**
	 * Constructor
	 */
	public RS274LoadParticipant() {
		super(XmlRS274GContent.class);
	}
	
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
		mapperService.addLoader(new FileGCodeSourceLoader());
		mapperService.addLoader(new SegmentizeModifierLoader());
		mapperService.addLoader(new TranslateModifierLoader());		
		
		mapperService.addExporter(new FileGCodeSourceExporter());
		mapperService.addExporter(new TranslateModifierExporter());
		mapperService.addExporter(new SegmentizeModifierExporter());		
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping  "+getServiceId());
		LOG.info("Successfully stopped "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#getPriority()
	 */
	@Override
	public int getPriority() {		
		return LOAD_PRIORITY;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#clearContent()
	 */
	@Override
	public void clearContent() throws GkException {
		gcodeService.clearAll();

	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.goko.core.workspace.io.LoadContext, org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	protected void loadContainer(LoadContext context, XmlRS274GContent container, IProgressMonitor monitor) throws GkException {
		// Load the GCodeProvider
		List<XmlGCodeProvider> lstGCodeProvider = container.getLstGCodeProvider();
		
		SubMonitor submonitor = SubMonitor.convert(monitor, CollectionUtils.size(lstGCodeProvider));
		if(CollectionUtils.isNotEmpty(lstGCodeProvider)){
			for (XmlGCodeProvider xmlGCodeProvider : lstGCodeProvider) {
				IGCodeProviderSource source = mapperService.load(xmlGCodeProvider.getSource(), IGCodeProviderSource.class);
				IGCodeProvider provider = gcodeService.parse(source, submonitor);
				provider.setCode(xmlGCodeProvider.getCode());
				gcodeService.addGCodeProvider(provider);
				loadModifiers(provider, xmlGCodeProvider, submonitor);
				submonitor.worked(1);
			}
		}
		submonitor.done();
	}

	@SuppressWarnings("unchecked")
	protected void loadModifiers(IGCodeProvider provider, XmlGCodeProvider xmlGCodeProvider, IProgressMonitor monitor) throws GkException {
		if(CollectionUtils.isNotEmpty(xmlGCodeProvider.getModifiers())){			
			for (XmlGCodeModifier xmlGCodeModifier : xmlGCodeProvider.getModifiers()) {
				AbstractModifier<GCodeProvider> modifier = mapperService.load(xmlGCodeModifier, AbstractModifier.class);
				modifier.setRS274NGCService(gcodeService);
				modifier.setIdGCodeProvider(provider.getId());
				gcodeService.addModifier(modifier);
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#getContainerType()
	 */
	@Override
	public String getContainerType() {		
		return XmlRS274GContent.CONTAINER_TYPE;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#getContainerClass()
	 */
	@Override
	public Class<XmlRS274GContent> getContainerClass() {
		return XmlRS274GContent.class;
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
