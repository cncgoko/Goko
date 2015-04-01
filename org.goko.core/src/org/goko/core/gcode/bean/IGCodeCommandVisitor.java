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
package org.goko.core.gcode.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.RawCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;

public interface IGCodeCommandVisitor {

	void visit(RawCommand command) throws GkException;
	void visit(CommentCommand command) throws GkException;
	void visit(SettingCommand command) throws GkException;
	void visit(MotionCommand command) throws GkException;
	void visit(LinearMotionCommand command) throws GkException;
	void visit(ArcMotionCommand command) throws GkException;
	void visit(FunctionCommand command) throws GkException;
}
