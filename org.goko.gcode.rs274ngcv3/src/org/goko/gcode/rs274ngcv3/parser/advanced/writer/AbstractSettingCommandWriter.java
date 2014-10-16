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

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.gcode.bean.commands.SettingCommand;

public abstract class AbstractSettingCommandWriter<T extends SettingCommand> extends AbstractCommentCommandWriter<T> {

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.writer.AbstractCommentCommandWriter#write(java.lang.String, org.goko.core.gcode.bean.commands.CommentCommand)
	 */
	@Override
	protected String write(String base, T command) throws GkException {
		String strCommand = super.write(base, command);
		strCommand = writeDistanceMode(strCommand, command);
		strCommand = writeUnit(strCommand, command);
		strCommand = writeMotionMode(strCommand, command);
		strCommand = writeFeedrate(strCommand, command);
		return strCommand;
	}

	private String writeDistanceMode(String strCommand, T command) throws GkException{
		if(command.isExplicitDistanceMode()){
			if(command.getDistanceMode() == EnumGCodeCommandDistanceMode.ABSOLUTE){
				strCommand += " G90";
			}else{
				strCommand += " G91";
			}
		}
		return strCommand;
	}

	protected String writeUnit(String strCommand, T command) throws GkException{
		if(command.isExplicitUnit()){
			if(command.getUnit() == EnumGCodeCommandUnit.INCHES){
				strCommand += " G20";
			}else{
				strCommand += " G21";
			}
		}
		return strCommand;
	}

	protected String writeMotionMode(String strCommand, T command) throws GkException{
		if(command.isExplicitMotionMode()){
			if(command.getMotionMode() == EnumGCodeCommandMotionMode.ARC_CLOCKWISE){
				strCommand += " G2";
			}else if(command.getMotionMode() == EnumGCodeCommandMotionMode.ARC_COUNTERCLOCKWISE){
				strCommand += " G3";
			}else if(command.getMotionMode() == EnumGCodeCommandMotionMode.FEEDRATE){
				strCommand += " G1";
			}else{
				strCommand += " G0";
			}
		}
		return strCommand;
	}

	protected String writeFeedrate(String strCommand, T command) throws GkException{
		if(command.isExplicitFeedrate()){
			strCommand += " F"+command.getFeedrate();
		}
		return strCommand;
	}
}
