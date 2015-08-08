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
package org.goko.controller.grbl.v08.configuration.serializer;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;

public class StringDoubleSerializer extends AbstractGrblSerializer<String, Double> {

	/**
	 * Constructor
	 */
	StringDoubleSerializer() {
		super(String.class, Double.class);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.serializer.AbstractGrblSerializer#fromSource(java.lang.Object)
	 */
	@Override
	public Double fromSource(String str) throws GkException {
		if(StringUtils.isNumeric(str)){
			return Double.valueOf(str);
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.serializer.AbstractGrblSerializer#fromTarget(java.lang.Object)
	 */
	@Override
	public String fromTarget(Double obj) throws GkException {
		if(obj != null){
			return obj.toString();
		}

		return StringUtils.EMPTY;
	}

}
