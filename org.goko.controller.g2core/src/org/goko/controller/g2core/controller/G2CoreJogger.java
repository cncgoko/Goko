/**
 * 
 */
package org.goko.controller.g2core.controller;

import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.tinyg.commons.jog.AbstractTinyGJogger;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 11 janv. 2017
 */
public class G2CoreJogger extends AbstractTinyGJogger<G2CoreConfiguration, G2CoreControllerService, G2CoreCommunicator>{

	/**
	 * Constructor 
	 * @param controllerService
	 * @param communicator
	 */
	public G2CoreJogger(G2CoreControllerService controllerService, G2CoreCommunicator communicator) {
		super(controllerService, communicator);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.jog.AbstractTinyGJogger#isReadyToJog()
	 */
	@Override
	protected boolean isReadyToJog() throws GkException {
		return getControllerService().getAvailablePlannerBuffer() > 38;
	}

}
