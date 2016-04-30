/**
 *
 */
package org.goko.controller.grbl.v08;

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
 * @author PsyKo
 *
 */
public class GrblJogging {
	private static final GkLog LOG = GkLog.getLogger(GrblJogging.class);	
	private IGrblControllerService grblService;
	private GrblCommunicator grblCommunicator;	
	private long period = 100; // 100ms aka 10Hz
	
	/**
	 * Constructor
	 * @throws GkException GkException 
	 */
	public GrblJogging(IGrblControllerService grblService, GrblCommunicator grblCommunicator) throws GkException {
//		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, VALUE_STORE_ID);
//		this.initPersistedValues();
//		this.lock = new Object();
		this.grblCommunicator = grblCommunicator;
		this.grblService = grblService;
	}
	
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException {	
		Length localStep = step;				
		EnumGrblAxis tinygAxis = EnumGrblAxis.getEnum(axis.getCode());
		if(isReadyToJog()){
			if(axis != null){						
				EnumUnit contextUnit = grblService.getGCodeContext().getUnit();
				String command = "G91G1";
				if(feedrate != null){
					command += "F"+ QuantityUtils.format(feedrate, 0, true, false, contextUnit.getFeedUnit());	
				}				
				EnumDistanceMode distanceMode = grblService.getGCodeContext().getDistanceMode();
				if(step == null){
					localStep = feedrate.multiply(Time.valueOf(period, TimeUnit.MILLISECOND));					
				}
												
				command = startRelativeJog(command, tinygAxis, localStep);
				
				grblCommunicator.send(GkUtils.toBytesList(command));
				if(distanceMode == EnumDistanceMode.ABSOLUTE){
					grblCommunicator.send(GkUtils.toBytesList("G90"));	
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
//			MachineState grblState = grblService.getState();
//			return GrblMachineState.READY.equals(grblState) || GrblMachineState.PROGRAM_END.equals(grblState) || GrblMachineState.PROGRAM_STOP.equals(grblState);
//		}		
		return  grblService.getUsedGrblBuffer() < 60;
	}
	
	/**
	 * Generates jogging command when Grbl is in relative distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String startRelativeJog(String command, EnumGrblAxis axis, Length step) throws GkException{
		command += axis.getAxisCode();
		Unit<Length> currentUnit = grblService.getGCodeContext().getUnit().getUnit();
		if(axis.isNegative()){
			command+="-";
		}
		command += GokoPreference.getInstance().format(step, true, false, currentUnit);
		return command;
	}
}
