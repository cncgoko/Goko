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
	
	public void jog(EnumControllerAxis axis, Length step, Speed feedrate) throws GkException;	
	
	public void stopJog() throws GkException;
}
