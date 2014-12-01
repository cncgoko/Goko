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

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M0Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M1Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M2Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M3Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M4Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M5Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M6Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M7Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M8Modifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.mwords.M9Modifier;

public abstract class AbstractFunctionCommandBuilder<T extends FunctionCommand> extends AbstractSettingCommandBuilder<T>{


	public AbstractFunctionCommandBuilder() {
		super();
		addModifier(new M0Modifier());
		addModifier(new M1Modifier());
		addModifier(new M2Modifier());
		addModifier(new M3Modifier());
		addModifier(new M4Modifier());
		addModifier(new M5Modifier());
		addModifier(new M6Modifier());
		addModifier(new M7Modifier());
		addModifier(new M8Modifier());
		addModifier(new M9Modifier());
	}
//	/** (inheritDoc)
//	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractSettingCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.commands.SettingCommand)
//	 */
//	@Override
//	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
//		super.buildCommand(lstTokens, context, targetCommand);
//		targetCommand.setType(EnumGCodeCommandType.FUNCTION);
//		findFunctionType(lstTokens, targetCommand);
//	}
//
//	private void findFunctionType(List<GCodeToken> lstTokens, T targetCommand) throws GkException {
//		List<GCodeToken> mToken = RS274.findTokenByLetter("M", lstTokens);
//		if(CollectionUtils.isNotEmpty(mToken)){
//			for (GCodeToken gCodeToken : mToken) {
//				targetCommand.addFunctionType(getFunctionType(gCodeToken));
//				lstTokens.remove(gCodeToken);
//			}
//		}
//	}
//
//	/**
//	 * Detects the function type
//	 * @param lstTokens the list of tokens
//	 * @param targetCommand the target command
//	 * @return EnumGCodeCommandFunctionType
//	 * @throws GkException GkException
//	 */
//	protected EnumGCodeCommandFunctionType getFunctionType(GCodeToken token) throws GkException{
//		if(token != null){
//			String strToken = StringUtils.remove(token.getValue(), "0").toUpperCase();
//			switch(strToken){
//				case "M1": return EnumGCodeCommandFunctionType.OPTIONAL_PROGRAM_STOP;
//				case "M2": return EnumGCodeCommandFunctionType.PROGRAM_END;
//				case "M3": return EnumGCodeCommandFunctionType.TURN_SPINDLE_CLOCKWISE;
//				case "M4": return EnumGCodeCommandFunctionType.TURN_SPINDLE_COUNTERCLOCKWISE;
//				case "M5": return EnumGCodeCommandFunctionType.TURN_SPINDLE_OFF;
//				case "M6": return EnumGCodeCommandFunctionType.TOOL_CHANGE;
//				case "M7": return EnumGCodeCommandFunctionType.MIST_COOLANTE_ON;
//				case "M8": return EnumGCodeCommandFunctionType.FLOOD_COOLANT_ON;
//				case "M9": return EnumGCodeCommandFunctionType.COOLANT_OFF;
//				default : return EnumGCodeCommandFunctionType.UNKNOWN;
//			}
//		}
//		return EnumGCodeCommandFunctionType.UNKNOWN;
//	}


	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractCommentCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return CollectionUtils.isNotEmpty(RS274.findTokenByLetter("M", lstTokens));
	}
}
