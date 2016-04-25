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
package org.goko.core.controller;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.common.event.IEventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.execution.IExecutionTokenState;
import org.goko.core.math.Tuple6b;
/**
 * Interface definition for the controller.
 * The controller is in charge of
 * @author PsyKo
 *
 */
public interface IControllerService<S extends IExecutionTokenState, G extends IGCodeContext> extends IGokoService, IEventDispatcher{	
	public static final String CONTROLLER_TOPIC				 = "ControllerEvent";
	public static final String CONTROLLER_TOPIC_ALL			 = "ControllerEvent/*";
	public static final String CONTROLLER_TOPIC_STATE_UPDATE = CONTROLLER_TOPIC+"/StateUpdate";
	/**
	 * Returns the current position of the machine
	 * @return a {@link Point3d} describing the current position of the machine
	 * @throws GkException an exception
	 */
	Tuple6b getPosition() throws GkException;

	/**
	 * Determine if the controller is ready to stream a file
	 * @return <code>true</code> if the controller is ready, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isReadyForFileStreaming() throws GkException;
	
	/**
	 * Performs the required checks before execution
	 * @throws GkException GkException
	 */
	void verifyReadyForExecution() throws GkException;
	
	/**
	 * Executes the given action
	 * @param actionId the id action of the requested action
	 * @return the requested {@link IGkControllerAction}
	 * @throws GkException an exception
	 */
	IGkControllerAction getControllerAction(String actionId) throws GkException;

	/**
	 * Determines if the given action is supported by the controller
	 * @param actionId the id action of the requested action
	 * @return <code>true</code> if the action is supported, <code>false</code> otherwise
	 * @throws GkException an exception
	 */
	boolean isControllerAction(String actionId) throws GkException;

	/**
	 * Returns a value of a controller state, parameter...
	 * @param name the name of the requested value
	 * @param clazz the requested class
	 * @return {@link ControllerValue}
	 * @throws GkException GkException
	 */
	<T> MachineValue<T> getMachineValue(String name, Class<T> clazz) throws GkException;

	/**
	 * Returns the type of the value with the given name
	 * @param name the name of the requested value
	 * @return the class of the value
	 * @throws GkException GkException
	 */
	Class<?> getMachineValueType(String name) throws GkException;
	/**
	 * Returns the current GCode context of the controller
	 * @return GCodeContext
	 * @throws GkException exception
	 */
	G getGCodeContext() throws GkException;

	/**
	 * Returns the list of available values
	 * @return list of String
	 * @throws GkException GkException
	 */
	List<MachineValueDefinition> getMachineValueDefinition() throws GkException;

	MachineValueDefinition getMachineValueDefinition(String id) throws GkException;
	MachineValueDefinition findMachineValueDefinition(String id) throws GkException;

	void cancelFileSending() throws GkException;

	void moveToAbsolutePosition(Tuple6b position) throws GkException;
	
}
