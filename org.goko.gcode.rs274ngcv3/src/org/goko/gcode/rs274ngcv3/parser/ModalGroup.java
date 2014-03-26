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
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeWord;

/**
 * A class defining a modal group for GCode command.
 * Modal commands are arranged in sets called “modal groups”, and only one member of a modal
 * group may be in force at any given time. In general, a modal group contains commands for which
 * it is logically impossible for two members to be in effect at the same time — like measure in
 * inches vs. measure in millimeters. A machining center may be in many modes at the same time,
 * with one mode from each modal group being in effect.
 *
 * @author PsyKo
 *
 */
public class ModalGroup {
	/**
	 * The list of GCode word inside this group
	 */
	List<GCodeWord> groupWords;

	public ModalGroup(String... words){
		this.groupWords = new ArrayList<GCodeWord>();
		for (String strWord : words) {
			this.groupWords.add(new GCodeWord(strWord));
		}
	}
	public ModalGroup(GCodeWord... words){
		this.groupWords = new ArrayList<GCodeWord>();
		for (GCodeWord gCodeWord : words) {
			this.groupWords.add(gCodeWord);
		}
	}

	public final void verifyModality(GCodeCommand command) throws GkException {
		List<GCodeWord> commandWords = command.getGCodeWords();
		GCodeWord firstWordFromGroup = null;
		for (GCodeWord gCodeWord : commandWords) {
			if(contains(gCodeWord)){
				if(firstWordFromGroup == null){
					firstWordFromGroup = gCodeWord;
				}else{
					throw new GkFunctionalException("GCode modality exception : the word '"+firstWordFromGroup+"' and '"+gCodeWord+"' can't be in the same command.");
				}
			}
		}
	}

	public boolean contains(GCodeWord word){
		return groupWords.contains(word);
	}
}
