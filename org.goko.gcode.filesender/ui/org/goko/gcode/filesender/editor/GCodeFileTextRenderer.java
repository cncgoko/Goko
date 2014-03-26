package org.goko.gcode.filesender.editor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.core.gcode.bean.IGCodeCommandProvider;

/**
 * Transform a GCode file into a text representation
 *
 * @author PsyKo
 *
 */
public class GCodeFileTextRenderer {
	/** EOL String token */
	private static String EOL = System.getProperty("line.separator");


	public static String renderGCodeFile(GCodeFile file) throws GkException{
		BufferedReader br;
		try {
			StringBuffer txtFile = new StringBuffer();
			br = new BufferedReader(new FileReader(file.getFile()));
			String line;
			while ((line = br.readLine()) != null) {
				txtFile.append(line.toString());
				txtFile.append(EOL);
			}
			br.close();
			return txtFile.toString();
		} catch (FileNotFoundException e) {
			throw new GkTechnicalException(e);
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
	/**
	 * Render the a GCodeFile
	 * @param file the GCodeFile to render
	 * @return a String
	 * @throws GkException GkException
	 */
	public static String renderCommands(IGCodeCommandProvider provider) throws GkException{
		StringBuffer txtFile = new StringBuffer();

		for(GCodeCommand gLine : provider.getGCodeCommands()){
			//txtFile.append(gLine.toString());
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
	private static String render(GCodeCommand gLine) {
		StringBuffer txtLine = new StringBuffer();
		if(StringUtils.isNotBlank(gLine.getLineNumber())){
			txtLine.append(gLine.getLineNumber());
			txtLine.append(" ");
		}
		txtLine.append(render(gLine.getGCodeWords()));

		if(StringUtils.isNotBlank(gLine.getComment())){
			txtLine.append(gLine.getComment());
			txtLine.append(" ");
		}

		return txtLine.toString();
	}
	/**
	 * Renders a list of {@link GCodeCommand}
	 * @param commands the list to render
	 * @return a String
	 */
	private static  String render(List<GCodeWord> lstGCodeWords) {
		StringBuffer txtCommands = new StringBuffer();
		for(GCodeWord word : lstGCodeWords){
			if(word == null){
				continue;
			}

			txtCommands.append(word.getStringValue());
			txtCommands.append(" ");
		}
		return txtCommands.toString();
	}
}
