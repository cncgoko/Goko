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
package org.goko.core.rs274ngcv3.parser.advanced.tokens.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.rs274ngcv3.parser.GCodeToken;

public abstract class MultiTokenCommandModifier<T extends GCodeCommand> extends TokenCommandModifier<T> {
	/** The token to match */
	protected List<String> stringPattern;
	/**
	 * Constructor
	 * @param token the matching token
	 */
	public MultiTokenCommandModifier(String... token) {
		stringPattern = new ArrayList<String>();
		for (String pattern : token) {
			stringPattern.add(pattern);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.rs274ngcv3.parser.advanced.tokens.abstracts.TokenCommandModifier#match(org.goko.core.rs274ngcv3.parser.GCodeToken)
	 */
	@Override
	public boolean match(GCodeToken token) throws GkException {
		for (String pattern : stringPattern) {
			if(StringUtils.equals(pattern, token.getValue())){
				return true;
			}
		}
		return false;
	}

}
