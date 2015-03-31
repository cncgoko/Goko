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

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.applicative.logging.ApplicativeLogEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.RS274GCodeService;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.abstracts.TokenCommandModifier;

/**
 * Command builder based on Token modifiers
 * @author PsyKo
 *
 * @param <T> the type of supported command
 */
public abstract class AbstractModifierCommandBuilder<T extends GCodeCommand> extends AbstractRS274CommandBuilder<T> {
	private static final GkLog LOG = GkLog.getLogger(AbstractModifierCommandBuilder.class);
	List<TokenCommandModifier<? super T>> modifiers;

	public AbstractModifierCommandBuilder() {
		this.modifiers = new ArrayList<TokenCommandModifier<? super T>>();
	}

	public AbstractModifierCommandBuilder(List<TokenCommandModifier<T>> modifiers) {
		this();
		this.modifiers.addAll(modifiers);
	}

	protected void addModifier(TokenCommandModifier<? super T> modifier){
		this.modifiers.add(modifier);
	}

	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
		targetCommand.setStringCommand( RS274.toString(lstTokens) );
		targetCommand.initFromContext(context);
		for (GCodeToken gCodeToken : lstTokens) {
			boolean recognizedToken = false;
			for (TokenCommandModifier<? super T> modifier : modifiers) {
				if(modifier.match(gCodeToken)){
					modifier.apply(gCodeToken, targetCommand);
					recognizedToken = true;
					continue;
				}

			}
			if(!recognizedToken){
				RS274GCodeService.getInstance().log(ApplicativeLogEvent.LOG_WARNING, "Unsupported token "+gCodeToken.getValue(), RS274GCodeService.NAME);
				LOG.error("Unsupported token "+gCodeToken.getValue() );
			}
		}
	}

}
