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
import org.goko.gcode.rs274ngcv3.command.RapidLinearMotionGCodeCommand;

public class RapidLinearMotionCommandBuilder extends LinearMotionCommandBuilder<RapidLinearMotionGCodeCommand>{
	protected static final GCodeWord G0_WORD = new GCodeWord("G0");

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#getDefiningWord()
	 */
	@Override
	public GCodeWord getDefiningWord() throws GkException {
		return G0_WORD;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public RapidLinearMotionGCodeCommand buildCommand(GCodeCommand command) throws GkException {
		RapidLinearMotionGCodeCommand g0Command = new RapidLinearMotionGCodeCommand();
		completeCommand(g0Command, command);
		return g0Command;
	}

}
