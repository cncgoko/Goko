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
package org.goko.core.gcode.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;

public interface IGCodeService extends IGokoService{
	/**
	 * Parse a file as a GCodeFile
	 * @param filepath the path to file
	 * @return {@link IGCodeProvider}
	 * @throws GkException GkException
	 */
	IGCodeProvider parseFile(String filepath) throws GkException;

	/**
	 * Parse a string ile as a GCodeFile
	 * @param gcode the gcode to aprse
	 * @return {@link IGCodeProvider}
	 * @throws GkException GkException
	 */
	IGCodeProvider parse(String gcode) throws GkException;

	/**
	 * Parse a command
	 * @param command the string command
	 * @return a List of {@link GCodeCommand}
	 * @throws GkException
	 */
	GCodeCommand parseCommand(String command) throws GkException;

	/**
	 * Convert a GCode command into an array of byte
	 * @param command the {@link GCodeCommand} to convert
	 * @return an array of {@link Byte}
	 * @throws GkException GkException
	 */
	byte[] convert(GCodeCommand command) throws GkException;

	/**
	 * Update the given GCode context with the given command.
	 * @param context the context to update
	 * @param command the command to apply
	 * @throws GkException GkException
	 */
	void update(GCodeContext context, GCodeCommand command) throws GkException;
}
