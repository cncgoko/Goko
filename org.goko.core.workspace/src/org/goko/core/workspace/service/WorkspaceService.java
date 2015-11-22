/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.core.workspace.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.GkProject;
import org.goko.core.workspace.bean.ProjectContainer;

/**
 * Default implementation of the workspace service
 *
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 */
public class WorkspaceService implements IWorkspaceService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.core.workspace.WorkspaceService";
	/** The list of listener */
	private List<IWorkspaceListener> listenerList;
	// Temporary project storage
	private GkProject project;
	
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
		this.listenerList = new ArrayList<IWorkspaceListener>();		
		this.project = new GkProject();
		this.project.addProjectContainer(new ProjectContainer("TEST"));
		LOG.info("Successfully started : "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#addWorkspaceListener(IWorkspaceListener)
	 */
	@Override
	public void addWorkspaceListener(IWorkspaceListener listener) throws GkException {
		this.listenerList.add(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceService#removeWorkspaceListener(org.goko.core.workspace.service.IWorkspaceListener)
	 */
	@Override
	public void removeWorkspaceListener(IWorkspaceListener listener) throws GkException {
		this.listenerList.remove(listener);
	}

	public void notifyWorkspaceEvent(IWorkspaceEvent event) throws GkException{
		notifyListeners(event);
	}
	/**
	 * Notify the registered listeners with the given event
	 * @param event the event
	 * @throws GkException GkException
	 */
	private void notifyListeners(IWorkspaceEvent event) throws GkException {
		if(CollectionUtils.isNotEmpty(listenerList)){
			for (IWorkspaceListener workspaceListener : listenerList) {
				workspaceListener.onWorkspaceEvent(event);
			}
		}
	}

	/**
	 * @return the project
	 */
	public GkProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(GkProject project) {
		this.project = project;
	}
}
