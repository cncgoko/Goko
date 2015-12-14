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
package org.goko.core.rs274ngcv3.parser.advanced.builders;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.rs274ngcv3.RS274;
import org.goko.core.rs274ngcv3.parser.GCodeToken;
import org.goko.core.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder;

public abstract class AbstractRawCommandBuilder<T extends GCodeCommand> extends AbstractRS274CommandBuilder<T> {

	/** {@inheritDoc}
	 * @see org.goko.core.rs274ngcv3.parser.advanced.IRS274CommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return true;
	}



	/** {@inheritDoc}
	 * @see org.goko.core.rs274ngcv3.parser.advanced.IRS274CommandBuilder#buildCommand(java.util.List, org.goko.core.gcode.bean.GCodeContext, java.lang.Object)
	 */
	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
		targetCommand.setStringCommand( RS274.toString(lstTokens) );

		GCodeToken lineToken = RS274.findUniqueTokenByLetter("n", lstTokens);
		if(lineToken != null){
			String strNumber =  RS274.getTokenValue(lineToken);
			targetCommand.setLineNumber( Integer.valueOf(strNumber) );
			lstTokens.remove(lineToken);
		}
	}


}
