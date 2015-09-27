/**
 * 
 */
package org.goko.controller.tinyg.controller.actions;

import org.goko.controller.tinyg.controller.TinyGControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.bean.MachineState;

/**
 * Kill alarm action (mainly soft limits)
 * 
 * @author PsyKo 
 */
public class TinyGKillAlarmAction extends AbstractTinyGControllerAction{

	/**
	 * @param controllerService
	 */
	public TinyGKillAlarmAction(TinyGControllerService controllerService) {
		super(controllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#canExecute()
	 */
	@Override
	public boolean canExecute() throws GkException {
		return MachineState.ALARM.equals(getControllerService().getState());
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
