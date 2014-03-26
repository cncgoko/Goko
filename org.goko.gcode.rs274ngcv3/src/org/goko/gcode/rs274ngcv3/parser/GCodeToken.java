/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.gcode.rs274ngcv3.parser;

public class GCodeToken {
	/** The token type */
	private GCodeTokenType type;
	/** The value */
	private String value;
	/**
	 * @param type
	 * @param value
	 */
	public GCodeToken(GCodeTokenType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	/**
	 * @return the type
	 */
	public GCodeTokenType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(GCodeTokenType type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
