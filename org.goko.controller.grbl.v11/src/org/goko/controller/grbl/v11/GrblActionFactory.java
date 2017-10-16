/**
 * 
 */
package org.goko.controller.grbl.v11;

import org.goko.controller.grbl.v11.actions.GrblFeedHoldAction;
import org.goko.controller.grbl.v11.actions.GrblHomeAction;
import org.goko.controller.grbl.v11.actions.GrblJogStartAction;
import org.goko.controller.grbl.v11.actions.GrblJogStopAction;
import org.goko.controller.grbl.v11.actions.GrblKillAlarmAction;
import org.goko.controller.grbl.v11.actions.GrblResetAction;
import org.goko.controller.grbl.v11.actions.GrblResetZeroAction;
import org.goko.controller.grbl.v11.actions.GrblSpindleOffAction;
import org.goko.controller.grbl.v11.actions.GrblSpindleOnCcwAction;
import org.goko.controller.grbl.v11.actions.GrblSpindleOnCwAction;
import org.goko.controller.grbl.v11.actions.GrblStartAction;
import org.goko.controller.grbl.v11.actions.GrblStopAction;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.ControllerActionFactory;

/**
 * @author Psyko
 * @date 5 avr. 2017
 */
public class GrblActionFactory extends ControllerActionFactory {
	/** The target controller service */
	private GrblControllerService grblControllerService;
	
	/**
	 * @param grblControllerService
	 */
	public GrblActionFactory(GrblControllerService grblControllerService) {
		super();
		this.grblControllerService = grblControllerService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.action.ControllerActionFactory#createActions()
	 */
	@Override
	public void createActions() throws GkException {
		add( new GrblFeedHoldAction(grblControllerService));
		add( new GrblHomeAction(grblControllerService));
		add( new GrblJogStartAction(grblControllerService));
		add( new GrblJogStopAction(grblControllerService));
		add( new GrblKillAlarmAction(grblControllerService));
		add( new GrblResetAction(grblControllerService));
		add( new GrblResetZeroAction(grblControllerService));
		add( new GrblStartAction(grblControllerService));
		add( new GrblStopAction(grblControllerService));
		add( new GrblSpindleOnCwAction(grblControllerService));
		add( new GrblSpindleOnCcwAction(grblControllerService));
		add( new GrblSpindleOffAction(grblControllerService));
	}

}
