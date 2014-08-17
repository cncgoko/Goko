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
package goko.table.property.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class PropertyArray<T> {
	private AbstractPropertyDescriptor<T> descriptor;
	private List<T> values;

	/**
	 * @param descriptor
	 * @param values
	 */
	PropertyArray(AbstractPropertyDescriptor<T> descriptor, List<T> values) {
		super();
		this.descriptor = descriptor;
		this.values = values;
	}
	/**
	 * @param descriptor
	 */
	PropertyArray(AbstractPropertyDescriptor<T> descriptor) {
		this(descriptor,new ArrayList<T>());
	}
	/**
	 * @return the descriptor
	 */
	public AbstractPropertyDescriptor<T> getDescriptor() {
		return descriptor;
	}
	/**
	 * @param descriptor the descriptor to set
	 */
	public void setDescriptor(AbstractPropertyDescriptor<T> descriptor) {
		this.descriptor = descriptor;
	}

	public void addValue(int index, T value){
		autosizeValuesList(index);
		values.set(index, value);
	}

	public T getValue(int index){
		return values.get(index);
	}

	protected void autosizeValuesList(int index){
		while(CollectionUtils.size(values) <= index){
			values.add(null);
		}
	}
}
