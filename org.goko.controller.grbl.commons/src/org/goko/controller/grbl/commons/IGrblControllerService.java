/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.commons;

import java.util.List;

import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener;
import org.goko.controller.grbl.commons.schedule.GrblScheduler;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContextProvider;
import org.goko.core.math.Tuple6b;

/**
 * Definition of the Grbl service
 *
 * @author PsyKo
 *
 */
public interface IGrblControllerService<C extends AbstractGrblConfiguration<C>, M extends MachineState> extends IControllerService<ExecutionTokenState, GCodeContext>,
																						IJogService,
																						IThreeAxisControllerAdapter,
																						ICoordinateSystemAdapter<ICoordinateSystem>,
																						IControllerConfigurationFileExporter,
																						IControllerConfigurationFileImporter,
																						IProbingService,
																						GCodeContextProvider{

	/**
	 * Adds the given {@link IGrblConfigurationListener} as a listener to this service configuration event
	 * @param listener the listener to add
	 */	
	void addConfigurationListener(IGrblConfigurationListener<C> listener);
	
	/**
	 * Removes the given {@link IGrblConfigurationListener} from the listener of this service configuration event
	 * @param listener the listener to remove
	 */
	void removeConfigurationListener(IGrblConfigurationListener<C> listener);
	
	/**
	 * Sets the configuration 
	 * @param configuration the configuration to set
	 * @throws GkException GkException
	 */
	void setConfiguration(C configuration) throws GkException;

	/**
	 * Sets the configuration and sends the modification to the board 
	 * @param configuration the configuration to set
	 * @throws GkException GkException
	 */
	void applyConfiguration(C configuration) throws GkException;
	
	/**
	 * Returns the configuration 
	 * @return the Grbl configuration
	 * @throws GkException GkException
	 */
	C getConfiguration() throws GkException;
	
	/**
	 * Set the value of the given identifier
	 * @param identifier the identifier of the setting
	 * @param value the value of the setting
	 * @throws GkException GkException
	 */
	void setConfigurationSetting(String identifier, String value) throws GkException;
	
	/**
	 * Sets the current controller state 
	 * @param state the state to set
	 * @throws GkException
	 */
	void setState(M state) throws GkException;
	
	/**
	 * Returns current controller state 
	 * @return MachineState
	 * @throws GkException
	 */
	MachineState getState() throws GkException;
	
	/**
	 * Returns the unit for the reported positions
	 * @return unit the unit of this controller
	 */
	Unit<Length> getReportUnit();
	
	/**
	 * Updates the GCode context
	 * @param updatedGCodeContext the context to get the value from
	 * @throws GkException GkException
	 */
	void updateGCodeContext(GCodeContext updatedGCodeContext) throws GkException;
	
	/**
	 * Set the state of the active polling
	 * @param enabled <code>true</code> to enable it, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	void setActivePollingEnabled(boolean enabled) throws GkException;

	/**
	 * Return the state of the active polling
	 * @return boolean
	 * @throws GkException GkException
	 */
	boolean isActivePollingEnabled() throws GkException;

	/**
	 * Enables check mode on Grbl
	 * @param enabled <code>true</code> to enable it, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	void setCheckModeEnabled(boolean enabled) throws GkException;

	/**
	 * Returns the count of used Grbl Rxtx buffer
	 * @return an integer
	 * @throws GkException GkException
	 */
	int getAvailableRxBuffer() throws GkException;
	
	/**
	 * Returns the count of used Grbl planner buffer
	 * @return an integer
	 * @throws GkException GkException
	 */
	int getAvailablePlannerBuffer() throws GkException;
	
	/**
	 * Returns <code>true</code> if the planner buffer is completely empty (no motion running)
	 * @return an integer
	 * @throws GkException GkException
	 */
	boolean isPlannerBufferEmpty() throws GkException;

	/**
	 * Sends the given GCode line
	 * @param line the line to send
	 * @throws GkException GkException
	 */
	void send(GCodeLine line) throws GkException;
	
	/**
	 * Sets the offset from machine origin for the given coordinate system 
	 * @param coordinateSystem the coordinate system 
	 * @param offset the offset from machine origin
	 * @throws GkException GkException
	 */
	void setCoordinateSystemOffset(ICoordinateSystem coordinateSystem, Tuple6b offset) throws GkException;

	/**
	 * Sends a request status
	 * @throws GkException GkException 
	 */
	void requestStatus() throws GkException;
	
	/**
	 * Entry point for Kill Alarm action
	 * @throws GkException GkException
	 */
	void killAlarm() throws GkException;
	
	/**
	 * Entry point for Pause Motion action
	 * @throws GkException GkException
	 */
	void pauseMotion() throws GkException;
	
	/**
	 * Entry point for Resume motion action
	 * @throws GkException GkException
	 */
	void resumeMotion() throws GkException;
	
	/**
	 * Entry point for Start Motion action
	 * @throws GkException GkException
	 */
	void startMotion() throws GkException;
	
	/**
	 * Entry point for Stop Motion action
	 * @throws GkException GkException
	 */
	void stopMotion() throws GkException;
	
	/**
	 * Entry point for Reset Zero action
	 * @throws GkException GkException
	 */
	void resetZero(List<String> axes) throws GkException;
	
	/**
	 * Entry point for Homing action
	 * @throws GkException GkException
	 */
	void startHomingSequence() throws GkException;
	
	/**
	 * Reset the Grbl board
	 * @throws GkException GkException
	 */
	void resetGrbl() throws GkException;
	
	/**
	 * Action to perform on connection 
	 * @throws GkException GkException
	 */
	void onConnected() throws GkException;
	
	/**
	 * Action to perform on disconnection 
	 * @throws GkException GkException
	 */
	void onDisconnected() throws GkException;
	
	/**
	 * Returns a scheduler element
	 * @return Scheduler
	 */
	GrblScheduler schedule();
	
	/**
	 * Returns this controller communicator
	 * @return the communicator
	 */
	AbstractGrblCommunicator<C, M, ?> getCommunicator();
	
	/**
	 * Returns the number of commands that were send but not acknowledged
	 * @return int
	 */
	int getPendingCommandsCount();
}
