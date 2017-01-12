/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.controller.action;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.ICodeBean;

/**
 * Interface describing a controller action
 * @author PsyKo
 *
 */
public interface IGkControllerAction extends ICodeBean {
	/**
	 * Determines if the action can be executed at the moment of the call
	 * @return <code>true</code> if the action can be executed, <code>false</code> otherwise
	 * @throws GkException an exception
	 */
	boolean canExecute() throws GkException;
	/**
	 * Executes the action
	 * @param parameters the parameters of execution as String
	 * @throws GkException an exception
	 */
	void execute(Object... parameters) throws GkException;
	/**
	 * Returns the unique identifier of the action
	 * @return a String describing the action
	 */
	String getId();

}
