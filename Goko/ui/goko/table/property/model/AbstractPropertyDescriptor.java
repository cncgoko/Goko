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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;


public abstract class AbstractPropertyDescriptor<T> {
	/** Id of the property */
	private String id;
	/** Label to be displayed */
	private String label;
	/** Cell editor */
	private CellEditor editor;

	/**
	 * Constructor
	 * @param id the id
	 * @param label the displayed label
	 */
	AbstractPropertyDescriptor(String id, String label) {
		super();
		this.id = id;
		this.label = label;

	}

	public abstract CellEditor createPropertyEditor(Composite parent);

	public CellEditor getEditor(Composite parent){
		if(editor == null){
			editor = createPropertyEditor(parent);
		}
		return editor;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public abstract String getPropertyValueLabel(T value);
	public abstract void  setPropertyValue(PropertyArray<T> pArray, int index, Object value);
	public abstract T getPropertyValue(PropertyArray<T> pArray, int index);
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(CellEditor editor) {
		this.editor = editor;
	}


}
