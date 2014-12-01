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
package org.goko.gcode.rs274ngcv3.parser.advanced.writer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.CommentCommand;

public abstract class AbstractCommentCommandWriter<T extends CommentCommand> extends AbstractRS274CommandWriter<T>{

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.writer.AbstractRS274CommandWriter#write(java.lang.String, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	protected String write(String base, T command) throws GkException {
		String str = "N"+String.valueOf(command.getLineNumber())+" "+base;
		return str;//+" "+command.getComment();
	}

	protected String addComment(String base, T command){
		if(StringUtils.isNotBlank(command.getComment())){
			return base + command.getComment();
		}
		return base;
	}
}
