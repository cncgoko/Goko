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
package org.goko.tinyg.controller.actions;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.controller.action.IGkControllerAction;
import org.goko.tinyg.controller.TinyGControllerService;

/**
 * Abstract TinyG action
 *
 * @author PsyKo
 *
 */
public abstract class AbstractTinyGControllerAction implements IGkControllerAction {
	/**
	 * TinyG controller service
	 */
	private TinyGControllerService controllerService;

	/**
	 * @param controllerService
	 */
	public AbstractTinyGControllerAction( TinyGControllerService controllerService) {
		super();
		this.controllerService = controllerService;
	}

	/**
	 * @return the controllerService
	 */
	protected TinyGControllerService getControllerService() {
		return controllerService;
	}

	protected String getStringParameter(Object object) throws GkException  {
		if(object == null){
			return null;
		}
		if(object instanceof String){
			return (String)object;
		}
		throw new GkFunctionalException("AbstractTinyGControllerAction : Cannot cast object '"+object.toString()+"' to a String...");
	}

}
