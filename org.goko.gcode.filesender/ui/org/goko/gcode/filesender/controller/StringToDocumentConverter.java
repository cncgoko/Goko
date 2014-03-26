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
package org.goko.gcode.filesender.controller;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.jface.text.Document;

public class StringToDocumentConverter extends Converter {

	public StringToDocumentConverter() {
		super(String.class, Document.class);
	}

	@Override
	public Object convert(Object fromObject) {
		if(fromObject == null){
			return StringUtils.EMPTY;
		}
		return new Document(String.valueOf(fromObject));
	}

}
