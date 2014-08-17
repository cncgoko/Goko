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
package org.goko.common.bindings.converters;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.conversion.Converter;

public class StringDoubleConverter extends Converter {

	public StringDoubleConverter() {
		super(String.class, Double.class);
	}

	@Override
	public Object convert(Object fromObject) {

		if(fromObject instanceof String){
			String strValue = (String) fromObject;
			if(StringUtils.defaultString(strValue).matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
				return Double.valueOf(strValue);
			}
		}
		return null;
	}


}
