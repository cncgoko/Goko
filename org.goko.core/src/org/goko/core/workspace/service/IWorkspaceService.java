/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.workspace.service;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.io.IResourceLocation;

public interface IWorkspaceService extends IGokoService{

	void notifyWorkspaceEvent(IWorkspaceEvent event) throws GkException;

	void addWorkspaceListener(IWorkspaceListener listener) throws GkException;

	void removeWorkspaceListener(IWorkspaceListener listener) throws GkException;

	void addProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException;

	void removeProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException;

	void addProjectSaveParticipant(IProjectSaveParticipant<?> participant) throws GkException;

	void addProjectLoadParticipant(IProjectLoadParticipant participant) throws GkException;

	GkProject getProject() throws GkException;

	void markProjectDirty() throws GkException;

	void createNewProject() throws GkException;
	
	void saveProject(IProjectLocation output, IProgressMonitor monitor) throws GkException;

	void loadProject(IProjectLocation input, IProgressMonitor monitor) throws GkException;
	
	// Tests
	
	IResourceLocation addResource(URI uri) throws GkException;
		
}
