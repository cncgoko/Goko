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
	List<String> groupWords;

	public ModalGroup(String... words){
		this.groupWords = new ArrayList<String>();
		for (String strWord : words) {
			this.groupWords.add(strWord);
		}
	}

	public final void verifyTokenModality(List<GCodeToken> tokens) throws GkException {		
		List<String> words = new ArrayList<String>();
		for (GCodeToken gCodeToken : tokens) {
			words.add(gCodeToken.getValue());
		}
		verifyStringModality(words);
	}
	
	public final void verifyStringModality(List<String> words) throws GkException {		
		String firstWordFromGroup = null;
		for (String word : words) {
			if(contains(word)){
				if(firstWordFromGroup == null){
					firstWordFromGroup = word;
				}else{
					throw new GkFunctionalException("GCode modality exception : the word '"+firstWordFromGroup+"' and '"+word+"' can't be in the same command ["+words.toString()+"].");
				}
			}
		}
	}

	public boolean contains(String word){
		return groupWords.contains(word);
	}
}
