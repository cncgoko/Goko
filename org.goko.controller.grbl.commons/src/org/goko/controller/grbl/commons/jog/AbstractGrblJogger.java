/**
 *
 */
package org.goko.controller.grbl.commons.jog;

import org.goko.controller.grbl.commons.IGrblControllerService;
import org.goko.controller.grbl.commons.bean.EnumGrblAxis;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 *
 */
public abstract class AbstractGrblJogger<F extends AbstractGrblConfiguration<F>, M extends MachineState, S extends IGrblControllerService<F, M>> {
	private static final GkLog LOG = GkLog.getLogger(AbstractGrblJogger.class);	
	private S controllerService;
	private long period = 100; // 100ms aka 10Hz
	
	/**
	 * Constructor
	 * @throws GkException GkException 
	 */
	public AbstractGrblJogger(S grblService) {
		this.controllerService = grblService;
	}
	
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {	
		Length localStep = step;				
		EnumGrblAxis tinygAxis = EnumGrblAxis.getEnum(axis.getCode());
		if(isReadyToJog()){
			if(axis != null && feedrate != null){	
				EnumUnit contextUnit = controllerService.getGCodeContext().getUnit();
				String command = "$J=G91";
				command += "F"+ QuantityUtils.format(feedrate, 0, true, false, contextUnit.getFeedUnit());	
				if(step == null){
					localStep = feedrate.multiply(Time.valueOf(period, TimeUnit.MILLISECOND));					
				}
												
				command = getRelativeJogCommand(command, tinygAxis, localStep);
				
				controllerService.getCommunicator().send(command, true);
			}
			
		}	
	}
	/**
	 * Determine if Grbl is ready to jog
	 * @return <code>true</code> if Grbl is ready to receive another jog order, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	protected abstract boolean isReadyToJog() throws GkException;

	
	/**
	 * Stops the jog
	 * @throws GkException 
	 */
	public abstract void stopJog() throws GkException;

	/**
	 * Generates jogging command when Grbl is in relative distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String getRelativeJogCommand(String command, EnumGrblAxis axis, Length step) throws GkException{
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
}
