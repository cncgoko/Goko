/**
 *
 */
package org.goko.controller.tinyg.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.goko.common.preferences.ScopedPreferenceStore;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 *
 */
public class TinyGJoggingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(TinyGJoggingRunnable.class);
	private static final String VALUE_STORE_ID = "org.goko.controller.tinyg.controller.TinyGJoggingRunnable";
	private static final String PERSISTED_FEED = "org.goko.controller.tinyg.controller.TinyGJoggingRunnable.feed";
	private static final String PERSISTED_STEP = "org.goko.controller.tinyg.controller.TinyGJoggingRunnable.step";
	private static final String PERSISTED_PRECISE = "org.goko.controller.tinyg.controller.TinyGJoggingRunnable.precise";
	private boolean jogging;
	private boolean stopped;
	private ITinygControllerService tinygService;
	private TinyGCommunicator tinygCommunicator;
	private Object lock;
	private EnumTinyGAxis axis;
	private BigDecimal feed;
	private Length step;
	private boolean precise;
	private ScopedPreferenceStore preferenceStore;

	/**
	 * Constructor
	 */
	public TinyGJoggingRunnable(ITinygControllerService tinygService, TinyGCommunicator tinygCommunicator) {
		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, VALUE_STORE_ID);
		this.initPersistedValues();
		this.lock = new Object();
		this.tinygCommunicator = tinygCommunicator;
		this.tinygService = tinygService;
	}

	private void initPersistedValues(){
		String feedStr = preferenceStore.getString(PERSISTED_FEED);
		if(StringUtils.isBlank(feedStr)){
			feedStr = "600";
		}
		this.feed = new BigDecimal(feedStr);
		String stepStr = preferenceStore.getString(PERSISTED_STEP);
		if(StringUtils.isBlank(stepStr)){
			stepStr = "1";
		}
		this.step = Length.valueOf(new BigDecimal(stepStr), Units.MILLIMETRE); // FIXME : store value between uses
		String preciseStr = preferenceStore.getString(PERSISTED_PRECISE);
		if(StringUtils.isBlank(preciseStr)){
			preciseStr = "false";
		}
		this.precise = Boolean.valueOf(preciseStr);
	}

	private void persistValues(){
		if(feed != null){
			preferenceStore.putValue(PERSISTED_FEED, feed.toPlainString());
		}
		preferenceStore.putValue(PERSISTED_PRECISE, String.valueOf(precise));

		if(step != null){
			preferenceStore.putValue(PERSISTED_STEP, step.value(Units.MILLIMETRE).toPlainString());
		}
	}
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(!stopped){
			try{
				waitJoggingActive();
				if(isReadyToJog()){
					if(axis != null && feed != null && step != null){
						String command = "G1F"+feed.toPlainString();
						EnumDistanceMode distanceMode = tinygService.getCurrentGCodeContext().getDistanceMode();
						if(distanceMode == EnumDistanceMode.ABSOLUTE){
							command = startAbsoluteJog(command);
						}else{
							command = startRelativeJog(command);
						}
						tinygCommunicator.send(GkUtils.toBytesList(command));

						if(precise){
							this.jogging = false;
						}
					}
				}
			}catch(GkException e){
				LOG.error(e);
			}
		}
	}

	/**
	 * Determine if TinyG is ready to jog
	 * @return <code>true</code> if TinyG is ready to receive another jog order, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	protected boolean isReadyToJog() throws GkException{
		if(precise){
			MachineState tinygState = tinygService.getState();
			return MachineState.READY.equals(tinygState) || MachineState.PROGRAM_END.equals(tinygState) || MachineState.PROGRAM_STOP.equals(tinygState);
		}
		return tinygService.getAvailableBuffer() > 28;
	}

	/**
	 * Generates jogging command when TinyG is in absolute distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String startAbsoluteJog(String command) throws GkException{
		command += axis.getAxisCode();
		BigDecimal target = null;
		Unit<Length> currentUnit = tinygService.getCurrentGCodeContext().getUnit().getUnit();
		switch (axis) {
		case X_NEGATIVE: target = tinygService.getX().subtract(step).value(currentUnit);
			break;
		case X_POSITIVE: target = tinygService.getX().add(step).value(currentUnit);
			break;
		case Y_NEGATIVE: target = tinygService.getY().subtract(step).value(currentUnit);
			break;
		case Y_POSITIVE: target = tinygService.getY().add(step).value(currentUnit);
			break;
		case Z_NEGATIVE: target = tinygService.getZ().subtract(step).value(currentUnit);
			break;
		case Z_POSITIVE: target = tinygService.getZ().add(step).value(currentUnit);
			break;
		case A_NEGATIVE: //target = tinygService.getA().value().subtract(step.value()); // FIXME : remove crappy subtraction between angle and distance
						throw new GkTechnicalException("A implémenter");
			//break;
		case A_POSITIVE: //target = tinygService.getA().value().add(step.value()); // FIXME : remove crappy addition between angle and distance
						throw new GkTechnicalException("A implémenter");
			//break;
		default:
			break;
		}
		command += target.setScale(5, RoundingMode.HALF_DOWN).toPlainString();
		return command;
	}

	/**
	 * Generates jogging command when TinyG is in relative distance mode
	 * @param command the base command
	 * @return a String
	 * @throws GkException GkException
	 */
	public String startRelativeJog(String command) throws GkException{
		command += axis.getAxisCode();
		if(axis.isNegative()){
			command+="-";
		}
		command += GokoPreference.getInstance().format(step, true, false);
		return command;
	}

	/**
	 * Wait until the jog is activated
	 * @throws GkException GkException
	 */
	private void waitJoggingActive() throws GkException{
		do{
			synchronized ( lock ) {
				try {
					lock.wait(500);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		}while( !isJogging() );
	}

	/**
	 * @return the jogging
	 */
	public boolean isJogging() {
		return jogging;
	}
	/**
	 * Start jogging
	 * @throws GkException GkException
	 */
	public void enableJogging() throws GkException{
		this.jogging = true;
		synchronized (lock) {
			lock.notify();
		}
	}

	/**
	 * Stop jogging
	 * @throws GkException GkException
	 */
	public void disableJogging() throws GkException{
		this.jogging = false;
		synchronized (lock) {
			lock.notify();
		}
		String command = StringUtils.EMPTY;
		if(!precise){ // In precise mode, let TinyG finish the complete move
			command += "!%";
			tinygCommunicator.send(GkUtils.toBytesList(command));
		}
	}

	/**
	 * @return the axis
	 */
	public EnumTinyGAxis getAxis() {
		return axis;
	}

	/**
	 * @param axis the axis to set
	 */
	public void setAxis(EnumTinyGAxis axis) {
		this.axis = axis;
	}

	/**
	 * @return the feed
	 */
	public BigDecimal getFeed() {
		return feed;
	}

	/**
	 * @param feed the feed to set
	 */
	public void setFeed(BigDecimal feed) {
		this.feed = feed;
		persistValues();
	}

	/**
	 * @return the step
	 */
	public Length getStep() {
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(Length step) {
		this.step = step;
		persistValues();
	}

	/**
	 * @return the precise
	 */
	public boolean isPrecise() {
		return precise;
	}

	/**
	 * @param precise the precise to set
	 */
	public void setPrecise(boolean precise) {
		this.precise = precise;
		persistValues();
	}
}
