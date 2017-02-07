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
package org.goko.controller.tinyg.commons.action;

import org.goko.controller.tinyg.commons.ITinyGControllerService;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.controller.action.IGkControllerAction;

/**
 * Abstract TinyG action
 *
 * @author PsyKo
 *
 */
public abstract class AbstractTinyGControllerAction<C extends AbstractTinyGConfiguration<C>, S extends ITinyGControllerService<?>> implements IGkControllerAction {
	/**
	 * TinyG controller service
	 */
	private S controllerService;

	/**
	 * @param controllerService
	 */
	public AbstractTinyGControllerAction( S controllerService) {
		super();
		this.controllerService = controllerService;
	}

	/**
	 * @return the controllerService
	 */
	protected S getControllerService() {
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

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#getCode()
	 */
	@Override
	public String getCode() {
		return getId();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) { }

}
