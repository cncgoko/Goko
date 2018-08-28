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
package org.goko.core.viewer.renderer;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IRendererProxy {
	public static final int ARC_CLOCKWISE = 0x01;
	public static final int ARC_COUNTERCLOCKWISE = 0x02;

	void drawGCode(IGCodeProvider gcodeProvider) throws GkException;

	void erase(Integer id) throws GkException;
}
