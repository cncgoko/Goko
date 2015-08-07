/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.tinyg.controller.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.controller.tinyg.controller.TinyGControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.bean.MachineState;

/**
 * Reset zero action for TinyG
 *
 * @author PsyKo
 *
 */
public class TinyGResetZeroAction extends AbstractTinyGControllerAction {

	public TinyGResetZeroAction(TinyGControllerService controllerService) {
		super(controllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#canExecute()
	 */
	@Override
	public boolean canExecute() throws GkException {
		return !ObjectUtils.equals(MachineState.MOTION_RUNNING, getControllerService().getState())
				&& !ObjectUtils.equals(MachineState.UNDEFINED, getControllerService().getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#execute(java.lang.String[])
	 */
	@Override
	public void execute(Object... parameters) throws GkException {
		List<String> lstAxes = new ArrayList<String>();
		if(parameters != null && parameters.length > 0) {
			for (int i = 0 ; i < parameters.length ; i++) {
				if(parameters[i] instanceof String){
					lstAxes.add((String)parameters[i]);
				}
			}
		}
		getControllerService().resetZero(lstAxes);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#getId()
	 */
	@Override
	public String getId() {
		return DefaultControllerAction.RESET_ZERO;
	}

}
