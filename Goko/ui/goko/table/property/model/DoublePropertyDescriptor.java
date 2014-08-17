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

import goko.table.property.jface.TextDoubleCellEditor;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class DoublePropertyDescriptor extends AbstractPropertyDescriptor<Double>{
	private String format;

	/**
	 * Constructor
	 * @param id the id
	 * @param label the label
	 */
	public DoublePropertyDescriptor(String id, String label, String format) {
		super(id, label);
		this.format = format;
	}

	/**
	 * Constructor
	 * @param id the id
	 * @param label the label
	 */
	public DoublePropertyDescriptor(String id, String label) {
		this(id, label, StringUtils.EMPTY);
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/** (inheritDoc)
	 * @see goko.table.property.model.AbstractPropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new TextDoubleCellEditor(parent);
	}

	@Override
	public String getPropertyValueLabel(Double value) {
		return String.valueOf(value);
	}

	@Override
	public Double getPropertyValue(PropertyArray<Double> pArray, int index) {
		return pArray.getValue(index);
	}

	@Override
	public void setPropertyValue(PropertyArray<Double> pArray, int index, Object value) {
		if(value instanceof Double){
			pArray.addValue(index, (Double) value);
		}else{
			pArray.addValue(index, null);
		}
	}

}
