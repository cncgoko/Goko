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
