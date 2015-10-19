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

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IWorkspaceService extends IGokoService{

	void addGCodeProvider(IGCodeProvider provider) throws GkException;

	IGCodeProvider getGCodeProvider(Integer id) throws GkException;

	void setCurrentGCodeProvider(Integer id) throws GkException;

	IGCodeProvider getCurrentGCodeProvider() throws GkException;

	void deleteGCodeProvider(Integer id) throws GkException;

	void addWorkspaceListener(IWorkspaceListener listener) throws GkException;

	void removeWorkspaceListener(IWorkspaceListener listener) throws GkException;

}
