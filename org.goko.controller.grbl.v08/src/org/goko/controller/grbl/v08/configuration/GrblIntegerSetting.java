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
package org.goko.controller.grbl.v08.configuration;

import org.apache.commons.lang3.StringUtils;

/**
 * Grbl setting represented by an integer value
 *
 * @author PsyKo
 *
 */
public class GrblIntegerSetting extends GrblSetting<Integer> {

	GrblIntegerSetting(String identifier, Integer value) {
		super(identifier, value, Integer.class);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.GrblSetting#setValueFromString(java.lang.String)
	 */
	@Override
	public void setValueFromString(String value) {
		if(StringUtils.isNumeric(value)){
			setValue(Integer.valueOf(value));
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.GrblSetting#getValueAsString()
	 */
	@Override
	public String getValueAsString() {
		return String.valueOf(getValue());
	}

}
