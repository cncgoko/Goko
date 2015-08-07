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

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.controller.tinyg.controller.ITinygControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineState;

public class TinyGConfigurationRefreshHandler {

	@Execute
	@Optional
	public void execute(ITinygControllerService tinyg) throws GkException {
		if(!ObjectUtils.equals(MachineState.UNDEFINED,tinyg.getState())){
			tinyg.refreshConfiguration();
		}
	}

}
