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

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.IModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.JModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.KModifier;

/**
 * Arc command builder
 *
 * @author PsyKo
 *
 */
public class ArcCommandBuilder extends AbstractMotionCommandBuilder<ArcMotionCommand> {
	//private static final GkLog LOG = GkLog.getLogger(ArcCommandBuilder.class);


	public ArcCommandBuilder() {
		super();
		addModifier(new IModifier());
		addModifier(new JModifier());
		addModifier(new KModifier());
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractMotionCommandBuilder#buildCommand(org.goko.core.gcode.bean.GCodeCommand, org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.commands.MotionCommand)
	 */
	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, ArcMotionCommand target) throws GkException {
		super.buildCommand(lstTokens, context, target);
		extractDirection(lstTokens, context, target);

	}

	protected void extractDirection(List<GCodeToken> lstTokens, GCodeContext context, ArcMotionCommand target) throws GkException {
		target.setClockwise( target.getMotionMode() == EnumGCodeCommandMotionMode.ARC_CLOCKWISE);
	}
//	protected void extractArcCenter(List<GCodeToken> lstTokens, GCodeContext context, ArcMotionCommand target) throws GkException {
//		Tuple6b absoluteCenterCoordinate = context.getPosition();
//		BigDecimal scale = new BigDecimal("1");
//		GCodeToken token = RS274.removeUniqueTokenByLetter("i", lstTokens);
//		if(target.getUnit() == EnumGCodeCommandUnit.INCHES){
//			scale = new BigDecimal("25.4");
//		}
//		Tuple6b centerRelativeOffsets = new Tuple6b().setNull();
//		if(token != null){
//			centerRelativeOffsets.setX( new BigDecimal( RS274.getTokenValue(token) ).multiply(scale));
//		}
//
//		token = RS274.removeUniqueTokenByLetter("j", lstTokens);
//		if(token != null){
//			centerRelativeOffsets.setY( new BigDecimal( RS274.getTokenValue(token) ).multiply(scale));
//		}
//
//		token = RS274.removeUniqueTokenByLetter("k", lstTokens);
//		if(token != null){
//			centerRelativeOffsets.setZ( new BigDecimal( RS274.getTokenValue(token) ).multiply(scale));
//		}
//
//		token = RS274.removeUniqueTokenByLetter("p", lstTokens);
//		if(token != null){
//			target.setTurnCount(Integer.valueOf( RS274.getTokenValue(token) ));
//		}
//
//		token = RS274.removeUniqueTokenByLetter("r", lstTokens);
//		if(token != null){
//			target.setRadius(new BigDecimal( RS274.getTokenValue(token) ).multiply(scale));
//		}
//		absoluteCenterCoordinate.updateRelative(centerRelativeOffsets);
//		target.setAbsoluteCenterCoordinate(absoluteCenterCoordinate);
//	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder#newInstance()
	 */
	@Override
	public ArcMotionCommand newInstance() throws GkException {
		return new ArcMotionCommand();
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractMotionCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return super.match(lstTokens, context)
			&& isArcMotionMode(lstTokens, context)
			&& (RS274.findUniqueTokenByLetter("i", lstTokens) != null
			|| RS274.findUniqueTokenByLetter("j", lstTokens) != null
			|| RS274.findUniqueTokenByLetter("k", lstTokens) != null
			|| RS274.findUniqueTokenByLetter("r", lstTokens) != null);
	}

	protected boolean isArcMotionMode(List<GCodeToken> lstTokens, GCodeContext context) throws GkException{
		return context.getMotionType() == EnumGCodeCommandMotionType.ARC
		|| RS274.isToken("G2", lstTokens)
		|| RS274.isToken("G3", lstTokens);
	}
}
