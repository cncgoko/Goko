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
package org.goko.gcode.rs274ngcv3.parser.advanced.builders;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.GCodeTokenType;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.FModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.SModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G0Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G17Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G18Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G19Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G1Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G20Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G21Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G2Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G3Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G53Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G54Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G55Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G56Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G57Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G58Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G90Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G91Modifier;

public abstract class AbstractSettingCommandBuilder<T extends SettingCommand> extends AbstractCommentCommandBuilder<T> {


	public AbstractSettingCommandBuilder() {
		super();
		addModifier(new G90Modifier());
		addModifier(new G91Modifier());

		addModifier(new G0Modifier());
		addModifier(new G1Modifier());
		addModifier(new G2Modifier());
		addModifier(new G3Modifier());

		addModifier(new G17Modifier());
		addModifier(new G18Modifier());
		addModifier(new G19Modifier());

		addModifier(new G20Modifier());
		addModifier(new G21Modifier());

		addModifier(new G53Modifier());
		addModifier(new G54Modifier());
		addModifier(new G55Modifier());
		addModifier(new G56Modifier());
		addModifier(new G57Modifier());
		addModifier(new G58Modifier());


		addModifier(new SModifier());
		addModifier(new FModifier());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractCommentCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		for (GCodeToken gCodeToken : lstTokens) {
			if(gCodeToken.getType() != GCodeTokenType.MULTILINE_COMMENT && gCodeToken.getType() != GCodeTokenType.SIMPLE_COMMENT){
				String letter = RS274.getTokenLetter(gCodeToken);
				if( !StringUtils.equalsIgnoreCase( letter ,"g") &&
					!StringUtils.equalsIgnoreCase( letter ,"n") &&
					!StringUtils.equalsIgnoreCase( letter ,"f")){
					return false;
				}
			}
		}
		return true;
	}
}


