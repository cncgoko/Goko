/**
 * 
 */
package org.goko.core.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.bean.EnumControllerAxis;

/**
 * 
 * Interface defining a jog service 
 * 
 * @author PsyKo
 *
 */
public interface IJogService {

	public void setJogStep(Length step) throws GkException;	
	public Length getJogStep() throws GkException;
	
	public void setJogFeedrate(Speed feed) throws GkException;	
	public Speed getJogFeedrate() throws GkException;
	
	public void setJogPrecise(boolean precise) throws GkException;
	public boolean isJogPrecise() throws GkException;
	
	public boolean isJogPreciseForced() throws GkException;
	
	public void startJog(EnumControllerAxis axis) throws GkException;	
	
	public void stopJog() throws GkException;
}
