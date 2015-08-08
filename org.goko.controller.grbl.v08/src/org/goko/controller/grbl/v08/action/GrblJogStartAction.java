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
package org.goko.controller.grbl.v08.action;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.controller.grbl.v08.GrblControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.action.DefaultControllerAction;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;

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
		EnumControllerAxis 	enumAxis = EnumControllerAxis.getEnum(axis);
		BigDecimal 			feed 	 = new BigDecimal(getStringParameter(parameters[1]));
		Unit<Length> unit = getControllerService().getCurrentGCodeContext().getUnit().getUnit();
		BigDecimalQuantity<Length>	step 	 = NumberQuantity.of(new BigDecimal(getStringParameter(parameters[2])), unit);
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
