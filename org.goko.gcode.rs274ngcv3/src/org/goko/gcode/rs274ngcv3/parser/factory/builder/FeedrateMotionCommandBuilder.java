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
import org.goko.gcode.rs274ngcv3.command.FeedrateLinearMotionGCodeCommand;

public class FeedrateMotionCommandBuilder extends LinearMotionCommandBuilder<FeedrateLinearMotionGCodeCommand>{
	protected static final GCodeWord G01_WORD = new GCodeWord("G1");

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#getDefiningWord()
	 */
	@Override
	public GCodeWord getDefiningWord() throws GkException {
		return G01_WORD;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public FeedrateLinearMotionGCodeCommand buildCommand(GCodeCommand command) throws GkException {
		FeedrateLinearMotionGCodeCommand g0Command = new FeedrateLinearMotionGCodeCommand();
		completeCommand(g0Command, command);
		return g0Command;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.builder.AbstractMotionCommandBuilder#completeCommand(org.goko.gcode.rs274ngcv3.command.GCodeMotionCommand, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void completeCommand(FeedrateLinearMotionGCodeCommand commandToComplete, GCodeCommand command) throws GkException {
		super.completeCommand(commandToComplete, command);
		GCodeWord fWord = findWordByLetter("F", command);
		if(fWord != null){
			commandToComplete.setFeedrate(fWord.getValue());
		}
	};

}
