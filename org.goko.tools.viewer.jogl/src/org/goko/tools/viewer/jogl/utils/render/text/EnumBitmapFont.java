/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.tools.viewer.jogl.utils.render.text;

public enum EnumBitmapFont {
	CONSOLAS("consolas","/resources/font/Consolas_$size.fnt");
	private String name;
	private String filepath;

	private EnumBitmapFont(String name, String filepath) {
		this.name = name;
		this.filepath = filepath;
	}
	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the filepath
	 */
	protected String getFilepath() {
		return filepath;
	}
	/**
	 * @param filepath the filepath to set
	 */
	protected void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
