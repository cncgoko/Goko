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
package org.goko.gcode.rs274ngcv3.parser.advanced;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandType;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.gcode.rs274ngcv3.GkGCodeService;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.ArcCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.CommentCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.LinearCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.SettingCommandWriter;

public class RS274CommandWriter {

	public <T extends GCodeCommand> String write(T command) throws GkException{
		String result = StringUtils.EMPTY;
		if(command.getType() == EnumGCodeCommandType.COMMENT){
			result = new CommentCommandWriter().write((CommentCommand) command);
		}else if(command.getType() == EnumGCodeCommandType.SETTING){
			result = new SettingCommandWriter().write((SettingCommand) command);
		}else if(command.getType() == EnumGCodeCommandType.MOTION){
			MotionCommand motionCommand = (MotionCommand) command;
			if(motionCommand.getMotionType() == EnumGCodeCommandMotionType.LINEAR){
				result = new LinearCommandWriter().write((LinearMotionCommand) command);
			}else{
				result = new ArcCommandWriter().write((ArcMotionCommand) command);
			}
		}else{
			result = command.getStringCommand();
		}
		return StringUtils.trim(result);
	}

	public static void main(String[] args) {
		GkGCodeService gcodeService = new GkGCodeService();
		RS274CommandWriter writer = new RS274CommandWriter();
		try {
			System.out.println(writer.write( gcodeService.parseCommand("G0X15Y20Z56")));
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
