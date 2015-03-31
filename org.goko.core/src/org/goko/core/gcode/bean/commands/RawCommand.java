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
package org.goko.core.gcode.bean.commands;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;

public class RawCommand extends GCodeCommand {

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#accept(org.goko.core.gcode.bean.IGCodeCommandVisitor)
	 */
	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		visitor.visit(this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#updateContext(org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public void updateContext(GCodeContext context) {
		// Nothing to do in raw method
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#initFromContext(org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public void initFromContext(GCodeContext context) {
		// Nothing to do in raw method
	}

}
