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
package org.goko.common.bindings.validator;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class StringBigDecimalValidator implements IValidator {

	public StringBigDecimalValidator() {

	}

	@Override
	public IStatus validate(Object value) {
		if(value != null && value instanceof String){
			String strValue = String.valueOf(value);
			DecimalFormat df = new DecimalFormat();
			df.setParseBigDecimal(true);
			try {
				df.parse(strValue);
			} catch (ParseException e) {
				return ValidationStatus.error("Invalid number : value '"+String.valueOf(value)+"' is not a valid number.");
			}
			return ValidationStatus.ok();
		}
		return ValidationStatus.error("Invalid number : value '"+String.valueOf(value)+"' is not a valid number.");
	}

}
