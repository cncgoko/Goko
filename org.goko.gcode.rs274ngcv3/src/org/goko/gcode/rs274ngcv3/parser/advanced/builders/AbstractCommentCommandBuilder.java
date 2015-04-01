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
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.CommentModifier;
import org.goko.gcode.rs274ngcv3.parser.advanced.tokens.NModifier;

/**
 * Abstract {@link CommentCommand} builder
 * @author PsyKo
 *
 * @param <T>
 */
public abstract class AbstractCommentCommandBuilder<T extends CommentCommand> extends AbstractModifierCommandBuilder<T> {
	//private static final GkLog LOG = GkLog.getLogger(AbstractCommentCommandBuilder.class);
	private CommentModifier commentModifier;

	public AbstractCommentCommandBuilder() {
		super();
		commentModifier = new CommentModifier();
		addModifier(commentModifier);
		addModifier(new NModifier());
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractRawCommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		boolean match = false;
		for (GCodeToken gCodeToken : lstTokens) {
			if(commentModifier.match(gCodeToken)){
				match = true;
				break;
			}
		}
		return match;
	}
//	/** {@inheritDoc}
//	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.builders.AbstractRawCommandBuilder#buildCommand(java.util.List, org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.GCodeCommand)
//	 */
//	@Override
//	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
//		String comment = "";
//		List<GCodeToken> tmpTokens = new ArrayList<GCodeToken>(lstTokens);
//		for (GCodeToken gCodeToken : tmpTokens) {
//			if(gCodeToken.getType() == GCodeTokenType.MULTILINE_COMMENT || gCodeToken.getType() == GCodeTokenType.SIMPLE_COMMENT){
//				comment += gCodeToken.getValue();
//				lstTokens.remove(gCodeToken);
//			}
//		}
//		targetCommand.setComment(comment);
//		super.buildCommand(lstTokens, context, targetCommand);
//	}
//
//
//	@Override
//	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
//
//		if(CollectionUtils.isNotEmpty(lstTokens)){
//			for (GCodeToken token : lstTokens) {
//				if(token.getType() == GCodeTokenType.MULTILINE_COMMENT || token.getType() == GCodeTokenType.SIMPLE_COMMENT){
//					return true;
//				}else{
//					return false;
//				}
//			}
//
//		}
//		return false;
//	}

}
