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
package org.goko.core.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;

public interface IThreeAxisControllerAdapter {

	/**
	 * Returns the X position of the controller
	 * @return the current X position
	 */
	Length getX() throws GkException;
	/**
	 * Returns the Y position of the controller
	 * @return the current Y position
	 */
	Length getY() throws GkException;
	/**
	 * Returns the Z position of the controller
	 * @return the current Z position
	 */
	Length getZ() throws GkException;
}
