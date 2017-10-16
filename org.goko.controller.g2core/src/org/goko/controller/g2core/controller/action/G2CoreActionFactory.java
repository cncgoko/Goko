/**
 * 
 */
package org.goko.controller.g2core.controller.action;

import org.goko.controller.g2core.controller.G2CoreControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.ControllerActionFactory;

/**
 * G2Core Action factory
 * 
 * @author Psyko
 * @date 10 janv. 2017
 */
public class G2CoreActionFactory extends ControllerActionFactory {
	/** The used G2Core controller service*/
	private G2CoreControllerService controllerService;
	
	/**
	 * Default constructor
	 * @throws GkException
	 */
	public G2CoreActionFactory(G2CoreControllerService controllerService) throws GkException {
		super();
		this.controllerService = controllerService;
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.ControllerActionFactory#createActions()
	 */
	@Override
	public void createActions() throws GkException {
		add(new G2CoreFeedHoldAction(controllerService));
		add(new G2CoreKillAlarmAction(controllerService));
		add(new G2CoreResetAction(controllerService));
		add(new G2CoreResetZeroAction(controllerService));
		add(new G2CoreSpindleOffAction(controllerService));
		add(new G2CoreSpindleOnCwAction(controllerService));
		add(new G2CoreStartJogAction(controllerService));
		add(new G2CoreStopAction(controllerService));
		add(new G2CoreStopJogAction(controllerService));
		add(new G2CoreHomingAction(controllerService));
	}

}