//
///**
// * Gather the setting commands from the rawCommand
// * @param rawCommand the unspecified command
// * @param context the current context
// * @param targetCommand the target command to build
// * @throws GkException GkException
// */
//protected void buildSettingsParameters(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	setDistanceModeMode(lstTokens, context, targetCommand);
//	setUnit(lstTokens, context, targetCommand);
//	setFeedrateAndMotionMode(lstTokens, context, targetCommand);
//	setToolNumber(lstTokens, context, targetCommand);
//	setCurrentPlane(lstTokens, context, targetCommand);
//
//}
//
///** (inheritDoc)
// * @see org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext)
// */
//@Override
//public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
//	super.buildCommand(lstTokens, context, targetCommand);
//	buildSettingsParameters(lstTokens, context, targetCommand);
//}
//
///**
// * Extracts Feedrate and Motion mode from the given raw command
// * @param rawCommand the unspecified command
// * @param context the current context
// * @param targetCommand the target command to build
// * @throws GkException GkException
// */
//protected void setFeedrateAndMotionMode(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	targetCommand.setExplicitMotionMode(true);
//	if(RS274.removeToken(RS274.MOTION_MODE_RAPID, lstTokens) != null || RS274.removeToken(RS274.MOTION_MODE_RAPID_EXTENDED, lstTokens) != null ){
//		targetCommand.setMotionMode(EnumGCodeCommandMotionMode.RAPID);
//
//	}else if (RS274.removeToken(RS274.MOTION_MODE_CONTROLLED, lstTokens) != null ||  RS274.removeToken(RS274.MOTION_MODE_CONTROLLED_EXTENDED, lstTokens) != null ){
//		targetCommand.setMotionMode(EnumGCodeCommandMotionMode.FEEDRATE);
//
//	}else if(RS274.removeToken(RS274.MOTION_MODE_ARC_CW, lstTokens) != null ||  RS274.removeToken(RS274.MOTION_MODE_ARC_CW_EXTENDED, lstTokens) != null ){
//		targetCommand.setMotionMode(EnumGCodeCommandMotionMode.ARC_CLOCKWISE);
//
//	}else if (RS274.removeToken(RS274.MOTION_MODE_ARC_CCW, lstTokens) != null || RS274.removeToken(RS274.MOTION_MODE_ARC_CCW_EXTENDED, lstTokens) != null ){
//		targetCommand.setMotionMode(EnumGCodeCommandMotionMode.ARC_COUNTERCLOCKWISE);
//
//	}else{
//		targetCommand.setMotionMode(context.getMotionMode());
//		targetCommand.setExplicitMotionMode(false);
//	}
//
//	// Extract the feedrate if the motion is anything but rapid
//	GCodeToken fToken = RS274.findUniqueTokenByLetter("F", lstTokens);
//	BigDecimal scale = new BigDecimal("1");
//	if(fToken != null){
//		if(targetCommand.getUnit() == EnumGCodeCommandUnit.INCHES){
//			scale = new BigDecimal("25.4");
//		}
//		lstTokens.remove(fToken);
//		targetCommand.setFeedrate( new BigDecimal(RS274.getTokenValue(fToken)).multiply(scale) );
//		targetCommand.setExplicitFeedrate(true);
//	}else{
//		targetCommand.setFeedrate( context.getFeedrate());
//	}
//}
//
//
//private void setCurrentPlane(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	targetCommand.setPlane(context.getPlane());
//	targetCommand.setExplicitPlane(false);
//	GCodeToken token = RS274.removeToken("G17", lstTokens);
//	if(token != null){
//		targetCommand.setPlane(EnumGCodeCommandPlane.XY_PLANE);
//		targetCommand.setExplicitPlane(true);
//	}
//
//	token = RS274.removeToken("G18", lstTokens);
//	if(token != null){
//		targetCommand.setPlane(EnumGCodeCommandPlane.XZ_PLANE);
//		targetCommand.setExplicitPlane(true);
//	}
//
//	token = RS274.removeToken("G19", lstTokens);
//	if(token != null){
//		targetCommand.setPlane(EnumGCodeCommandPlane.YZ_PLANE);
//		targetCommand.setExplicitPlane(true);
//	}
//
//}
//
///**
// * Extracts Distance mode from the given raw command
// * @param rawCommand the unspecified command
// * @param context the current context
// * @param targetCommand the target command to build
// * @throws GkException GkException
// */
//protected void setDistanceModeMode(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	targetCommand.setDistanceMode(context.getDistanceMode());
//	targetCommand.setExplicitDistanceMode(false);
//
//	if( RS274.removeToken("G90", lstTokens) != null){
//
//		targetCommand.setDistanceMode(EnumGCodeCommandDistanceMode.ABSOLUTE);
//		targetCommand.setExplicitDistanceMode(true);
//
//	}else if (RS274.removeToken("G91", lstTokens) != null){
//		targetCommand.setDistanceMode(EnumGCodeCommandDistanceMode.RELATIVE);
//		targetCommand.setExplicitDistanceMode(true);
//	}
//
//}
//
///**
// * Extracts Units from the given raw command
// * @param rawCommand the unspecified command
// * @param context the current context
// * @param targetCommand the target command to build
// * @throws GkException GkException
// */
//protected void setUnit(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	if(RS274.removeToken("G20", lstTokens) != null){
//		targetCommand.setUnit(EnumGCodeCommandUnit.INCHES);
//		targetCommand.setExplicitUnit(true);
//
//	}else if (RS274.removeToken("G21", lstTokens) != null){
//		targetCommand.setUnit(EnumGCodeCommandUnit.MILLIMETERS);
//		targetCommand.setExplicitUnit(true);
//
//	}else{
//		targetCommand.setUnit(context.getUnit());
//		targetCommand.setExplicitUnit(false);
//	}
//}
//
///**
// * Extracts Tool Number from the given raw command
// * @param rawCommand the unspecified command
// * @param context the current context
// * @param targetCommand the target command to build
// * @throws GkException GkException
// */
//protected void setToolNumber(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
//	GCodeToken toolToken = RS274.findUniqueTokenByLetter("T", lstTokens);
//	if(toolToken != null){
//		String toolNumber = RS274.getTokenValue(toolToken);
//		targetCommand.setToolNumber(Integer.valueOf(toolNumber));
//		targetCommand.setExplicitToolNumber(true);
//		lstTokens.remove(toolToken);
//	}else{
//		targetCommand.setToolNumber(context.getToolNumber());
//		targetCommand.setExplicitToolNumber(false);
//	}
//
//}
