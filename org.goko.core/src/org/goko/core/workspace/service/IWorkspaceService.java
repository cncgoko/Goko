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

import java.io.File;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.workspace.element.GkProject;

public interface IWorkspaceService extends IGokoService{

	void notifyWorkspaceEvent(IWorkspaceEvent event) throws GkException;

	void addWorkspaceListener(IWorkspaceListener listener) throws GkException;

	void removeWorkspaceListener(IWorkspaceListener listener) throws GkException;

	void addProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException;

	void removeProjectLifecycleListener(IProjectLifecycleListener listener) throws GkException;

	void addProjectSaveParticipant(IProjectSaveParticipant<?> participant) throws GkException;

	void addProjectLoadParticipant(IProjectLoadParticipant<?> participant) throws GkException;

	IProjectLoadParticipant<?> findProjectLoadParticipantByType(String type) throws GkException;

	GkProject getProject() throws GkException;

	void markProjectDirty() throws GkException;

	void saveProject(File project) throws GkException;

	void loadProject(File project) throws GkException;
}
