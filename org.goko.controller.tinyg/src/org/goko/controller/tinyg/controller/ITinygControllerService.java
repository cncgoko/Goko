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
package org.goko.controller.tinyg.controller;

import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.controller.IContinuousJogService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.controller.bean.MachineState;

public interface ITinygControllerService extends IControllerService, IProbingService, IFourAxisControllerAdapter, ICoordinateSystemAdapter, IContinuousJogService, IWorkVolumeProvider {

	/**
	 * Returns the TinyG configuration
	 * @return TinyGConfiguration
	 * @throws GkException
	 */
	TinyGConfiguration getConfiguration() throws GkException;

	/**
	 * Set this controller configuration. This method SHOULD NOT send back the configuration to the tinyG board
	 * @param configuration the configuration to set
	 * @throws GkException GkException
	 */
	void setConfiguration(TinyGConfiguration configuration) throws GkException;

	/**
	 * Set this controller configuration. This method SHOULD send back the configuration to the tinyG board
	 * @param configuration the configuration to set
	 * @throws GkException GkException
	 */
	void updateConfiguration(TinyGConfiguration configuration) throws GkException;

	/**
	 * Request a configuration refresh
	 * @throws GkException GkException
	 */
	void refreshConfiguration() throws GkException;
	
	void setPlannerBufferSpaceCheck(boolean plannerBufferSpaceCheck) throws GkTechnicalException;
	boolean isPlannerBufferSpaceCheck();

	MachineState getState() throws GkException;
}
