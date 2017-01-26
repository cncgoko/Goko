/**
 *
 */
package org.goko.controller.tinyg.controller;

import org.goko.controller.tinyg.commons.AbstractTinyGExecutor;

/**
 * @author Psyko
 * @date 15 janv. 2017
 */
public class TinyGExecutor extends AbstractTinyGExecutor<TinyGControllerService> {

	/**
	 * Constructor 
	 * @param tinygService the running tinyg service
	 */
	public TinyGExecutor(TinyGControllerService tinygService) {
		super(tinygService);
		setRequiredBufferSpace(8);
	}

}
