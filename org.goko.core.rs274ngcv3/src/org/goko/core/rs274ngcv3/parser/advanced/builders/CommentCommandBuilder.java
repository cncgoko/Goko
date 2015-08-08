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

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.CommentCommand;

/**
 * Comment command builder
 *
 * @author PsyKo
 *
 */
public class CommentCommandBuilder extends  AbstractCommentCommandBuilder<CommentCommand>{

	@Override
	public CommentCommand newInstance() throws GkException {
		return new CommentCommand();
	}

}
