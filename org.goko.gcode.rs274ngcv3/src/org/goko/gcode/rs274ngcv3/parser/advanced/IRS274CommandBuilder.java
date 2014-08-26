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
package org.goko.gcode.rs274ngcv3.parser.advanced;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;

public interface IRS274CommandBuilder<T extends GCodeCommand> {

	/**
	 * Determines if this builder is suitable to build a command from the raw command in the given context
	 * @param lstTokens the list of GCodeTokens
	 * @param context the context
	 * @return <code>true</code> if the builder is appropriate for the command, <code>false</code> otherwise
	 * @throws GkException
	 */
	boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException;

	/**
	 * Build the command by reading information in the rawCommand and completing the targetCommand
	 * @param lstTokens the list of GCodeTokens
	 * @param context the context
	 * @return the built T command
	 * @throws GkException GkException
	 */
	T createCommand(List<GCodeToken> lstTokens, GCodeContext context) throws GkException;

}
