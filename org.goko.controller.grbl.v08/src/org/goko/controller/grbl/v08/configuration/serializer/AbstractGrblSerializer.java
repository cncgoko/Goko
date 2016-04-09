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

import org.goko.core.common.exception.GkException;

public abstract class AbstractGrblSerializer<S, T> {
	private Class<S> sourceClass;
	private Class<T> targetClass;

	/**
	 * @param sourceClass
	 * @param targetClass
	 */
	AbstractGrblSerializer(Class<S> sourceClass, Class<T> targetClass) {
		super();
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	/**
	 * @return the sourceClass
	 */
	public Class<S> getSourceClass() {
		return sourceClass;
	}


	/**
	 * @param sourceClass the sourceClass to set
	 */
	public void setSourceClass(Class<S> sourceClass) {
		this.sourceClass = sourceClass;
	}

	/**
	 * @return the targetClass
	 */
	public Class<T> getTargetClass() {
		return targetClass;
	}

	/**
	 * @param targetClass the targetClass to set
	 */
	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	public abstract T fromSource(S obj) throws GkException;
	public abstract S fromTarget(T obj) throws GkException;
}
