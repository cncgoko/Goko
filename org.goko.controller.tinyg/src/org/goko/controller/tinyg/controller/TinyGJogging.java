/**
 *
 */
package org.goko.controller.tinyg.controller;

import org.goko.controller.tinyg.commons.jog.AbstractTinyGJogger;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

/**
 * TinyG Jogging utility
 * @author PsyKo
 */
/**
 * @author Psyko
 * @date 11 janv. 2017
 */
public class TinyGJogging extends AbstractTinyGJogger<TinyGConfiguration, TinyGControllerService, TinyGCommunicator>{

	/**
	 * Constructor 
	 * @param controllerService
	 * @param communicator
	 */
	public TinyGJogging(TinyGControllerService controllerService, TinyGCommunicator communicator) {
		super(controllerService, communicator);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.jog.AbstractTinyGJogger#isReadyToJog()
	 */
	@Override
	protected boolean isReadyToJog() throws GkException {
		return getControllerService().getAvailablePlannerBuffer() > 5;
	}

}