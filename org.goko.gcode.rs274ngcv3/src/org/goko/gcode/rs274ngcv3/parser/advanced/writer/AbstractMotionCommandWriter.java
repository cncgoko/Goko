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

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.gcode.bean.commands.MotionCommand;

public abstract class AbstractMotionCommandWriter<T extends MotionCommand> extends AbstractSettingCommandWriter<T>{

	@Override
	protected String write(String base, T command) throws GkException {
		String strCommand = writeCoordinate(StringUtils.EMPTY, "X", command.getCoordinates().getX(), command.getUnit());
		strCommand = writeCoordinate(strCommand, "Y", command.getCoordinates().getY(), command.getUnit());
		strCommand = writeCoordinate(strCommand, "Z", command.getCoordinates().getZ(), command.getUnit());
		strCommand = writeCoordinate(strCommand, "A", command.getCoordinates().getA(), command.getUnit());
		strCommand = writeCoordinate(strCommand, "B", command.getCoordinates().getB(), command.getUnit());
		strCommand = writeCoordinate(strCommand, "C", command.getCoordinates().getC(), command.getUnit());
		strCommand += base;
		return super.write(strCommand, command);
	}

	protected String writeCoordinate(String base, String letter, BigDecimal value, EnumGCodeCommandUnit unit) throws GkException{
		if(value != null){
			BigDecimal tmpVal = value;
			if(unit == EnumGCodeCommandUnit.INCHES){
				//tmpVal = value.divide(new BigDecimal("25.4"));
			}
			if(StringUtils.isEmpty(base)){
				base += letter + tmpVal;
			}else{
				base += " "+ letter + tmpVal;
			}
		}
		return base;
	}
}
