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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.core.rs274ngcv3.RS274;
import org.goko.core.rs274ngcv3.parser.GCodeToken;
import org.goko.core.rs274ngcv3.parser.GCodeTokenType;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.FModifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.SModifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G0Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G17Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G18Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G19Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G1Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G20Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G21Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G2Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G3Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G53Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G54Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G55Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G56Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G57Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G58Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G90Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.gwords.G91Modifier;

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
	 * @see org.goko.core.rs274ngcv3.parser.advanced.builders.AbstractCommentCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
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