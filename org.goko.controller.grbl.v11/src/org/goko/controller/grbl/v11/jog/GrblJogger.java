/**
 * 
 */
package org.goko.controller.grbl.v11.jog;

import org.goko.controller.grbl.commons.jog.AbstractGrblJogger;
import org.goko.controller.grbl.v11.Grbl;
import org.goko.controller.grbl.v11.GrblControllerService;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Grbl Jogger for 1.1
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblJogger extends AbstractGrblJogger<GrblConfiguration, GrblMachineState, GrblControllerService> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GrblJogger.class);
	/**
	 * Constructor
	 * @param grblService the targeted Grbl service
	 * @throws GkException
	 */
	public GrblJogger(GrblControllerService grblService) {
		super(grblService);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.jog.AbstractGrblJogger#isReadyToJog()
	 */
	@Override
	protected boolean isReadyToJog() throws GkException {
		GrblMachineState currentState = getControllerService().getState();
		return GrblMachineState.JOG.equals(currentState) || GrblMachineState.IDLE.equals(currentState);
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.jog.AbstractGrblJogger#stopJog()
	 */
	@Override
	public void stopJog() throws GkException {		
		getControllerService().getCommunicator().sendImmediately(Grbl.Commands.JOG_CANCEL, true);	
	}
}
