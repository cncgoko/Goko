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
package org.goko.controller.tinyg.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.goko.controller.tinyg.controller.ITinygControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineState;

public class TinyGPlannerBufferControlHandler {
	private boolean initialized;

	@CanExecute
	public boolean canExecute(MHandledMenuItem menuItem, ITinygControllerService controllerService){
		if(!initialized){
			initialized = true;
			menuItem.setSelected(controllerService.isPlannerBufferSpaceCheck());
		}
		try {
			return !MachineState.UNDEFINED.equals(controllerService.getState());
		} catch (GkException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Execute
	public void execute( MHandledMenuItem menuItem, ITinygControllerService tinyg) throws GkException {
		tinyg.setPlannerBufferSpaceCheck(menuItem.isSelected());
	}
}
