/**
 * 
 */
package org.goko.controller.tinyg.commons;

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
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContextProvider;
import org.goko.core.gcode.service.IGCodeExecutionListener;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public interface ITinyGControllerService extends IControllerService<ExecutionTokenState, GCodeContext>,
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
	
	AbstractTinyGCommunicator<?> getCommunicator();
	
	<S extends ITinyGControllerService> Scheduler<S> schedule();
}
