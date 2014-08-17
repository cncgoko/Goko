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
package org.goko.common.bindings.validator;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class StringRealNumberValidator implements IValidator {

	public StringRealNumberValidator() {

	}

	@Override
	public IStatus validate(Object value) {
		if(value instanceof BigDecimal ||
			value instanceof Integer ||
			value instanceof Double ||
			value instanceof Float ){
			return ValidationStatus.ok();
		}
		if(value != null && value instanceof String &&
				StringUtils.defaultIfBlank(value.toString(), StringUtils.EMPTY).matches("-?[0-9]+(\\.[0-9]*)?")){
			return ValidationStatus.ok();
		}
		return ValidationStatus.error("Invalid number : value '"+String.valueOf(value)+"' is not a valid number.");
	}

}
