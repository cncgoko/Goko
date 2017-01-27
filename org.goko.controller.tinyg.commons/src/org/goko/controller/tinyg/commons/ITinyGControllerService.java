/**
 * 
 */
package org.goko.controller.tinyg.commons;

import java.util.List;

import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener;
import org.goko.controller.tinyg.commons.schedule.Scheduler;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;
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
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public interface ITinyGControllerService<C extends AbstractTinyGConfiguration<C>> extends IControllerService<ExecutionTokenState, GCodeContext>,
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
	 * Adds the given {@link ITinyGConfigurationListener} as a listener to this service configuration event
	 * @param listener the listener to add
	 */	
	void addConfigurationListener(ITinyGConfigurationListener<C> listener);
	
	/**
	 * Removes the given {@link ITinyGConfigurationListener} from the listener of this service configuration event
	 * @param listener the listener to remove
	 */
	void removeConfigurationListener(ITinyGConfigurationListener<C> listener);
	
	/**
	 * Returns the current length unit
	 * @return Unit
	 */
	Unit<Length> getCurrentUnit();
	
	/**
	 * Sets the current controller state 
	 * @param state the state to set
	 * @throws GkException
	 */
	void setState(MachineState state) throws GkException;
	
	/**
	 * Returns current controller state 
	 * @return MachineState
	 * @throws GkException
	 */
	MachineState getState() throws GkException;
	
	/**
	 * Sets the current velocity 
	 * @param velocity the velocity to set
	 * @throws GkException
	 */
	void setVelocity(Speed velocity) throws GkException;
	
	/**
	 * Returns the current velocity
	 * @return the current velocity
	 */
	Speed getVelocity() throws GkException;
	
	/**
	 * Updates the GCode context
	 * @throws GkException GkException
	 */
	void updateGCodeContext(final GCodeContext context) throws GkException;
		
	/**
	 * Stops any motion 
	 * @throws GkException GkException
	 */
	void stopMotion() throws GkException;
	
	/**
	 * Stops any jog motion 
	 * @throws GkException GkException
	 */
	void stopJog() throws GkException;
	
	/**
	 * Returns the running communicator
	 * @return
	 */
	AbstractTinyGCommunicator<C, ?> getCommunicator();
	
	/**
	 * Returns a scheduler element
	 * @return Scheduler
	 */
	<S extends ITinyGControllerService<C>> Scheduler<S> schedule();
	
	/**
	 * Returns the current configuration
	 * @return C
	 */
	C getConfiguration();

	/**
	 * Sets the given configuration and send it to the distant board
	 * @param configuration the configuration
	 * @throws GkException GkException
	 */
	void applyConfiguration(C configuration) throws GkException;
	
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
	 * Entry point for Reset action
	 * @throws GkException GkException
	 */
	void resetTinyG() throws GkException;
	
	/**
	 * Entry point for Turn Spindle On action
	 * @throws GkException GkException
	 */
	void turnSpindleOn() throws GkException;
	
	/**
	 * Entry point for Turn Spindle Off action
	 * @throws GkException GkException
	 */
	void turnSpindleOff() throws GkException;
	
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
	 * Returns the available planner buffer count 
	 * @return an integer
	 */
	int getAvailablePlannerBuffer();
	
	/**
	 * Sets the available planner buffer count 
	 * @param available the number of available slots
	 */
	void setAvailablePlannerBuffer(int available);

	/**
	 * Getter on the planner buffer check mode
	 * @return <code>true</code> if TinyG runs using the planner buffer, <code>false</code> otherwise
	 */
	boolean isPlannerBufferCheck();
	
	/**
	 * Setter on the planner buffer check mode
	 * @param plannerBufferCheck <code>true</code> if TinyG runs using the planner buffer, <code>false</code> otherwise
	 */
	void setPlannerBufferCheck(boolean plannerBufferCheck);
	
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
}
