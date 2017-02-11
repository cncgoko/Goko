/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.g2core.controller.action;

import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.controller.G2CoreControllerService;
import org.goko.controller.tinyg.commons.action.AbstractTinyGControllerAction;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.DefaultControllerAction;

/**
 * Implementation of the Reset action for G2Core
 * @author PsyKo
 *
 */
public class G2CoreResetAction extends AbstractTinyGControllerAction<G2CoreConfiguration, G2CoreControllerService>{
	/**
	 * Constructor
	 * @param controllerService the Grbl service
	 */
	public G2CoreResetAction(G2CoreControllerService controllerService) {
		super(controllerService);
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#canExecute()
	 */
	@Override
	public boolean canExecute() throws GkException {
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#execute(java.lang.String[])
	 */
	@Override
	public void execute(Object... parameters) throws GkException {		
		getControllerService().resetTinyG();		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.IGkControllerAction#getId()
	 */
	@Override
	public String getId() {
		return DefaultControllerAction.HARD_RESET;
	}

}
