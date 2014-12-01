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
package org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.abstracts.CompleteTokenCommandModifier;

public class G56Modifier extends CompleteTokenCommandModifier<SettingCommand>{

	public G56Modifier() {
		super("G56");
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.tokens.abstracts.TokenCommandModifier#apply(org.goko.gcode.rs274ngcv3.parser.GCodeToken, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void apply(GCodeToken token, SettingCommand command) throws GkException {
		command.setCoordinateSystem(EnumCoordinateSystem.G56);
		command.setExplicitcoordinateSystem(true);
	}

}
