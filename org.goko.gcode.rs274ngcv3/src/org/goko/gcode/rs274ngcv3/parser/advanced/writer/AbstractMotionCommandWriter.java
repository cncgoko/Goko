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
package org.goko.gcode.rs274ngcv3.parser.advanced.writer;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.MotionCommand;

public abstract class AbstractMotionCommandWriter<T extends MotionCommand> extends AbstractSettingCommandWriter<T>{

	@Override
	protected String write(String base, T command) throws GkException {
		String strCommand = super.write(base, command);
		strCommand = writeCoordinate(strCommand, " X", command.getCoordinates().getX());
		strCommand = writeCoordinate(strCommand, " Y", command.getCoordinates().getY());
		strCommand = writeCoordinate(strCommand, " Z", command.getCoordinates().getZ());
		strCommand = writeCoordinate(strCommand, " A", command.getCoordinates().getA());
		strCommand = writeCoordinate(strCommand, " B", command.getCoordinates().getB());
		strCommand = writeCoordinate(strCommand, " C", command.getCoordinates().getC());
		return strCommand;
	}

	protected String writeCoordinate(String base, String letter, BigDecimal value) throws GkException{
		if(value != null){
			base += letter + value;
		}
		return base;
	}
}
