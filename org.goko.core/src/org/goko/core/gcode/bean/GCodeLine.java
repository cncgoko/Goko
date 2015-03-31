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

import java.util.List;

public class GCodeLine {
	/** The gcode command in this line */
	private List<GCodeCommand> gcodeCommands;
	/** Line number if any */
	private String lineNumber;
	/** Comment if any */
	private String comment;

	/**
	 * @return the gcodeCommands
	 */
	public List<GCodeCommand> getGcodeCommands() {
		return gcodeCommands;
	}
	/**
	 * @param gcodeCommands the gcodeCommands to set
	 */
	public void setGcodeCommands(List<GCodeCommand> gcodeCommands) {
		this.gcodeCommands = gcodeCommands;
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
