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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandFunctionType;
import org.goko.core.gcode.bean.commands.FunctionCommand;

public abstract class AbstractFunctionCommandWriter<T extends FunctionCommand> extends AbstractSettingCommandWriter<T>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.writer.AbstractRS274CommandWriter#write(java.lang.String, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	protected String write(String base, T command) throws GkException {
		String strCommand = writeMWord(base, command);
		return super.write(strCommand, command);
	}
	/**
	 * Add the M Word to the command
	 * @param base the base string
	 * @param command the command
	 * @return
	 */
	protected String writeMWord(String base, T command){
		return base + getMWord(command);
	}

	protected String getMWord(T command){
		String words = StringUtils.EMPTY;
		for (EnumGCodeCommandFunctionType functionType : command.getFunctionType()) {
			switch(functionType){
				case OPTIONAL_PROGRAM_STOP        : words += "M1 ";
				break;
				case PROGRAM_END                  : words += "M2 ";
				break;
				case TURN_SPINDLE_CLOCKWISE       : words += "M3 ";
				break;
				case TURN_SPINDLE_COUNTERCLOCKWISE: words += "M4 ";
				break;
				case TURN_SPINDLE_OFF             : words += "M5 ";
				break;
				case TOOL_CHANGE                  : words += "M6 ";
				break;
				case MIST_COOLANTE_ON             : words += "M7 ";
				break;
				case FLOOD_COOLANT_ON             : words += "M8 ";
				break;
				case COOLANT_OFF                  : words += "M9 ";
				break;
			}
		}

		return words;
	}
}
