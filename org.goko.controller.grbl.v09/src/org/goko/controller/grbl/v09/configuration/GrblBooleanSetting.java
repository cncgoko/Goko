/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.v09.configuration;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Grbl setting represented by a double value
 *
 * @author PsyKo
 *
 */
public class GrblBooleanSetting extends GrblSetting<Boolean> {

	GrblBooleanSetting(String identifier, Boolean value) {
		super(identifier, value, Boolean.class);
	}

	/**
	 * (inheritDoc)
	 *
	 * @see org.goko.controller.grbl.v08.configuration.GrblSetting#setValueFromString(java.lang.String)
	 */
	@Override
	public void setValueFromString(String value) {
		if(StringUtils.isNotBlank(value)){
			setValue(BooleanUtils.toBoolean(value,"1","0"));
		}
	}

	@Override
	public String getValueAsString() {
		return BooleanUtils.toString(getValue(),"1","0");
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.configuration.GrblSetting#newInstance()
	 */
	@Override
	protected GrblSetting<Boolean> newInstance() {
		return new GrblBooleanSetting(getIdentifier(), new Boolean(getValue()));
	}
}
