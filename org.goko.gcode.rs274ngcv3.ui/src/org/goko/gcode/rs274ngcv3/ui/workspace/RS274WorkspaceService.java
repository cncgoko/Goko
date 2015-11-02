/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.GCodeContainerUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class RS274WorkspaceService implements IRS274WorkspaceService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService";
	/** Workspace UI service */
	private IWorkspaceUIService workspaceUIService;
	/** Workspace UI service */
	private IRS274NGCService gcodeService;
	
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
		getWorkspaceUIService().addProjectContainerUiProvider(new GCodeContainerUiProvider(getGcodeService()));
		LOG.info("Successfully started "+getServiceId());
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
	
	
	
}
