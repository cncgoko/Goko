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
package org.goko.gcode.rs274ngcv3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.gcode.rs274ngcv3.parser.GCodeLexer;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.AdvancedGCodeAnalyser;
import org.goko.gcode.rs274ngcv3.parser.advanced.RS274CommandWriter;

public class GkGCodeService implements IGCodeService {

	public GkGCodeService() {
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parseFile(java.lang.String)
	 */
	@Override
	public GCodeFile parseFile(String filepath) throws GkException {
		System.err.println("startTimeDebug org.goko.core.gcode.service.IGCodeService#parse(java.lang.String)");
		File 				file = new File(filepath);
		if(!file.exists()){
			throw new GkFunctionalException("File '"+filepath+"' does not exist...");
		}

		GCodeLexer 			gcodeLexer = new GCodeLexer();
		List<GCodeToken> 	lstTokens = gcodeLexer.createTokensFromFile(filepath);
		GCodeFile		 	gcodeFile = new AdvancedGCodeAnalyser().createFile(lstTokens, new GCodeContext());
		return gcodeFile;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parse(java.lang.String)
	 */
	@Override
	public IGCodeProvider parse(String gcode) throws GkException {
		InputStream 		inputStream = new ByteArrayInputStream(gcode.getBytes());
		GCodeLexer 			gcodeLexer = new GCodeLexer();
		List<GCodeToken> 	lstTokens = gcodeLexer.createTokensFromInputStream(inputStream);
		GCodeFile 			gcodeFile = new AdvancedGCodeAnalyser().createFile(lstTokens, new GCodeContext());
		return gcodeFile;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parseCommand(java.lang.String)
	 */
	@Override
	public GCodeCommand parseCommand(String command) throws GkException {
		GCodeLexer gcodeLexer = new GCodeLexer();
		GCodeCommand gcodeCommand = new AdvancedGCodeAnalyser().createCommand(gcodeLexer.createTokens(command), new GCodeContext());

		return gcodeCommand;

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#convert(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public byte[] convert(GCodeCommand command) throws GkException {
		RS274CommandWriter writer = new RS274CommandWriter();
		return StringUtils.defaultString(writer.write(command)).getBytes();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void update(GCodeContext context, GCodeCommand command) throws GkException {
		command.updateContext(context);
	}



}
