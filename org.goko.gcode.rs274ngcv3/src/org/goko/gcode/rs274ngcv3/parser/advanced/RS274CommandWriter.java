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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.RawCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.ArcCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.CommentCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.FunctionCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.LinearCommandWriter;
import org.goko.gcode.rs274ngcv3.parser.advanced.writer.SettingCommandWriter;

public class RS274CommandWriter implements IGCodeCommandVisitor {
	private String writtenCommand;
	private CommentCommandWriter commentCommandWriter;
	private SettingCommandWriter settingCommandWriter;
	private LinearCommandWriter linearCommandWriter;
	private ArcCommandWriter arcCommandWriter;
	private FunctionCommandWriter functionCommandWriter;

	public RS274CommandWriter() {
		writtenCommand 			= StringUtils.EMPTY;
		commentCommandWriter 	= new CommentCommandWriter();
		settingCommandWriter 	= new SettingCommandWriter();
		linearCommandWriter 	= new LinearCommandWriter();
		arcCommandWriter 		= new ArcCommandWriter();
		functionCommandWriter 	= new FunctionCommandWriter();
	}

	public <T extends GCodeCommand> String write(T command) throws GkException{
		writtenCommand = StringUtils.EMPTY;
		// Accept will make the command call itself on this object, matching types
		command.accept(this);
		return StringUtils.trim(writtenCommand);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.RawCommand)
	 */
	@Override
	public void visit(RawCommand command) throws GkException {
		writtenCommand = command.getStringCommand();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.CommentCommand)
	 */
	@Override
	public void visit(CommentCommand command) throws GkException {
		writtenCommand = commentCommandWriter.write(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.SettingCommand)
	 */
	@Override
	public void visit(SettingCommand command) throws GkException {
		writtenCommand = settingCommandWriter.write(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.MotionCommand)
	 */
	@Override
	public void visit(MotionCommand command) throws GkException {
		// Motion command without type should not exist
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.LinearMotionCommand)
	 */
	@Override
	public void visit(LinearMotionCommand command) throws GkException {
		writtenCommand = linearCommandWriter.write(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.ArcMotionCommand)
	 */
	@Override
	public void visit(ArcMotionCommand command) throws GkException {
		writtenCommand = arcCommandWriter.write(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.FunctionCommand)
	 */
	@Override
	public void visit(FunctionCommand command) throws GkException {
		writtenCommand = functionCommandWriter.write(command);
	}

}
