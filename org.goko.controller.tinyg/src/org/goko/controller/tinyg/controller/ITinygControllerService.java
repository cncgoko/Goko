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

import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.GCodeLine;

public interface ITinygControllerService extends ITinyGControllerService<TinyGConfiguration>,
												ITinyGConfigurationListener<TinyGConfiguration>{
	
	/**
	 * Returns the machine state 
	 * @return MachineState
	 * @throws GkException GkException
	 */
	MachineState getState() throws GkException;
	
	void send(GCodeLine gCodeLine) throws GkException;
	
	void killAlarm() throws GkException;
	
	void stopMotion() throws GkException;
	
	void pauseMotion() throws GkException;
	
	void resumeMotion() throws GkException;
	
	void startMotion() throws GkException;
	
	void resetTinyG() throws GkException;

	/**
	 * Apply the given configuration and sends the update to the board 
	 * @param configuration the new configuration
	 * @throws GkException GkException 
	 */
	void applyConfiguration(TinyGConfiguration configuration) throws GkException;
}
