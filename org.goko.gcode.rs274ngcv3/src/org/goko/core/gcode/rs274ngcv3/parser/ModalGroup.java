/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.rs274ngcv3.parser;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

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

	public final void verifyModality(List<GCodeToken> tokens) throws GkException {
		GCodeToken firstTokenFromGroup = null;
		for (GCodeToken token : tokens) {
			if(contains(token.getValue())){
				if(firstTokenFromGroup == null){
					firstTokenFromGroup = token;			
				}else{					
					throw new GkFunctionalException("GCO-001", firstTokenFromGroup.getValue(), token.getValue());
				}
			}
		}
	}

	public boolean contains(String word){
		return groupWords.contains(word);
	}
}
