/**
 *
 */
package org.goko.controller.tinyg.commons.jog;

import org.goko.controller.tinyg.commons.AbstractTinyGCommunicator;
import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.controller.tinyg.commons.bean.EnumTinyGAxis;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;

/**
 * TinyG Jogging utility
 * @author PsyKo
 */
public abstract class AbstractTinyGJogger<F extends AbstractTinyGConfiguration<F>, S extends ITinyGControllerService<F>, C extends AbstractTinyGCommunicator<F, S>> {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(AbstractTinyGJogger.class);
	/** The target TinyG service */
	private S controllerService;
	/** The target communicator */
	private C communicator;
	/** The estimated jog interval */
	private long period = 100; // 100ms aka 10Hz
	/** The active distance mode before jog */
	private EnumDistanceMode previousDistanceMode;
	/** Stop requested flag - when complete effective stop before jogging again*/
	private boolean stopRequested;
	
	/**
	 * Constructor
	 */
	public AbstractTinyGJogger(S controllerService, C communicator) {
		this.controllerService = controllerService;
		this.communicator = communicator;
		this.stopRequested = false;
	}
	
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {
		Length localStep = step;				
		EnumTinyGAxis tinygAxis = EnumTinyGAxis.getEnum(axis.getCode());
		
		if(!stopRequested && isReadyToJog()){
			if(axis != null){	
				GCodeContext context = controllerService.getGCodeContext();
				if(previousDistanceMode == null){					
					previousDistanceMode = context.getDistanceMode();	
				}
				EnumUnit contextUnit = context.getUnit();
				String command = "G91G1";
				if(feedrate != null){
					command += "F"+ QuantityUtils.format(feedrate, 0, true, false, contextUnit.getFeedUnit());	
				}				
				
				if(step == null){
					localStep = feedrate.multiply(Time.valueOf(period, TimeUnit.MILLISECOND).multiply(2));					
				}
											
				command = getRelativeJogCommand(command, tinygAxis, localStep);
				communicator.send(command, true);	
			}
			
		}	
	}
	/**
	 * Determine if TinyG is ready to jog
	 * @return <code>true</code> if TinyG is ready to receive another jog order, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	abstract protected boolean isReadyToJog() throws GkException;
	
	/**
	 * Stops jogging
	 * @throws GkException GkException 
	 */
	public void stopJog() throws GkException{
		if(previousDistanceMode != null){
			stopRequested = true;	
			controllerService.stopMotion();

			// Restore previous distance mode
			controllerService.schedule().execute(getRestoreDistanceModeRunnable(previousDistanceMode)).whenState(MachineState.PROGRAM_STOP).timeout(5, TimeUnit.SECOND).begin();
			if(previousDistanceMode != null){
				controllerService.schedule().execute(new Runnable() {
					
					@Override
					public void run() {
						previousDistanceMode  = null;
						stopRequested = false;						
					}
				}).when(DefaultControllerValues.CONTEXT_DISTANCE_MODE, previousDistanceMode.name()).timeout(5, TimeUnit.SECOND).begin();			
			}
		}
	}
	
	/**
	 * Creates the runnable that will restore the previous distance mode 
	 * @param pPreviousDistanceMode the previous distance mode
	 * @return Runnable
	 */
	protected Runnable getRestoreDistanceModeRunnable(EnumDistanceMode pPreviousDistanceMode){
		return new Runnable() {				
			@Override
			public void run() {
				try {
					if(EnumDistanceMode.ABSOLUTE == pPreviousDistanceMode){
						communicator.send("G90", true);
					}else{
						communicator.send("G91", true);
					}
					
				} catch (GkException e) {
					LOG.error(e);
				}					
			}
		};
	}
	/**
	 * Generates jogging command when Grbl is in relative distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String getRelativeJogCommand(String command, EnumTinyGAxis axis, Length step) throws GkException{
		command += axis.getAxisCode();
		Unit<Length> currentUnit = controllerService.getGCodeContext().getUnit().getUnit();
		if(axis.isNegative()){
			command+="-";
		}
		command += GokoPreference.getInstance().format(step, true, false, currentUnit);
		return command;
	}

	/**
	 * @return the controllerService
	 */
	public S getControllerService() {
		return controllerService;
	}

	/**
	 * @return the communicator
	 */
	public C getCommunicator() {
		return communicator;
	}
}
