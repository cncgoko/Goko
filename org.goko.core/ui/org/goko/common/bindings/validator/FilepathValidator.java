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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class FilepathValidator implements IValidator {

	public FilepathValidator() {

	}

	@Override
	public IStatus validate(Object value) {
		if(value != null
				&& value instanceof String
				&& StringUtils.isNotBlank(String.valueOf(value))){
			File file = new File(String.valueOf(value));
			if(file.exists()){
				if(file.isFile()){
					return ValidationStatus.ok();
				}else{
					return ValidationStatus.error("Path does not point to a valid file.");
				}
			}else{
				return ValidationStatus.error("File does not exist.");
			}
		}
		return ValidationStatus.ok();
	}


}
