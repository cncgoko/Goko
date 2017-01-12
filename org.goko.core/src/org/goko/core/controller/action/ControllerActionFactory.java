/**
 * 
 */
package org.goko.core.controller.action;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheByCode;

/**
 * @author Psyko
 * @date 8 janv. 2017
 */
public abstract class ControllerActionFactory extends CacheByCode<IGkControllerAction>{
		
	/**
	 * Creates the actions for this given factory
	 * @throws GkException
	 */
	public abstract void createActions() throws GkException;
}
