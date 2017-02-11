/**
 * 
 */
package org.goko.controller.g2core.controller.action;

import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.g2core.controller.G2CoreControllerService;
import org.goko.controller.tinyg.commons.action.AbstractTinyGControllerAction;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.DefaultControllerAction;

/**
 * Kill alarm action (mainly soft limits)
 * 
 * @author PsyKo 
 */
public class G2CoreKillAlarmAction extends AbstractTinyGControllerAction<G2CoreConfiguration, G2CoreControllerService>{

	/**
	 * @param controllerService
	 */
	public G2CoreKillAlarmAction(G2CoreControllerService controllerService) {
		super(controllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#canExecute()
	 */
	@Override
	public boolean canExecute() throws GkException {
		return G2Core.State.ALARM.equals(getControllerService().getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#execute(java.lang.Object[])
	 */
	@Override
	public void execute(Object... parameters) throws GkException {
		getControllerService().killAlarm();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#getId()
	 */
	@Override
	public String getId() {
		return DefaultControllerAction.KILL_ALARM;
	}

}
