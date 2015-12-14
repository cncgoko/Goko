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
/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.goko.core.rs274ngcv3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.core.rs274ngcv3.config.RS274Preference;
import org.goko.core.rs274ngcv3.parser.GCodeLexer;
import org.goko.core.rs274ngcv3.parser.GCodeToken;
import org.goko.core.rs274ngcv3.parser.advanced.AdvancedGCodeAnalyser;
import org.goko.core.rs274ngcv3.parser.advanced.RS274CommandWriter;

public class RS274GCodeService implements IGCodeService {
	private static final GkLog LOG = GkLog.getLogger(RS274GCodeService.class);
	/** Service ID */
	private static final String ID = "org.goko.core.rs274ngcv3";
	/** Service Name */
	public static final String NAME = "GCode";
	/** Instace */
	private static RS274GCodeService instance;
	/** Applicative log service */
	private IApplicativeLogService applicativeLogService;
	/**
	 * Empty constructor
	 */
	public RS274GCodeService() {

	}

	public static RS274GCodeService getInstance(){
		if(instance == null){
			instance = new RS274GCodeService();
		}
		return instance;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		RS274Preference.getInstance();
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
	public GCodeFile parseFile(String filepath, GCodeContext context) throws GkException {
		File 				file = new File(filepath);
		if(!file.exists()){
			throw new GkFunctionalException("File '"+filepath+"' does not exist...");
		}
		GCodeContext parserContext = context;
		if(parserContext == null){
			parserContext = new GCodeContext();
		}
		GCodeLexer 			gcodeLexer = new GCodeLexer();
		List<GCodeToken> 	lstTokens = gcodeLexer.createTokensFromFile(filepath);
		LOG.info("End of token creation...");
		GCodeFile		 	gcodeFile = new AdvancedGCodeAnalyser().createFile(lstTokens, parserContext);
		return gcodeFile;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parseFile(java.lang.String, org.goko.core.gcode.bean.GCodeContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IGCodeProvider parseFile(String filepath, GCodeContext context, IProgressMonitor monitor) throws GkException {
		File 				file = new File(filepath);		
		SubMonitor subMonitor = SubMonitor.convert(monitor,"Reading file", 2);
		if(!file.exists()){
			throw new GkFunctionalException("File '"+filepath+"' does not exist...");
		}
		GCodeContext parserContext = context;
		if(parserContext == null){
			parserContext = new GCodeContext();
		}
		GCodeLexer 			gcodeLexer = new GCodeLexer();
		subMonitor.subTask("Creating tokens...");
		List<GCodeToken> 	lstTokens = gcodeLexer.createTokensFromFile(filepath);
		subMonitor.worked(1);
		LOG.info("End of token creation...");
		subMonitor.subTask("Creating commands...");
		GCodeFile		 	gcodeFile = new AdvancedGCodeAnalyser().createFile(lstTokens, parserContext, subMonitor.newChild(1));
		subMonitor.done();
		return gcodeFile;
	};
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parse(java.lang.String)
	 */
	@Override
	public IGCodeProvider parse(String gcode, GCodeContext context) throws GkException {
		InputStream 		inputStream = new ByteArrayInputStream(gcode.getBytes());
		GCodeLexer 			gcodeLexer = new GCodeLexer();
		List<GCodeToken> 	lstTokens = gcodeLexer.createTokensFromInputStream(inputStream);
		GCodeContext parserContext = context;
		if(parserContext == null){
			parserContext = new GCodeContext();
		}
		GCodeFile 			gcodeFile = new AdvancedGCodeAnalyser().createFile(lstTokens, parserContext);
		return gcodeFile;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#parseCommand(java.lang.String)
	 */
	@Override
	public GCodeCommand parseCommand(String command, GCodeContext context) throws GkException {
		GCodeLexer gcodeLexer = new GCodeLexer();
		GCodeContext parserContext = context;
		if(parserContext == null){
			parserContext = new GCodeContext();
		}
		GCodeCommand gcodeCommand = new AdvancedGCodeAnalyser().createCommand(gcodeLexer.createTokens(command), parserContext);
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
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.bean.GCodeContext, org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public void update(GCodeContext context, GCodeCommand command) throws GkException {
		command.updateContext(context);
	}

	/**
	 * @param applicativeLogService the applicativeLogService to set
	 */
	public void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
	}

	public void log(int severity, String message, String source){
		if(this.applicativeLogService != null){
			this.applicativeLogService.log(severity, message, source);
		}
	}


}
