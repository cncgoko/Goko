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
package org.goko.gcode.rs274ngcv3.parser;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.gcode.rs274ngcv3.parser.factory.GCodeCommandFactory;

/**
 * A GCode parser that use lexer token to create GCodeCommands
 *
 * @author PsyKo
 *
 */
public class GCodeParser {
	private GCodeCommandFactory factory;
	private int currentId = 1;

	public GCodeParser() {
		factory = new GCodeCommandFactory();
	}

	public List<GCodeCommand> createGCodeCommand(List<GCodeToken> tokens) throws GkException{
		List<GCodeCommand> lstGCodeCommand = new ArrayList<GCodeCommand>();
		List<GCodeToken> tmpLstTokens = new ArrayList<GCodeToken>();
		// Let's detect the end token. We search the first NEW_LINE token, or the end of the list
		for (GCodeToken gCodeToken : tokens) {
			if(gCodeToken.getType() == GCodeTokenType.NEW_LINE){
				GCodeCommand command = factory.create(tmpLstTokens);
				command.setId(currentId++);
				command.setLineNumber(String.valueOf(currentId));
				lstGCodeCommand.add( command ); //createAtomGCodeCommand(tmpLstTokens) );
				tmpLstTokens.clear();
			}else{
				tmpLstTokens.add(gCodeToken);
			}
		}
		// Let's compute the final GCodeCommand
		GCodeCommand command = factory.create(tmpLstTokens);
		command.setId(currentId++);
		command.setLineNumber(String.valueOf(currentId));
		lstGCodeCommand.add( command );
		return lstGCodeCommand;
	}

}
