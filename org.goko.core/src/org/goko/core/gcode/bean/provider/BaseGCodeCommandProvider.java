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
package org.goko.core.gcode.bean.provider;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeCommandProvider;

/**
 * Basic command provider
 *
 * @author PsyKo
 *
 */
public class BaseGCodeCommandProvider implements IGCodeCommandProvider{
	/** The commands */
	private List<GCodeCommand> lstCommands;

	public BaseGCodeCommandProvider(GCodeCommand command){
		lstCommands = new ArrayList<GCodeCommand>();
		lstCommands.add(command);
	}

	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return lstCommands;
	}

}
