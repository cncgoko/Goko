package org.goko.core.gcode.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class GCodeFile implements IGCodeProvider {
	/** The original file */
	private File file;
	/** The list of gcode commands */
	private List<GCodeCommand> gCodeCommands;


	/**
	 * Constructor
	 */
	public GCodeFile(){
		gCodeCommands = new ArrayList<GCodeCommand>();
	}

	public void addGCodeCommand(GCodeCommand command){
		if(StringUtils.isBlank(command.getLineNumber())){
			generateLineNumber(command);
		}
		this.gCodeCommands.add(command);
	}

	/**
	 * Add the given collection of GCodeCommand to this file
	 * @param commands the collection of lines
	 */
	public void addGCodeCommand(Collection<GCodeCommand> commands){
		if(CollectionUtils.isNotEmpty(commands)){
			for (GCodeCommand gCodeCommand : commands) {
				addGCodeCommand(gCodeCommand);
			}
		}
	}

	/**
	 * Generate a line number for the given command
	 * @param command the GCodeLine
	 */
	private void generateLineNumber(GCodeCommand command) {
		int lineNumber = CollectionUtils.size(gCodeCommands) + 1;
		command.setLineNumber("N"+String.valueOf(lineNumber));
	}

	/**
	 * Return the number of line in this file
	 * @return the number of line
	 */
	public int getLineCount(){
		return CollectionUtils.size(gCodeCommands);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the gCodeCommands
	 */
	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return gCodeCommands;
	}


}
