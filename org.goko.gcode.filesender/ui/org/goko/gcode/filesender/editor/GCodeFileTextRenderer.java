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
package org.goko.gcode.filesender.editor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IGCodeProvider;

/**
 * Transform a GCode file into a text representation
 *
 * @author PsyKo
 *
 */
public class GCodeFileTextRenderer {
	/** EOL String token */
	private static String EOL = System.getProperty("line.separator");


	public static String renderGCodeFile(IGCodeProvider provider) throws GkException{		
		return GCodeFileTextRenderer.renderCommands(provider);		
	}
	/**
	 * Render the a GCodeFile
	 * @param file the GCodeFile to render
	 * @return a String
	 * @throws GkException GkException
	 */
	public static String renderCommands(IGCodeProvider provider) throws GkException{
		StringBuffer txtFile = new StringBuffer();

		for(GCodeLine gLine : provider.getLines()){
			txtFile.append(render(gLine));
			txtFile.append(EOL);
		}

		return txtFile.toString();
	}

	/**
	 * Renders a single GCodeLine
	 * @param gLine GCodeLine to render
	 * @return a String
	 */
	public static String render(GCodeLine gLine) {
		StringBuffer txtLine = new StringBuffer();
		txtLine.append(render(gLine.getWords()));

		return txtLine.toString();
	}
	/**
	 * Renders a list of {@link GCodeCommand}
	 * @param commands the list to render
	 * @return a String
	 */
	private static String render(List<GCodeWord> lstGCodeWords) {
		StringBuffer txtCommands = new StringBuffer();
		for(GCodeWord word : lstGCodeWords){
			if(word == null){
				continue;
			}
			if(StringUtils.isNotBlank(txtCommands.toString())){
				txtCommands.append(" ");	
			}
			txtCommands.append(word.completeString());			
		}
		return txtCommands.toString();
	}
}
