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
package org.goko.controller.grbl.v08;

import org.goko.controller.grbl.v08.configuration.GrblConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IStepJogService;

/**
 * Definition of the Grbl service
 *
 * @author PsyKo
 *
 */
public interface IGrblControllerService extends IControllerService, IStepJogService{

	void setConfiguration(GrblConfiguration configuration) throws GkException;

	GrblConfiguration getConfiguration() throws GkException;

	GrblState getGrblState();
	
	void setActivePollingEnabled(boolean enabled) throws GkException;
	
	boolean isActivePollingEnabled() throws GkException;
	
	GrblMachineState getState() throws GkException;
	
	void setCheckModeEnabled(boolean enabled) throws GkException;
}
