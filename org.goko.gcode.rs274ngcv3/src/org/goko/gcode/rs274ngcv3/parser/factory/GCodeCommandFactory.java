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
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.ModalGroup;

/**
 * A factory for command creation
 *
 * @author PsyKo
 *
 */
public class GCodeCommandFactory {
	private List<ModalGroup> modalGroups;
	private ModalGroup motionGroup;

	public GCodeCommandFactory() {
		this.modalGroups = new ArrayList<ModalGroup>();
		this.motionGroup = new ModalGroup("G0", "G1", "G2", "G3", "G38.2", "G80", "G81", "G82", "G83", "G84", "G85", "G86", "G87", "G88", "G89"  );
		this.modalGroups.add( motionGroup );
		this.modalGroups.add( new ModalGroup("G17", "G18", "G19"  ));
		this.modalGroups.add( new ModalGroup("G90", "G91"));
		this.modalGroups.add( new ModalGroup("G93", "G94"));
		this.modalGroups.add( new ModalGroup("G20", "G21"));
		this.modalGroups.add( new ModalGroup("G40", "G41", "G42"));
		this.modalGroups.add( new ModalGroup("G43","G49"));
		this.modalGroups.add( new ModalGroup("G98","G99"));
		this.modalGroups.add( new ModalGroup("G54", "G55", "G56", "G57", "G58", "G59", "G59.1", "G59.2", "G59.3"));
		this.modalGroups.add( new ModalGroup("G61", "G61.1", "G64"));

		this.modalGroups.add( new ModalGroup("M0", "M1", "M2", "M30", "M60"));
		this.modalGroups.add( new ModalGroup("M6"));
		this.modalGroups.add( new ModalGroup("M3","M4","M5"));
		this.modalGroups.add( new ModalGroup("M7","M9"));
		this.modalGroups.add( new ModalGroup("M8","M9"));
		this.modalGroups.add( new ModalGroup("M48", "M49"));
	}

	public GCodeCommand create(List<GCodeToken> tokens) throws GkException{
		GCodeCommand command = createBaseCommand(tokens);
		verifyModality(command);

		return command;
	}

	protected GCodeWord findMotionWord(GCodeCommand command){
		for (GCodeWord word : command.getGCodeWords()) {
			if(motionGroup.contains(word)){
				return word;
			}
		}
		return null;
	}



	protected GCodeCommand createBaseCommand(List<GCodeToken> tokens) throws GkException{
		GCodeCommand command = new GCodeCommand();
		command.setState(new GCodeCommandState(GCodeCommandState.NONE));
		for (GCodeToken gCodeToken : tokens) {
			switch(gCodeToken.getType()){
			case LINE_NUMBER: command.setLineNumber(gCodeToken.getValue());
				break;
			case SIMPLE_COMMENT: command.setComment(gCodeToken.getValue());
				break;
			case WORD: command.addGCodeWord(new GCodeWord(gCodeToken.getValue()));
				break;
			case MULTILINE_COMMENT: command.setComment(gCodeToken.getValue());
				break;
				default : throw new GkTechnicalException("Unsupported GCodeToken type '"+gCodeToken.getType()+"'");
			}
		}
		return command;
	}

	public void verifyModality(GCodeCommand command) throws GkException{
		for (ModalGroup group : modalGroups) {
			group.verifyModality(command);
		}
	}
}
