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
package org.goko.core.rs274ngcv3.parser;

public enum GCodeTokenType {
	LINE_NUMBER("LINE_NUMBER","N[0-9]{1,5}"),
	MULTILINE_COMMENT("MULTILINE_COMMENT","\\((.*?)\\)"),
	SIMPLE_COMMENT("SIMPLE_COMMENT",";(.*)"),
	WORD("WORD","^[a-zA-Z]((-|\\+)?([0-9]+(\\.[0-9]*)?)|(\\.[0-9]+))"),
	NEW_LINE("NEW_LINE","(\\r)?\\n");

	private String code;
	private String pattern;

	/**
	 * @param code
	 * @param pattern
	 */
	private GCodeTokenType(String code, String pattern) {
		this.code = code;
		this.pattern = pattern;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
