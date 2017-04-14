/**
 * 
 */
package org.goko.tools.shuttlxpress.handlers;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.log.GkLog;
import org.goko.tools.shuttlxpress.preferences.ShuttleXPressPreferences;

/**
 * Shuttle XPress support for jog
 * @author Psyko
 * @date 23 avr. 2016
 */
public class ShuttleXpressHandler {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(ShuttleXpressHandler.class);
	/** Action parameter */
	private static final String ACTION_PARAMETER = "org.goko.tools.shuttlexpress.parameter.action";
	/** Offset parameter */
	private static final String OFFSET_PARAMETER = "org.goko.tools.shuttlexpress.parameter.offset";
	/** Action name for rapid jogging */
	private static final String RAPID_JOG = "rapidJog";
	/** Action name for precise jogging */
	private static final String PRECISE_JOG = "preciseJog";
	/** Action name for stop jogging */
	private static final String STOP = "stop";
	/** Action name for enabling X axis */
	private static final String ENABLE_X_AXIS = "xaxis";
	/** Action name for enabling Y axis */	
	private static final String ENABLE_Y_AXIS = "yaxis";
	/** Action name for enabling Z axis */
	private static final String ENABLE_Z_AXIS = "zaxis";
	/** Action name for enabling A axis */
	private static final String ENABLE_A_AXIS = "aaxis";
	private EnumControllerAxis currentPositiveAxis;
	private EnumControllerAxis currentNegativeAxis;
	private EnumControllerAxis currentAxis;
	private Speed requestedFeed;
	private Integer previousOffset;
	
	@Inject IJogService jogService;
	
	@Inject IControllerService controllerService;
	
	/**
	 * Constructor
	 */
	public ShuttleXpressHandler() {
		super();
		this.currentPositiveAxis = EnumControllerAxis.X_POSITIVE;
		this.currentNegativeAxis = EnumControllerAxis.X_NEGATIVE;		
	}
	
	/**
	 * Execute the given action
	 * @param action the name of the action
	 * @param offset the offset when jogging
	 * @throws GkException GkException
	 */
	@Execute
	@Inject
	public void execute(@Optional @Named(ACTION_PARAMETER) String action, @Optional @Named(OFFSET_PARAMETER) String offset) throws GkException{
		if(StringUtils.isBlank(action)){
			return;
		}
		switch (action) {
		case STOP:  requestedFeed = null;	
					jogService.stopJog();
			break;
		case RAPID_JOG:	rapidJog(Integer.valueOf(offset));
			break;
		case PRECISE_JOG: preciseJog(Integer.valueOf(offset));
			break;
		case ENABLE_X_AXIS:	this.currentPositiveAxis = EnumControllerAxis.X_POSITIVE;
							this.currentNegativeAxis = EnumControllerAxis.X_NEGATIVE;			
			break;
		case ENABLE_Y_AXIS:	this.currentPositiveAxis = EnumControllerAxis.Y_POSITIVE;
							this.currentNegativeAxis = EnumControllerAxis.Y_NEGATIVE;			
			break;
		case ENABLE_Z_AXIS:	this.currentPositiveAxis = EnumControllerAxis.Z_POSITIVE;
							this.currentNegativeAxis = EnumControllerAxis.Z_NEGATIVE;			
			break;
		case ENABLE_A_AXIS:	this.currentPositiveAxis = EnumControllerAxis.A_POSITIVE;
							this.currentNegativeAxis = EnumControllerAxis.A_NEGATIVE;			
					break;
		case DefaultControllerAction.RESET_ZERO: resetZero();
			break;
		default:
			if(isControllerAction(action)){
				executeControllerAction(action);
			}
			break;
		}
	}

	/**
	 * Perform a precise jog 
	 * @param offset the offset of the wheel displacement
	 * @throws GkException GkException 
	 */
	private void preciseJog(Integer offset) throws GkException {
		requestedFeed = ShuttleXPressPreferences.getInstance().getPreciseJogSpeed();				
		Length step = ShuttleXPressPreferences.getInstance().getPreciseJogStep();
		
		if(offset > 0 ){
			currentAxis = currentPositiveAxis;
		}else{
			currentAxis = currentNegativeAxis;
		}
		
		jogService.jog(currentAxis, step, requestedFeed);
	}

	/**
	 * Perform a rapid jog 
	 * @param offset the offset of the wheel displacement
	 * @throws GkException GkException 
	 */
	private void rapidJog(Integer offset) throws GkException {
		// Let's filter until the user has reached the required position on the jog wheel
		// Otherwise, going from position 0 to position 7 will trigger all the jog with offset ranging from 0 to 7 (including 1, 2, 3,..., 6)
		if(ObjectUtils.equals(offset, previousOffset)){
			double factor = (double)(Math.abs(offset)-1)/6;
			
			Speed minFeed = ShuttleXPressPreferences.getInstance().getRapidJogMinimumSpeed(); 
			Speed maxFeed = ShuttleXPressPreferences.getInstance().getRapidJogMaximumSpeed();
			requestedFeed = minFeed.add( maxFeed.subtract(minFeed).multiply(BigDecimal.valueOf(factor)));
					
			if(offset > 0 ){
				currentAxis = currentPositiveAxis;
			}else{
				currentAxis = currentNegativeAxis;
			}			
			jogService.jog(currentAxis, null, requestedFeed);
		}
		previousOffset = offset;
	}
	
	/**
	 * Determine if the given ID matches an action ID of the current controller
	 * @param actionId the ID of the action
	 * @return <code>true</code> if the given ID matches an action, <code>false</code> otherwise
	 */
	private boolean isControllerAction(String actionId){
		try {
			return controllerService.isControllerAction(actionId);
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
	}
	
	/**
	 * Execute the given action
	 * @param actionId the ID of the action
	 */
	private void executeControllerAction(String actionId){
		try {
			IGkControllerAction action = controllerService.getControllerAction(actionId);
			action.execute(null);
		} catch (GkException e) {
			LOG.error(e);			
		}
	}
	

	/**
	 * Reset zero on current selected axis 
	 */
	private void resetZero() {
		try {
			if(currentAxis != null){
				IGkControllerAction action = controllerService.getControllerAction(DefaultControllerAction.RESET_ZERO);
				action.execute(new String[]{currentAxis.getAxisCode()});
			}
		} catch (GkException e) {
			LOG.error(e);			
		}
	}

}
