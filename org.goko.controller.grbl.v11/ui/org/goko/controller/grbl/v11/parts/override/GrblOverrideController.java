/**
 * 
 */
package org.goko.controller.grbl.v11.parts.override;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.controller.grbl.commons.IGrblOverrideService;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 10 avr. 2017
 */
public class GrblOverrideController extends AbstractController<GrblOverrideModel> {
	private static final GkLog LOG = GkLog.getLogger(GrblOverrideController.class);
	@Inject
	private IGrblOverrideService grblOverrideService;
	
	/**
	 * @param binding
	 */
	public GrblOverrideController() {
		super(new GrblOverrideModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
				
	}

	public void resetSpindleSpeed(){
		try{
			grblOverrideService.resetSpindleSpeed();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void increaseSpindleSpeed1(){
		try{
			grblOverrideService.increaseSpindleSpeed1();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void increaseSpindleSpeed10(){
		try{
			grblOverrideService.increaseSpindleSpeed10();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void decreaseSpindleSpeed1(){
		try{
			grblOverrideService.decreaseSpindleSpeed1();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void decreaseSpindleSpeed10(){
		try{
			grblOverrideService.decreaseSpindleSpeed10();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void resetFeedSpeed(){
		try{
			grblOverrideService.resetFeedSpeed();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void increaseFeedSpeed1(){
		try{
			grblOverrideService.increaseFeedSpeed1();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void increaseFeedSpeed10(){
		try{
			grblOverrideService.increaseFeedSpeed10();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void decreaseFeedSpeed1(){
		try{
			grblOverrideService.decreaseFeedSpeed1();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void decreaseFeedSpeed10(){
		try{
			grblOverrideService.decreaseFeedSpeed10();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void setRapidSpeed25(){
		try{
			grblOverrideService.setRapidSpeed25();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void setRapidSpeed50(){
		try{
			grblOverrideService.setRapidSpeed50();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	public void resetRapidSpeed(){
		try{
			grblOverrideService.resetRapidSpeed();
		}catch(GkException e){
			LOG.error(e);
		}
	}
}
