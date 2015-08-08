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
package org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandFunctionType;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.rs274ngcv3.parser.GCodeToken;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.abstracts.MultiTokenCommandModifier;

public class M1Modifier extends MultiTokenCommandModifier<FunctionCommand>{

	public M1Modifier() {
		super("M1","M01");
	}

	/** (inheritDoc)
	 * @see org.goko.core.rs274ngcv3.parser.advanced.tokens.abstracts.TokenCommandModifier#apply(org.goko.core.rs274ngcv3.parser.GCodeToken, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void apply(GCodeToken token, FunctionCommand command) throws GkException {
		command.addFunctionType(EnumGCodeCommandFunctionType.OPTIONAL_PROGRAM_STOP);
	}

}
