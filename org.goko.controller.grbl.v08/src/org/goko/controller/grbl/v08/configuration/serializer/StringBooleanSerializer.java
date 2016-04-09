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
package org.goko.controller.grbl.v08.configuration.serializer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;

public class StringBooleanSerializer extends AbstractGrblSerializer<String, Boolean> {

	/**
	 * Constructor
	 */
	StringBooleanSerializer() {
		super(String.class, Boolean.class);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.serializer.AbstractGrblSerializer#fromSource(java.lang.Object)
	 */
	@Override
	public Boolean fromSource(String str) throws GkException {
		if(StringUtils.isNumeric(str)){
			return StringUtils.equals(str, "1");
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.serializer.AbstractGrblSerializer#fromTarget(java.lang.Object)
	 */
	@Override
	public String fromTarget(Boolean obj) throws GkException {
		if(obj != null){
			if(obj){
				return "1";
			}else{
				return "0";
			}
		}

		return StringUtils.EMPTY;
	}

}
