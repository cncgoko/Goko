/**
 *
 */
package org.goko.controller.tinyg.controller;

import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.log.GkLog;

/**
 * TinyG Jogging utility
 * @author PsyKo
 */
public class TinyGJogging {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(TinyGJogging.class);
	/** The target TinyG service */
	private ITinygControllerService tinygService;
	/** The target communicator */
	private TinyGCommunicator tinygCommunicator;
	/** The estimated jog interval */
	private long period = 100; // 100ms aka 10Hz
	/** The active distance mode before jog */
	private EnumDistanceMode previousDistanceMode;
	
	/**
	 * Constructor
	 */
	public TinyGJogging(ITinygControllerService tinygService, TinyGCommunicator tinygCommunicator) {
		this.tinygCommunicator = tinygCommunicator;
		this.tinygService = tinygService;
	}
	
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {	
		Length localStep = step;				
		EnumTinyGAxis tinygAxis = EnumTinyGAxis.getEnum(axis.getCode());
		if(isReadyToJog()){
			if(axis != null){			
				if(previousDistanceMode == null){
					previousDistanceMode = tinygService.getGCodeContext().getDistanceMode();	
				}
				EnumUnit contextUnit = tinygService.getGCodeContext().getUnit();
				String command = "G91G1";
				if(feedrate != null){
					command += "F"+ QuantityUtils.format(feedrate, 0, true, false, contextUnit.getFeedUnit());	
				}				
				
				if(step == null){
					localStep = feedrate.multiply(Time.valueOf(period, TimeUnit.MILLISECOND).multiply(2));					
				}
											
				command = getRelativeJogCommand(command, tinygAxis, localStep);
				tinygCommunicator.send(GkUtils.toBytesList(command), true);				
				if(previousDistanceMode == EnumDistanceMode.ABSOLUTE){					
					tinygCommunicator.send(GkUtils.toBytesList("G90"), true);
				}								
			}
			
		}	
	}
	/**
	 * Determine if Grbl is ready to jog
	 * @return <code>true</code> if Grbl is ready to receive another jog order, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	protected boolean isReadyToJog() throws GkException{
//		if(precise){
//			MachineState grblState = tinygService.getState();
//			return MachineState.READY.equals(grblState) || MachineState.PROGRAM_END.equals(grblState) || MachineState.PROGRAM_STOP.equals(grblState);
//		}		
		return tinygService.getAvailableBuffer() > 28;
	}
	
	/**
	 * Stops jogging
	 * @throws GkException GkException 
	 */
	protected void stopJog() throws GkException{
		tinygService.stopMotion();
		// Restore previous distance mode		
		if(previousDistanceMode == EnumDistanceMode.ABSOLUTE){
			tinygCommunicator.send(GkUtils.toBytesList("G90"), true);
		}
		previousDistanceMode  = null;
	}
	/**
	 * Generates jogging command when Grbl is in relative distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String getRelativeJogCommand(String command, EnumTinyGAxis axis, Length step) throws GkException{
		command += axis.getAxisCode();
		Unit<Length> currentUnit = tinygService.getGCodeContext().getUnit().getUnit();
		if(axis.isNegative()){
			command+="-";
		}
		command += GokoPreference.getInstance().format(step, true, false, currentUnit);
		return command;
	}
}
