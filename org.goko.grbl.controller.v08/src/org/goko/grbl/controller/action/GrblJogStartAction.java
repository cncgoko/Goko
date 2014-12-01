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
package org.goko.grbl.controller.action;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.bean.MachineState;
import org.goko.grbl.controller.EnumGrblAxis;
import org.goko.grbl.controller.GrblControllerService;

/**
 * Feed hold action for Grbl
 *
 * @author PsyKo
 *
 */
public class GrblJogStartAction extends AbstractGrblControllerAction {

	/**
	 * Constructor
	 * @param controllerService the Grbl service
	 */
	public GrblJogStartAction(GrblControllerService controllerService) {
		super(controllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#canExecute()
	 */
	@Override
	public boolean canExecute() throws GkException {
		return !ObjectUtils.equals(MachineState.UNDEFINED, getControllerService().getState()) && !ObjectUtils.equals(MachineState.ALARM, getControllerService().getState());
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#execute(java.lang.String[])
	 */
	@Override
	public void execute(Object... parameters) throws GkException {
		String 			axis 	 = getStringParameter(parameters[0]);
		EnumGrblAxis 	enumAxis = EnumGrblAxis.getEnum(axis);
		String 			feed 	 = getStringParameter(parameters[1]);
		String 			step 	 = getStringParameter(parameters[2]);
		getControllerService().startJog(enumAxis, feed, step);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#getId()
	 */
	@Override
	public String getId() {
		return DefaultControllerAction.JOG_START;
	}

}
