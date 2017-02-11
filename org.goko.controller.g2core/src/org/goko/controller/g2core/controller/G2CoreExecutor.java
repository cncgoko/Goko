/**
 * 
 */
package org.goko.controller.g2core.controller;

import org.goko.controller.tinyg.commons.AbstractTinyGExecutor;

/**
 * @author Psyko
 * @date 15 janv. 2017
 */
public class G2CoreExecutor extends AbstractTinyGExecutor<G2CoreControllerService> {

	/**
	 * Constructor 
	 * @param tinygService the running tinyg service
	 */
	public G2CoreExecutor(G2CoreControllerService tinygService) {
		super(tinygService);
		setRequiredBufferSpace(8);
	}

}
