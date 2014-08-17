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
package org.goko.gcode.rs274ngcv3;

import java.math.BigDecimal;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * {@link GCodeContext} updater that matches the rs274ngcv3 specification
 *
 * @author PsyKo
 *
 */
public class GCodeContextUpdater {

	protected static void updateContext(GCodeContext context, GCodeCommand command) {
		for (GCodeWord word : command.getGCodeWords()) {
			updateContext(context, word);
		}
	}

	private static void updateContext(GCodeContext context, GCodeWord word) {
		switch(word.getLetter()){
		case "G": applyGWord(context,word);
		break;
		case "M": applyMWord(context,word);
		break;
		case "X": updatePosition(context,new Tuple6b(word.getValue(),null,null));
		break;
		case "Y": updatePosition(context,new Tuple6b(null,word.getValue(),null));
		break;
		case "Z": updatePosition(context,new Tuple6b(null,null,word.getValue(),null));
		break;
		case "A": updatePosition(context,new Tuple6b(null,null,null,word.getValue(),null,null));
		break;
		case "B": updatePosition(context,new Tuple6b(null,null,null,null,word.getValue(),null));
		break;
		case "C": updatePosition(context,new Tuple6b(null,null,null,null,null,word.getValue()));
		break;
		case "I": updateOffset(context,new Tuple6b(word.getValue(),null,null));
		break;
		case "J": updateOffset(context,new Tuple6b(null,word.getValue(),null));
		break;
		case "K": updateOffset(context,new Tuple6b(null,null,word.getValue()));
		break;
		case "F": updateFeedrate(context,word.getValue());
		break;
		}
	}

	private static void updateFeedrate(GCodeContext context, BigDecimal value) {
		context.setFeedrate(value);
	}

	private static void updateOffset(GCodeContext context, Tuple6b motion){
		if(context.isAbsolute()){
			context.getOffset().updateAbsolute(motion);
		}else{
			context.getOffset().updateRelative(motion);
		}
	}

	private static void updatePosition(GCodeContext context, Tuple6b motion){
		if(context.isAbsolute()){
			context.getPosition().updateAbsolute(motion);
		}else{
			context.getPosition().updateRelative(motion);
		}
	}

	private static void applyGWord(GCodeContext context, GCodeWord word) {
		switch(word.getStringValue()){
			case "G00": ;
			case "G0": context.setMotionMode(RS274.MOTION_MODE_RAPID);
			break;
			case "G01": ;
			case "G1": context.setMotionMode(RS274.MOTION_MODE_CONTROLLED);
			break;
			case "G02": ;
			case "G2": context.setMotionMode(RS274.MOTION_MODE_ARC_CW);
			break;
			case "G03": ;
			case "G3": context.setMotionMode(RS274.MOTION_MODE_ARC_CCW);
			break;
			case "G17": context.setCurrentPlane(0);
			break;
			case "G18": context.setCurrentPlane(1);
			break;
			case "G19": context.setCurrentPlane(2);
			break;
			case "G20": context.setMetric(false);
			break;
			case "G21": context.setMetric(true);
			break;
			case "G90": context.setAbsolute(true);
			break;
			case "G91": context.setAbsolute(false);
			break;
		}
	}

	private static void applyMWord(GCodeContext context, GCodeWord word) {

	}
}
