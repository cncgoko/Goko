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
package org.goko.core.rs274ngcv3.parser.advanced.writer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;

public class LinearCommandWriter extends AbstractMotionCommandWriter<LinearMotionCommand> {

	/** (inheritDoc)
	 * @see org.goko.core.rs274ngcv3.parser.advanced.writer.AbstractRS274CommandWriter#write(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public String write(LinearMotionCommand command) throws GkException {
		return addComment(super.write(StringUtils.EMPTY, command), command);
	}

}
