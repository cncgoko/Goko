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

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.GCodeTokenType;

public abstract class AbstractSettingCommandBuilder<T extends SettingCommand> extends AbstractCommentCommandBuilder<T> {

	/**
	 * Gather the setting commands from the rawCommand
	 * @param rawCommand the unspecified command
	 * @param context the current context
	 * @param targetCommand the target command to build
	 * @throws GkException GkException
	 */
	protected void buildSettingsParameters(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		setDistanceModeMode(lstTokens, context, targetCommand);
		setUnit(lstTokens, context, targetCommand);
		setFeedrateAndMotionMode(lstTokens, context, targetCommand);
		// TODO : add plane selectionqsdqs
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
		super.buildCommand(lstTokens, context, targetCommand);
		buildSettingsParameters(lstTokens, context, targetCommand);
	}

	/**
	 * Extracts Feedrate and Motion mode from the given raw command
	 * @param rawCommand the unspecified command
	 * @param context the current context
	 * @param targetCommand the target command to build
	 * @throws GkException GkException
	 */
	protected void setFeedrateAndMotionMode(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		targetCommand.setExplicitMotionMode(true);
		if(RS274.isToken(RS274.MOTION_MODE_RAPID, lstTokens)|| RS274.isToken(RS274.MOTION_MODE_RAPID_EXTENDED, lstTokens) ){
			targetCommand.setMotionMode(EnumGCodeCommandMotionMode.RAPID);

		}else if (RS274.isToken(RS274.MOTION_MODE_CONTROLLED, lstTokens) ||  RS274.isToken(RS274.MOTION_MODE_CONTROLLED_EXTENDED, lstTokens) ){
			targetCommand.setMotionMode(EnumGCodeCommandMotionMode.FEEDRATE);

		}else if(RS274.isToken(RS274.MOTION_MODE_ARC_CW, lstTokens) ||  RS274.isToken(RS274.MOTION_MODE_ARC_CW_EXTENDED, lstTokens)){
			targetCommand.setMotionMode(EnumGCodeCommandMotionMode.ARC_CLOCKWISE);

		}else if (RS274.isToken(RS274.MOTION_MODE_ARC_CCW, lstTokens) || RS274.isToken(RS274.MOTION_MODE_ARC_CCW_EXTENDED, lstTokens)){
			targetCommand.setMotionMode(EnumGCodeCommandMotionMode.ARC_COUNTERCLOCKWISE);

		}else{
			targetCommand.setMotionMode(context.getMotionMode());
			targetCommand.setExplicitMotionMode(false);
		}

		// Extract the feedrate if the motion is anything but rapid
		GCodeToken fToken = RS274.findUniqueTokenByLetter("F", lstTokens);
		if(targetCommand.getMotionMode() != EnumGCodeCommandMotionMode.RAPID){
			if(fToken != null){
				targetCommand.setFeedrate( new BigDecimal(RS274.getTokenValue(fToken)) );
				targetCommand.setExplicitFeedrate(true);
			}else{
				targetCommand.setFeedrate( context.getFeedrate());
			}
		}else{
			targetCommand.setFeedrate(context.getFeedrate());
		}
	}

	/**
	 * Extracts Distance mode from the given raw command
	 * @param rawCommand the unspecified command
	 * @param context the current context
	 * @param targetCommand the target command to build
	 * @throws GkException GkException
	 */
	protected void setDistanceModeMode(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		targetCommand.setDistanceMode(context.getDistanceMode());
		targetCommand.setExplicitDistanceMode(false);

		if(RS274.isToken("g90", lstTokens)){
			targetCommand.setDistanceMode(EnumGCodeCommandDistanceMode.ABSOLUTE);
			targetCommand.setExplicitDistanceMode(true);

		}else if (RS274.isToken("g91", lstTokens)){
			targetCommand.setDistanceMode(EnumGCodeCommandDistanceMode.RELATIVE);
			targetCommand.setExplicitDistanceMode(true);
		}

	}

	/**
	 * Extracts Units from the given raw command
	 * @param rawCommand the unspecified command
	 * @param context the current context
	 * @param targetCommand the target command to build
	 * @throws GkException GkException
	 */
	protected void setUnit(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		if(RS274.findToken("G20", lstTokens) != null){
			targetCommand.setUnit(EnumGCodeCommandUnit.INCHES);
			targetCommand.setExplicitUnit(true);

		}else if (RS274.findToken("G21", lstTokens) != null){
			targetCommand.setUnit(EnumGCodeCommandUnit.MILLIMETERS);
			targetCommand.setExplicitUnit(true);

		}else{
			targetCommand.setUnit(context.getUnit());
			targetCommand.setExplicitUnit(false);
		}
	}

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
