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
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContextProvider;
import org.goko.core.gcode.service.IGCodeExecutionListener;

public interface ITinygControllerService extends IControllerService<ExecutionTokenState, GCodeContext>,
												 IProbingService,
												 IFourAxisControllerAdapter,
												 ICoordinateSystemAdapter<ICoordinateSystem>,
												 IJogService,												 
												 IWorkVolumeProvider,
												 IControllerConfigurationFileExporter,
												 IControllerConfigurationFileImporter,
												 IGCodeExecutionListener<ExecutionTokenState, IExecutionToken<ExecutionTokenState>>,
												 GCodeContextProvider{

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
	
	/**
	 * Sets the check for planner buffer space.
	 * @param plannerBufferSpaceCheck the state of planner buffer check
	 * @throws GkException GkException
	 */
	void setPlannerBufferSpaceCheck(boolean plannerBufferSpaceCheck) throws GkException;
	
	/**
	 * Returns the state of the planner buffer check 
	 * @return <code>true</code> if the test is enabled, <code>false</code> otherwise
	 */
	boolean isPlannerBufferSpaceCheck();

	/**
	 * Returns the machine state 
	 * @return MachineState
	 * @throws GkException GkException
	 */
	MachineState getState() throws GkException;
	
	void killAlarm() throws GkException;
	
	public int getAvailableBuffer() throws GkException;
	
	public void setAvailableBuffer(int availableBuffer) throws GkException;
	
	void send(GCodeLine gCodeLine) throws GkException;
	
	void stopMotion() throws GkException;
	
	void pauseMotion() throws GkException;
	
	void resumeMotion() throws GkException;
	
	void startMotion() throws GkException;
}
