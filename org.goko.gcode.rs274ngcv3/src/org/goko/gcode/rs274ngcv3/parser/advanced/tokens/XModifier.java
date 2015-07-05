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
package org.goko.gcode.rs274ngcv3.parser.advanced.tokens;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.abstracts.LetterTokenCommandModifier;

public class XModifier extends LetterTokenCommandModifier<MotionCommand>{

	public XModifier() {
		super("X");
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.tokens.abstracts.TokenCommandModifier#apply(org.goko.gcode.rs274ngcv3.parser.GCodeToken, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void apply(GCodeToken token, MotionCommand command) throws GkException {
		Tuple6b coordinates = command.getCoordinates();
		coordinates.setX( NumberQuantity.of(new BigDecimal(RS274.getTokenValue(token)), command.getUnit().getUnit()));
		command.setCoordinates(coordinates);
	}

}
