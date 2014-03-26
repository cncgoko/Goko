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
package org.goko.common.elements.combo;

import org.apache.commons.lang3.ObjectUtils;

/**
 * A standard value/label item
 *
 * @author PsyKo
 *
 */
public class LabeledValue<T> implements ILabelledValue<T>{
	/** The value */
	private T value;
	/** The label */
	private String label;

	/**
	 * Constructor
	 * @param value
	 * @param label
	 */
	public LabeledValue(T value, String label) {
		this.value = value;
		this.label = label;
	}

	/**
	 * @return the value
	 */
	@Override
	public T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the label
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LabeledValue){
			return ObjectUtils.equals(value, ((LabeledValue<T>)obj).getValue());
		}
		return false;
	}


}
