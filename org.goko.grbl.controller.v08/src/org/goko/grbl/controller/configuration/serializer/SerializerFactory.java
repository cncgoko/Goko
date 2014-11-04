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
package org.goko.grbl.controller.configuration.serializer;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkTechnicalException;

public class SerializerFactory {
	private List<AbstractGrblSerializer<?,?>> lstSerializer;

	public SerializerFactory() {
		lstSerializer = new ArrayList<AbstractGrblSerializer<?,?>>();
		addSerializer( new StringDoubleSerializer());
		addSerializer( new StringIntegerSerializer());
		addSerializer( new StringBooleanSerializer());
	}

	public void addSerializer(AbstractGrblSerializer<?,?> serializer){
		lstSerializer.add(serializer);
	}

	@SuppressWarnings("unchecked")
	public <T> AbstractGrblSerializer<String, T> getSerializer(Class<T> targetClass) throws GkTechnicalException{
		for (AbstractGrblSerializer<?,?> abstractGrblSerializer : lstSerializer) {
			if(abstractGrblSerializer.getTargetClass() == targetClass){
				return (AbstractGrblSerializer<String, T>)abstractGrblSerializer;
			}
		}
		throw new GkTechnicalException("No serializer found for class "+targetClass.toString());
	}
}
