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
package org.goko.gcode.rs274ngcv3.parser.advanced.writer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoConfig;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;

public class ArcCommandWriter extends AbstractMotionCommandWriter<ArcMotionCommand>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.writer.AbstractRS274CommandWriter#write(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public String write(ArcMotionCommand command) throws GkException {
		String str = super.write(StringUtils.EMPTY, command);
		return addComment(writeOffsets(str, command), command);
	}

	/**
	 * Writes I,J and K offsets
	 * @param str the base string
	 * @param command the command
	 * @throws GkException GkException
	 */
	private String writeOffsets(String str, ArcMotionCommand command) throws GkException {
		Tuple6b offsets = command.getAbsoluteCenterCoordinate().subtract(command.getAbsoluteStartCoordinate());
		Unit<Length> unit = command.getUnit().getUnit();
		if(offsets.getX() != null ){
			str += " I"+ GokoConfig.getInstance().format(offsets.getX().to(unit), false, false);
		}
		if(offsets.getY() != null){
			str += " J"+GokoConfig.getInstance().format(offsets.getY().to(unit), false, false);
		}

		if(offsets.getZ() != null &&  Math.abs(offsets.getZ().doubleValue()) > 0.00001){ // Ugly hack to avoid having K with zero close value
			str += " K"+GokoConfig.getInstance().format(offsets.getZ().to(unit), false, false);
		}
		return str;
	}

}
