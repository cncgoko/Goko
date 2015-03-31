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
package org.goko.gcode.rs274ngcv3.parser.advanced.builders;

import java.math.BigDecimal;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.AModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.BModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.CModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.XModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.YModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.ZModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.gwords.G382Modifier;

public abstract class AbstractMotionCommandBuilder<T extends MotionCommand> extends AbstractSettingCommandBuilder<T> {

	public AbstractMotionCommandBuilder() {
		super();
		addModifier(new XModifier());
		addModifier(new YModifier());
		addModifier(new ZModifier());
		addModifier(new AModifier());
		addModifier(new BModifier());
		addModifier(new CModifier());
		addModifier(new G382Modifier());

	}
//	/** (inheritDoc)
//	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractSettingCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.commands.SettingCommand)
//	 */
//	@Override
//	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
//		super.buildCommand(lstTokens, context, targetCommand);
//		setAbstractMotionParameters(lstTokens, context, targetCommand);
//	}


	/**
	 * Get all the motion related command from the raw command
	 * @param rawCommand the raw command
	 * @param context the context
	 * @param targetCommand the target command
	 * @throws GkException GkException
	 */
	private void setAbstractMotionParameters(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		targetCommand.setAbsoluteStartCoordinate(new Tuple6b(context.getPosition()));
		setCoordinates(lstTokens, context, targetCommand);
		//setEndPointCoordinates(rawCommand, targetCommand);
		// TODO : add plane selection
	}


	/**
	 * Extracts the X, Y, Z, A, B, C coordinates from the command and context
	 * @param rawCommand the raw command
	 * @param context the context
	 * @param targetCommand the target command
	 * @throws GkException GkException
	 */
	private void setCoordinates(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException{
		Tuple6b coordinates = new Tuple6b().setNull();
		GCodeToken token = RS274.removeUniqueTokenByLetter("x", lstTokens);
		BigDecimal scale = new BigDecimal("1");
		if(targetCommand.getUnit() == EnumGCodeCommandUnit.INCHES){
			scale = new BigDecimal("25.4");
		}
		if(token != null){
			coordinates.setX( new BigDecimal( RS274.getTokenValue(token)).multiply(scale));
		}
		token = RS274.removeUniqueTokenByLetter("y", lstTokens);
		if(token != null){
			coordinates.setY( new BigDecimal( RS274.getTokenValue(token)).multiply(scale));
		}
		token= RS274.removeUniqueTokenByLetter("z", lstTokens);
		if(token != null){
			coordinates.setZ( new BigDecimal( RS274.getTokenValue(token)).multiply(scale));
		}
		token = RS274.removeUniqueTokenByLetter("a", lstTokens);
		if(token != null){
			coordinates.setA( new BigDecimal( RS274.getTokenValue(token)));
		}
		token = RS274.removeUniqueTokenByLetter("b", lstTokens);
		if(token != null){
			coordinates.setB( new BigDecimal( RS274.getTokenValue(token)));
		}
		token = RS274.removeUniqueTokenByLetter("c", lstTokens);
		if(token != null){
			coordinates.setC( new BigDecimal( RS274.getTokenValue(token)));
		}
		targetCommand.setCoordinates(coordinates);
	}


	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder#match(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		// No call to super on purpose
		return RS274.findUniqueTokenByLetter("x", lstTokens) != null
				|| RS274.findUniqueTokenByLetter("y", lstTokens) != null
				|| RS274.findUniqueTokenByLetter("z", lstTokens) != null
				|| RS274.findUniqueTokenByLetter("a", lstTokens) != null
				|| RS274.findUniqueTokenByLetter("b", lstTokens) != null
				|| RS274.findUniqueTokenByLetter("c", lstTokens) != null;
	}



}
