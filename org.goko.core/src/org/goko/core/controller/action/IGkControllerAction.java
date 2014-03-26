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
package org.goko.core.controller.action;

import org.goko.core.common.exception.GkException;

/**
 * Interface describing a controller action
 * @author PsyKo
 *
 */
public interface IGkControllerAction {
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
