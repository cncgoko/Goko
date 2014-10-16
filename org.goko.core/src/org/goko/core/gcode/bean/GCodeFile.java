package org.goko.core.gcode.bean;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.gcode.bean.provider.AbstractGCodeProvider;

public class GCodeFile extends AbstractGCodeProvider implements IGCodeProvider {
	/** The original file */
	private File file;

	/**
	 * Constructor
	 * @param commands the content of the file
	 */
	public GCodeFile(List<GCodeCommand> commands){
		super(commands);
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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getName()
	 */
	@Override
	public String getName() {
		if(file != null){
			return file.getName();
		}
		return StringUtils.EMPTY;
	}


}
