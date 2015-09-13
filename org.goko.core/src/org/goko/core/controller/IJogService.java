/**
 * 
 */
package org.goko.core.controller;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.EnumControllerAxis;

/**
 * 
 * Interface defining a jog service 
 * 
 * @author PsyKo
 *
 */
public interface IJogService {

	public void startJog(EnumControllerAxis axis, BigDecimal feedrate) throws GkException;

	public void stopJog() throws GkException;
}
