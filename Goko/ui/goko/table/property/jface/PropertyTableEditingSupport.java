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
package goko.table.property.jface;

import goko.table.property.model.PropertyArray;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.widgets.Composite;

public class PropertyTableEditingSupport extends EditingSupport {

	private int columnIndex;

	public PropertyTableEditingSupport(ColumnViewer viewer, int columnIndex) {
		super(viewer);
		this.columnIndex = columnIndex;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {
		if(element instanceof PropertyArray<?>){
			return getPropertyCellEditor((PropertyArray)element);
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		if(element instanceof PropertyArray<?>){
			return getPropertyValue((PropertyArray)element);
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if(element instanceof PropertyArray<?>){
			setPropertyValue((PropertyArray)element, value);
		}
	}


	protected <T> CellEditor getPropertyCellEditor(PropertyArray<T> pArray) {
		return pArray.getDescriptor().getEditor((Composite)getViewer().getControl());
	}

	protected boolean canEditProperty(PropertyArray<?> pArray) {
		return true;
	}

	protected <T> Object getPropertyValue(PropertyArray<T> pArray) {
		return pArray.getDescriptor().getPropertyValue(pArray, columnIndex);
	}


	protected <T> void setPropertyValue(PropertyArray<T> pArray, Object value) {
		pArray.getDescriptor().setPropertyValue(pArray, columnIndex, value);
		getViewer().refresh();
	}

}
