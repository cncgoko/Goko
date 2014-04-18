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
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.rs274ngcv3.command.GCodeArcCommand;

public class CWArcMotionCommandBuilder extends AbstractMotionCommandBuilder<GCodeArcCommand>{
	protected static final GCodeWord G02_WORD = new GCodeWord("G2");
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#getDefiningWord()
	 */
	@Override
	public GCodeWord getDefiningWord() throws GkException {
		return G02_WORD;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public GCodeArcCommand buildCommand(GCodeCommand command) throws GkException {
		GCodeArcCommand arcCommand = new GCodeArcCommand(true);
		completeCommand(arcCommand, command);
		return arcCommand;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.factory.AbstractGCodeMotionCommandBuilder#completeCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void completeCommand(GCodeArcCommand commandToComplete, GCodeCommand command) throws GkException {
		super.completeCommand(commandToComplete, command);
// TODO : support for plane selection
		Tuple6b endpoint = new Tuple6b(null,null,null,null,null,null);

		GCodeWord endPointXWord = findWordByLetter("X", command);
		if(endPointXWord != null){
			endpoint.setX(endPointXWord.getValue());
		}
		GCodeWord endPointYWord = findWordByLetter("Y", command);
		if(endPointYWord != null){
			endpoint.setY(endPointYWord.getValue());
		}
		GCodeWord endPointZWord = findWordByLetter("Z", command);
		if(endPointZWord != null){
			endpoint.setZ(endPointZWord.getValue());
		}
		GCodeWord endPointAWord = findWordByLetter("A", command);
		if(endPointAWord != null){
			endpoint.setA(endPointAWord.getValue());
		}
		GCodeWord endPointBWord = findWordByLetter("B", command);
		if(endPointBWord != null){
			endpoint.setB(endPointBWord.getValue());
		}
		GCodeWord endPointCWord = findWordByLetter("C", command);
		if(endPointCWord != null){
			endpoint.setC(endPointCWord.getValue());
		}
		commandToComplete.setEndpoint(endpoint);

		GCodeWord radiusWord = findWordByLetter("R", command);
		if(radiusWord != null){
			commandToComplete.setRadius(radiusWord.getValue());
		}
		GCodeWord offsetIWord = findWordByLetter("I", command);
		if(offsetIWord != null){
			commandToComplete.setOffsetI(offsetIWord.getValue());
		}
		GCodeWord offsetJWord = findWordByLetter("J", command);
		if(offsetJWord != null){
			commandToComplete.setOffsetJ(offsetJWord.getValue());
		}
		GCodeWord offsetKWord = findWordByLetter("K", command);
		if(offsetKWord != null){
			commandToComplete.setOffsetK(offsetKWord.getValue());
		}
	}

}
