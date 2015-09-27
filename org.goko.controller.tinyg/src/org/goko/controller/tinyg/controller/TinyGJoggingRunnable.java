/**
 * 
 */
package org.goko.controller.tinyg.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 *
 */
public class TinyGJoggingRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(TinyGJoggingRunnable.class);
	private boolean jogging;
	private boolean stopped;
	private ITinygControllerService tinygService;
	private TinyGCommunicator tinygCommunicator;
	private Object lock;
	private EnumTinyGAxis axis;
	private BigDecimal feed;
	private BigDecimalQuantity<Length> step;
	private boolean precise;
	/**
	 * 
	 */
	public TinyGJoggingRunnable(ITinygControllerService tinygService, TinyGCommunicator tinygCommunicator) {
		lock = new Object();
		this.tinygCommunicator = tinygCommunicator;
		this.tinygService = tinygService;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {		
		while(!stopped){
			try{
				waitJoggingActive();
				if(tinygService.getAvailableBuffer() > 28){
					if(axis != null && feed != null && step != null){
						String command = "G1F"+feed.toPlainString();
						EnumGCodeCommandDistanceMode distanceMode = tinygService.getCurrentGCodeContext().getDistanceMode();
						if(distanceMode == EnumGCodeCommandDistanceMode.ABSOLUTE){
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
	 * Generates jogging command when TinyG is in absolute distance mode  
	 * @param command the base command 
	 * @return a String
	 * @throws GkException GkException
	 */
	public String startAbsoluteJog(String command) throws GkException{
		command += axis.getAxisCode();		
		BigDecimal target = null;
		switch (axis) {
		case X_NEGATIVE: target = tinygService.getX().subtract(step).value();
			break;
		case X_POSITIVE: target = tinygService.getX().add(step).value();
			break;
		case Y_NEGATIVE: target = tinygService.getY().subtract(step).value();
			break;
		case Y_POSITIVE: target = tinygService.getY().add(step).value();
			break;
		case Z_NEGATIVE: target = tinygService.getZ().subtract(step).value();
			break;
		case Z_POSITIVE: target = tinygService.getZ().add(step).value();
			break;
		case A_NEGATIVE: target = tinygService.getA().value().subtract(step.value()); // FIXME : remove crappy subtraction between angle and distance
			break;
		case A_POSITIVE: target = tinygService.getA().value().add(step.value()); // FIXME : remove crappy addition between angle and distance
			break;
		default:
			break;
		}
		command += target.toPlainString();
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
					lock.wait(50);
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
		String command = StringUtils.EMPTY;	
		if(!precise){ // In precise mode, let TinyG finish the complete move
			command += "!%";
			tinygCommunicator.send(GkUtils.toBytesList(command));
		}		
		if(!precise){ // In precise mode, let TinyG finish the complete move
			command += "!%";
		}
		tinygCommunicator.send(GkUtils.toBytesList(command));
		this.jogging = false;
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
	}

	/**
	 * @return the step
	 */
	public BigDecimalQuantity<Length> getStep() {
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(BigDecimalQuantity<Length> step) {
		this.step = step;
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
	}
}
