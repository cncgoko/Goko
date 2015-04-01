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

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;

/**
 * Linear command builder
 * @author PsyKo
 *
 */
public class LinearCommandBuilder extends AbstractMotionCommandBuilder<LinearMotionCommand> {
	/** LOG */
	//private static final GkLog LOG = GkLog.getLogger(LinearCommandBuilder.class);

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder#newInstance()
	 */
	@Override
	public LinearMotionCommand newInstance() throws GkException {
		return new LinearMotionCommand();
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractMotionCommandBuilder#buildCommand(java.util.Collection, org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.commands.MotionCommand)
	 */
	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, LinearMotionCommand targetCommand) throws GkException {
		super.buildCommand(lstTokens, context, targetCommand);
		//LOG.info("Building linear command from "+lstTokens.toString());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractMotionCommandBuilder#match(java.util.Collection, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return super.match(lstTokens, context) && isLinearMotionMode(lstTokens, context);
	}

	protected boolean isLinearMotionMode(List<GCodeToken> lstTokens, GCodeContext context) throws GkException{
		return (context.getMotionType() == EnumGCodeCommandMotionType.LINEAR
		|| (RS274.isToken("G0", lstTokens) || RS274.isToken("G00", lstTokens))
		|| RS274.isToken("G1", lstTokens) || RS274.isToken("G01", lstTokens))
		&& (!RS274.isToken("G2", lstTokens) && !RS274.isToken("G02", lstTokens))
		&& (!RS274.isToken("G3", lstTokens) && !RS274.isToken("G03", lstTokens));
	}
}
