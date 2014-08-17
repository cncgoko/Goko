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

import goko.table.property.model.AbstractPropertyDescriptor;
import goko.table.property.model.PropertyArray;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.goko.core.log.GkLog;

public class PropertyValueColumnLabelProvider extends ColumnLabelProvider {
	private static final GkLog LOG = GkLog.getLogger(PropertyValueColumnLabelProvider.class);
	private int columnIndex;


	/**
	 * @param columnIndex
	 */
	public PropertyValueColumnLabelProvider(int columnIndex) {
		super();
		this.columnIndex = columnIndex;
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if(element instanceof PropertyArray<?>){
			PropertyArray<?> pArray = (PropertyArray<?>)element;
			return getText(pArray);
			/*AbstractPropertyDescriptor<?> pDesc = pArray.getDescriptor();
			return pDesc.getPropertyValueLabel( pArray.getValue(columnIndex) );*/
		}
		LOG.error("Unsupported type :"+element);
		return "UNSUPPORTED";
	}

	private <T> String getText(PropertyArray<T> pArray){
		AbstractPropertyDescriptor<T> pDesc = pArray.getDescriptor();
		return pDesc.getPropertyValueLabel( pArray.getValue(columnIndex) );
	}

}
