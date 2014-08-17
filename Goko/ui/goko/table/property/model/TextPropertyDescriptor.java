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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class TextPropertyDescriptor extends AbstractPropertyDescriptor<String>{
	private String format;

	/**
	 * Constructor
	 * @param id the id
	 * @param label the label
	 */
	public TextPropertyDescriptor(String id, String label, String format) {
		super(id, label);
		this.format = format;
	}

	/**
	 * Constructor
	 * @param id the id
	 * @param label the label
	 */
	public TextPropertyDescriptor(String id, String label) {
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
		if(parent != null){
			return new TextCellEditor(parent);
		}
		return new TextCellEditor();
	}

	@Override
	public String getPropertyValueLabel(String value) {
		return value;
	}

	@Override
	public String getPropertyValue(PropertyArray<String> pArray, int index) {
		return StringUtils.defaultString(pArray.getValue(index));
	}

	@Override
	public void setPropertyValue(PropertyArray<String> pArray, int index, Object value) {
		pArray.addValue(index, String.valueOf(value));
	}

}
