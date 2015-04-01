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
package org.goko.gcode.rs274ngcv3.parser.advanced;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.GCodeTokenType;
import org.goko.gcode.rs274ngcv3.parser.ModalGroup;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.ArcCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.CommentCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.FunctionCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.LinearCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.RawCommandBuilder;
import org.goko.gcode.rs274ngcv3.parser.advanced.builders.SettingCommandBuilder;

public class AdvancedGCodeAnalyser {
	private static final GkLog LOG = GkLog.getLogger(AdvancedGCodeAnalyser.class);
	private List<IRS274CommandBuilder<?>> lstBuilders;
	private List<ModalGroup> modalGroups;

	public AdvancedGCodeAnalyser() {
		lstBuilders = new ArrayList<IRS274CommandBuilder<?>>();
		lstBuilders.add( new ArcCommandBuilder() );
		lstBuilders.add( new LinearCommandBuilder() );
		//lstBuilders.add( new ToolChangeCommandBuilder() );
		lstBuilders.add( new FunctionCommandBuilder() );
		lstBuilders.add( new SettingCommandBuilder() );
		lstBuilders.add( new CommentCommandBuilder() );
		lstBuilders.add( new RawCommandBuilder() );
		initializeModalGroups();
	}

	protected void initializeModalGroups(){
		this.modalGroups = new ArrayList<ModalGroup>();
		this.modalGroups.add( new ModalGroup("G0", "G1", "G2", "G3", "G38.2", "G80", "G81", "G82", "G83", "G84", "G85", "G86", "G87", "G88", "G89" ));

		this.modalGroups.add( new ModalGroup("G17", "G18", "G19"  ));
		this.modalGroups.add( new ModalGroup("G90", "G91"));
		this.modalGroups.add( new ModalGroup("G93", "G94"));
		this.modalGroups.add( new ModalGroup("G20", "G21"));
		this.modalGroups.add( new ModalGroup("G40", "G41", "G42"));
		this.modalGroups.add( new ModalGroup("G43","G49"));
		this.modalGroups.add( new ModalGroup("G98","G99"));
		this.modalGroups.add( new ModalGroup("G54", "G55", "G56", "G57", "G58", "G59", "G59.1", "G59.2", "G59.3"));
		this.modalGroups.add( new ModalGroup("G61", "G61.1", "G64"));

		this.modalGroups.add( new ModalGroup("M0", "M1", "M2", "M30", "M60"));
		this.modalGroups.add( new ModalGroup("M6"));
		this.modalGroups.add( new ModalGroup("M3","M4","M5"));
		this.modalGroups.add( new ModalGroup("M7","M9"));
		this.modalGroups.add( new ModalGroup("M8","M9"));
		this.modalGroups.add( new ModalGroup("M48", "M49"));
	}

	/**
	 * Create a GCodeFile by parsing the given list of {@link GCodeToken}
	 * @param tokens the list of token
	 * @param intialContext the initial context
	 * @return a {@link GCodeFile}
	 * @throws GkException GkException
	 */
	public GCodeFile createFile(List<GCodeToken> tokens, GCodeContext intialContext) throws GkException{
		GCodeContext 		context = new GCodeContext(intialContext);
		List<GCodeToken> 	lineTokens = new ArrayList<GCodeToken>();
		List<GCodeCommand> 	lstCommmands = new ArrayList<GCodeCommand>();
		// Let's detect the end token. We search the first NEW_LINE token, or the end of the list
		for (GCodeToken gCodeToken : tokens) {
			if(gCodeToken.getType() == GCodeTokenType.NEW_LINE){
				if(CollectionUtils.isNotEmpty(lineTokens)){
					GCodeCommand command = createCommand(lineTokens, context);
					lstCommmands.add( command );
					lineTokens.clear();
				}
			}else{
				lineTokens.add(gCodeToken);
			}
		}
		// Let's compute the final GCodeCommand
		if(CollectionUtils.isNotEmpty(lineTokens)){
			GCodeCommand command = createCommand(lineTokens, context);
			lstCommmands.add( command );
			lineTokens.clear();
		}

		GCodeFile file = new GCodeFile(lstCommmands);
		return file;
	}

	/**
	 * Create a GCodeCommand from the given list of tokens
	 * @param tokens the list of token
	 * @param intialContext the initial context
	 * @return a {@link GCodeCommand}
	 * @throws GkException GkException
	 */
	public GCodeCommand createCommand(List<GCodeToken> tokens, GCodeContext intialContext) throws GkException{
		verifyModality(tokens);
		GCodeCommand typedCommand = null;
		typedCommand = analyseGCodeCommand(tokens, intialContext);
		typedCommand.updateContext(intialContext);
		return typedCommand;
	}

	/**
	 * Create a specialized GCodeCommand by calling the several builders
	 * @param tokens the list of token
	 * @param intialContext the initial context
	 * @return a {@link GCodeCommand}
	 * @throws GkException GkException
	 */
	private GCodeCommand analyseGCodeCommand(List<GCodeToken> tokens, GCodeContext context) throws GkException{
		for (IRS274CommandBuilder<?> builder : lstBuilders) {
			if(builder.match(tokens, context)){
				return builder.createCommand(tokens, context);
			}
		}
		String tokensString = "";
		for (GCodeToken gCodeToken : tokens) {
			tokensString += gCodeToken.getValue();
		}
		throw new GkTechnicalException("No builder found for command token list "+tokensString);
	}

	/**
	 * Verify the modality of the token in a single line. If the modality constraint is violated, an exception is thrown.
	 * @param tokens the list of token
	 *  @throws GkException GkException
	 */
	public void verifyModality(List<GCodeToken> lstToken) throws GkException{
		for (ModalGroup group : modalGroups) {
			group.verifyTokenModality(lstToken);
		}
	}
}
