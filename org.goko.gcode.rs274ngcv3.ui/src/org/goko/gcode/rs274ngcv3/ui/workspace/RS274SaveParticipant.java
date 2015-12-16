package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.source.FileGCodeSource;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.SaveContext;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.IProjectSaveParticipant;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.source.XmlFileGCodeSource;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

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
	//	workspaceService.addProjectSaveParticipant(this);
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
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#save(org.goko.core.workspace.io.SaveContext)
	 */
	@Override
	public List<XmlProjectContainer> save(SaveContext context) throws GkException {
		List<XmlProjectContainer> containers = new ArrayList<XmlProjectContainer>();

		XmlProjectContainer rs274Container = new XmlProjectContainer();
		rs274Container.setType(RS274_CONTENT_TYPE);
		rs274Container.setPath(context.getResourcePath(RS274_CONTENT_FILE_NAME));
		containers.add(rs274Container);
		persistContent(new File(context.getResourcesFolder(), RS274_CONTENT_FILE_NAME));
		return containers;
	}

	private void persistContent(File target) throws GkException{
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
		Serializer p = new Persister();
		try {
			p.write(content, target);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
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

}
