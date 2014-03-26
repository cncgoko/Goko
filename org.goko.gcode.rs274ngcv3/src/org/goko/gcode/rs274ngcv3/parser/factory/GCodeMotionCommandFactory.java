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
package org.goko.gcode.rs274ngcv3.parser.factory;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.gcode.rs274ngcv3.parser.factory.builder.CCWArcMotionCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.factory.builder.CWArcMotionCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.factory.builder.FeedrateMotionCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.factory.builder.RapidLinearMotionCommandBuilder;

public class GCodeMotionCommandFactory {
	private List<AbstractGCodeMotionCommandBuilder<?>> builders;

	public GCodeMotionCommandFactory(){
		this.builders = new ArrayList<AbstractGCodeMotionCommandBuilder<?>>();
		this.builders.add( new CWArcMotionCommandBuilder());
		this.builders.add( new CCWArcMotionCommandBuilder());
		this.builders.add( new RapidLinearMotionCommandBuilder());
		this.builders.add( new FeedrateMotionCommandBuilder());
	}

	public GCodeCommand buildCommand(GCodeCommand baseCommand, GCodeWord motionWord) throws GkException{
		for (AbstractGCodeMotionCommandBuilder<?> builder : builders) {
			if(builder.getDefiningWord().equals(motionWord)){
				GCodeCommand command = builder.buildCommand(baseCommand);
				command.setComment(baseCommand.getComment());
				command.setLineNumber(baseCommand.getLineNumber());
				command.setGCodeWords(baseCommand.getGCodeWords());
				command.setState(baseCommand.getState());
				return command;
			}
		}
		return null;
	}
}
