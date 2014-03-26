package org.goko.core.gcode.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GCodeFile implements IGCodeCommandProvider {
	/** The original file */
	private File file;
	/** The parsed data */
	private List<GCodeCommand> lstGCodeCommands;

	/**
	 * Constructor
	 */
	public GCodeFile(){
		lstGCodeCommands = new ArrayList<GCodeCommand>();
	}
	/**
	 * Add a parsed line to this file
	 * @param line the line to add
	 */
	public void addGCodeCommand(GCodeCommand line){
		lstGCodeCommands.add(line);
	}
	/**
	 * Add a parsed line to this file
	 * @param line the line to add
	 */
	public void addAllGCodeCommand(List<GCodeCommand> lstCommands){
		this.lstGCodeCommands.addAll(lstCommands);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandProvider#getGCodeCommands()
	 */
	@Override
	public final List<GCodeCommand> getGCodeCommands() {
		return lstGCodeCommands;
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


}
