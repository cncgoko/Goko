package org.goko.core.controller;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.common.event.IEventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeExecutionQueue;

/**
 * Interface definition for the controller.
 * The controller is in charge of
 * @author PsyKo
 *
 */
public interface IControllerService extends IGokoService, IEventDispatcher{

	/**
	 * Returns the current position of the machine
	 * @return a {@link Point3d} describing the current position of the machine
	 * @throws GkException an exception
	 */
	Point3d getPosition() throws GkException;
	/**
	 * Executes the GCode contained in the gcodeProvider
	 * @param gcodeProvider the {@link IGCodeProvider}
	 * @throws GkException GkException
	 */
	GCodeExecutionQueue executeGCode(IGCodeProvider gcodeProvider) throws GkException;

	/**
	 * Determine if the controller is ready to stream a file
	 * @return <code>true</code> if the controller is ready, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isReadyForFileStreaming() throws GkException;
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
	 * Returns the list of available values
	 * @return list of String
	 * @throws GkException GkException
	 */
	List<MachineValueDefinition> getMachineValueDefinition() throws GkException;

	MachineValueDefinition getMachineValueDefinition(String id) throws GkException;

	void cancelFileSending() throws GkException;

}
