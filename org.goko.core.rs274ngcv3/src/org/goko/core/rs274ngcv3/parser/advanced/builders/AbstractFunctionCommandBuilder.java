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

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.rs274ngcv3.RS274;
import org.goko.core.rs274ngcv3.parser.GCodeToken;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M0Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M1Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M2Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M30Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M3Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M4Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M5Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M6Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M7Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M8Modifier;
import org.goko.core.rs274ngcv3.parser.advanced.tokens.mwords.M9Modifier;

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
		addModifier(new M30Modifier());
	}

	/** (inheritDoc)
	 * @see org.goko.core.rs274ngcv3.parser.advanced.builders.AbstractCommentCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return CollectionUtils.isNotEmpty(RS274.findTokenByLetter("M", lstTokens));
	}
}
