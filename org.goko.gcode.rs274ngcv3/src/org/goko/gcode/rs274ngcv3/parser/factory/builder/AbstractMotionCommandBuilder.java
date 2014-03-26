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
package org.goko.gcode.rs274ngcv3.parser.factory.builder;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.gcode.rs274ngcv3.command.GCodeMotionCommand;
import org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder;

public abstract class AbstractMotionCommandBuilder<T extends GCodeMotionCommand > extends AbstractGCodeMotionCommandBuilder<T>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#completeCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void completeCommand(T commandToComplete, GCodeCommand command) throws GkException {
		GCodeWord feedrateWord = findWordByLetter("F", command);
		if(feedrateWord != null){
			commandToComplete.setFeedrate(feedrateWord.getValue());
		}
	}

}
