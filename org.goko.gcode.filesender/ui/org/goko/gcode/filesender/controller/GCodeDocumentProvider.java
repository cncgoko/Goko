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
package org.goko.gcode.filesender.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Document;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.gcode.filesender.editor.GCodeFileTextRenderer;

public class GCodeDocumentProvider extends Document {
	/** EOL String token */
	private static String EOL = System.getProperty("line.separator");

	private IGCodeProvider gcodeProvider;
	private Map<Integer, Integer> gCodeCommandLine;

	public GCodeDocumentProvider(IGCodeProvider gcodeProvider){
		this.gcodeProvider = gcodeProvider;
		this.gCodeCommandLine = new HashMap<Integer, Integer>();
		init();
	}

	private void init() {
		List<GCodeCommand> lstCommands = gcodeProvider.getGCodeCommands();
		int line = 0;
		StringBuffer buffer = new StringBuffer();
		for (GCodeCommand gCodeCommand : lstCommands) {
			buffer.append(GCodeFileTextRenderer.render(gCodeCommand) );
			buffer.append(EOL);
			gCodeCommandLine.put(gCodeCommand.getId(), line++);
		}
		set(buffer.toString());
	}

	public int getLineForCommand(int idCommand){
		if(gCodeCommandLine.containsKey(idCommand)){
			return gCodeCommandLine.get(idCommand);
		}
		return -1;
	}
}
