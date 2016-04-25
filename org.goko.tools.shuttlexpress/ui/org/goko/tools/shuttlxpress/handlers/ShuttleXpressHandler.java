/**
 * 
 */
package org.goko.tools.shuttlxpress.handlers;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.log.GkLog;
import org.goko.tools.shuttlxpress.preferences.ShuttleXPressPreferences;

/**
 * Shuttle XPress support for jog
 * @author Psyko
 * @date 23 avr. 2016
 */
public class ShuttleXpressHandler {
	private static final GkLog LOG = GkLog.getLogger(ShuttleXpressHandler.class);
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
	private boolean isJogging;
	private Speed requestedFeed;
	private static long DEBOUNCE_TIME_MS = 100;
	
	@Inject IJogService jogService;
	
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
	public void execute(@Optional @Named("org.goko.tools.shuttlexpress.parameter.action") String action, @Optional @Named("org.goko.tools.shuttlexpress.parameter.offset") String offset) throws GkException{
		System.err.println("Execute "+action+" "+offset);
		if(StringUtils.isBlank(action)){
			return;
		}
		switch (action) {
		case STOP:  requestedFeed = null;	
					isJogging = false;
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

		default:
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
		jogService.setJogStep(step);
		if(offset > 0 ){
			currentAxis = currentPositiveAxis;
		}else{
			currentAxis = currentNegativeAxis;
		}
		jogService.setJogFeedrate(requestedFeed);
		jogService.setJogPrecise(true);
		jogService.startJog(currentAxis);
	}

	/**
	 * Perform a rapid jog 
	 * @param offset the offset of the wheel displacement
	 * @throws GkException GkException 
	 */
	private void rapidJog(Integer offset) throws GkException {
		double factor = (double)(Math.abs(offset)-1)/6;
		
		Speed minFeed = ShuttleXPressPreferences.getInstance().getRapidJogMinimumSpeed(); 
		Speed maxFeed = ShuttleXPressPreferences.getInstance().getRapidJogMaximumSpeed();
		requestedFeed = minFeed.add( maxFeed.subtract(minFeed).multiply(BigDecimal.valueOf(factor)));
		System.err.println("factor: "+factor+" feed = "+QuantityUtils.format(requestedFeed));		
		
		if(offset > 0 ){
			currentAxis = currentPositiveAxis;
		}else{
			currentAxis = currentNegativeAxis;
		}
		jogService.setJogFeedrate(requestedFeed);
		if(!isJogging){
				debounceRapidJog();
		}		
	}
	
	/**
	 * Debounce for rapid jog. Allows to wait a few amount of time until the user has reached the required position on the jog wheel.
	 * Otherwise, going from position 0 to position 7 will trigger all the jog with offset ranging from 0 to 7 (including 1, 2, 3,..., 6)
	 * @throws GkException GkException
	 */
	private void debounceRapidJog() throws GkException{
		isJogging = true;
		jogService.setJogPrecise(false);
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run()  {
				if(requestedFeed != null){
					try {
						jogService.startJog(currentAxis);
					} catch (GkException e) {
						LOG.error(e);
					}					
				}
			}	
		};
		timer.schedule(task, DEBOUNCE_TIME_MS);
	}
}
