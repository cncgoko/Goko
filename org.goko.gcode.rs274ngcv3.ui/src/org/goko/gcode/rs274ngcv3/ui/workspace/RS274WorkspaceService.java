/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.source.FileGCodeSource;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.AbstractProjectLoadParticipant;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.source.XmlFileGCodeSource;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.TestModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.TranslateModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.GCodeContainerUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
/**
 * @author Psyko
 *
 */
public class RS274WorkspaceService extends AbstractProjectLoadParticipant<XmlRS274GContent> implements IRS274WorkspaceService, IGCodeProviderRepositoryListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService";
	/** Workspace UI service */
	private IWorkspaceUIService workspaceUIService;
	/** Workspace service */
	private IWorkspaceService workspaceService;
	/** Workspace UI service */
	private IExecutionService<?,?> executionService;
	/** Workspace UI service */
	private IRS274NGCService gcodeService;
	/** The list of existing IModifierUiProvider*/
	List<IModifierUiProvider<GCodeProvider, ?>> lstModifierUiProvider;
	/** Dirty state */
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
		// Create the RS274 project container
		getWorkspaceUIService().addProjectContainerUiProvider(new GCodeContainerUiProvider(getGcodeService(), this, executionService));
		getGcodeService().addListener(this);
		lstModifierUiProvider = new ArrayList<IModifierUiProvider<GCodeProvider, ?>>();
		workspaceService.addProjectSaveParticipant(this);
		workspaceService.addProjectLoadParticipant(this);
		initModifierUiProvider();
		LOG.info("Successfully started "+getServiceId());
	}

	/**
	 * Initialization of the default modifiers
	 * @throws GkException GkException
	 */
	private void initModifierUiProvider() throws GkException {
		addModifierBuilder(new TestModifierBuilder());
		addModifierBuilder(new TranslateModifierBuilder());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/**
	 * @return the workspaceUiService
	 */
	public IWorkspaceUIService getWorkspaceUIService() {
		return workspaceUIService;
	}

	/**
	 * @param workspaceUiService the workspaceUiService to set
	 */
	public void setWorkspaceUIService(IWorkspaceUIService workspaceUiService) {
		this.workspaceUIService = workspaceUiService;
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

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService#getModifierBuilder()
	 */
	@Override
	public List<IModifierUiProvider<GCodeProvider, ?>> getModifierBuilder() throws GkException{
		return lstModifierUiProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService#addModifierBuilder(org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider)
	 */
	@Override
	public void addModifierBuilder(IModifierUiProvider<GCodeProvider, ?> modifierBuilder) throws GkException {
		lstModifierUiProvider.add(modifierBuilder);
	}
	/**
	 * @return the executionService
	 */
	public IExecutionService<?, ?> getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService<?, ?> executionService) {
		this.executionService = executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		dirty = true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		dirty = true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		dirty = true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#save(org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public XmlRS274GContent save() throws GkException {
		XmlRS274GContent content = new XmlRS274GContent();
		List<IGCodeProvider> lstProviders = gcodeService.getGCodeProvider();
		ArrayList<XmlGCodeProvider> lstXmlProvider = new ArrayList<XmlGCodeProvider>();

		for (IGCodeProvider igCodeProvider : lstProviders) {
			XmlGCodeProvider xmlProvider = new XmlGCodeProvider();
			xmlProvider.setCode(igCodeProvider.getCode());
			FileGCodeSource s = (FileGCodeSource) igCodeProvider.getSource();
			XmlFileGCodeSource xmlFileSource = new XmlFileGCodeSource();
			xmlFileSource.setPath(s.getFile().getAbsolutePath());
			xmlProvider.setSource(xmlFileSource);
			lstXmlProvider.add(xmlProvider);
		}
		content.setLstGCodeProvider(lstXmlProvider);

		return content;
	}

	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#getProjectContainerType()
	 */
	@Override
	public String getProjectContainerType(){
		return "XmlRS274GContent";
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

//	/** (inheritDoc)
//	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(java.lang.Object)
//	 */
//	@Override
//	public void load(InputNode input) throws GkException {
//		XmlNodeConverter<XmlRS274GContent> converter = new XmlNodeConverter<XmlRS274GContent>(XmlRS274GContent.class);
//		XmlRS274GContent result = converter.load(input);
//		
//		ArrayList<XmlGCodeProvider> lstGcode = result.getLstGCodeProvider();
//		for (XmlGCodeProvider xmlGCodeProvider : lstGcode) {
//			
//		}
//		
//		System.out.println("public void load(XmlRS274GContent content) throws GkException ");
//	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#newContentInstance()
	 */
	@Override
	public XmlRS274GContent newContentInstance() {
		return new XmlRS274GContent();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.AbstractProjectLoadParticipant#getProjectContainerContentClass()
	 */
	@Override
	protected Class<XmlRS274GContent> getProjectContainerContentClass() {
		return XmlRS274GContent.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.AbstractProjectLoadParticipant#loadContent(java.lang.Object)
	 */
	@Override
	protected void loadContent(XmlRS274GContent content) throws GkException {
		
	}

}
