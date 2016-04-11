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
package org.goko.controller.grbl.v09.action;

import org.goko.controller.grbl.v09.GrblControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.controller.action.IGkControllerAction;

public abstract class AbstractGrblControllerAction implements IGkControllerAction{
	/**
	 * Grbl controller service
	 */
	private GrblControllerService grblService;

	/**
	 * @param controllerService
	 */
	public AbstractGrblControllerAction( GrblControllerService controllerService) {
		super();
		this.grblService = controllerService;
	}

	/**
	 * @return the controllerService
	 */
	protected GrblControllerService getControllerService() {
		return grblService;
	}

	protected String getStringParameter(Object object) throws GkException  {
		if(object == null){
			return null;
		}
		if(object instanceof String){
			return (String)object;
		}
		throw new GkFunctionalException("AbstractGrblControllerAction : Cannot cast object '"+object.toString()+"' to a String...");
	}

}
