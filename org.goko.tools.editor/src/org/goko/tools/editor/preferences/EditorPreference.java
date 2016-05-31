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
package org.goko.tools.editor.preferences;

import org.goko.core.config.GkPreference;

/**
 * Editor preferences
 *
 * @author PsyKo
 *
 */
public class EditorPreference extends GkPreference{	
	public static final String NODE = "org.goko.tools.editor";

	public static final String FONT_NAME 	= "editor.fontname";
	public static final String FONT_SIZE 	= "editor.fontsize";
	
	private static EditorPreference instance;
	
	public EditorPreference(){
		super(NODE);	
	}

	public static EditorPreference getInstance() {
		if(instance == null){
			instance = new EditorPreference();
		}
		return instance;
	}

	/**
	 * @return the fontName
	 */
	public String getFontName() {
		return getString(FONT_NAME);
	}

	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		setValue(FONT_NAME, fontName);		
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return getInt(FONT_SIZE);
	}

	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(int fontSize) {
		setValue(FONT_SIZE, fontSize);
	}	
}
