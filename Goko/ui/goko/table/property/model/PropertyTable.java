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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PropertyTable {
	private int columnCount;
	private Map<String, PropertyArray<?>> mapValues;


	/**
	 * @param columnCount
	 */
	public PropertyTable(int columnCount) {
		super();
		this.columnCount = columnCount;
		this.mapValues = new HashMap<String, PropertyArray<?>>();
	}
	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}
	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public void addProperty(AbstractPropertyDescriptor<?> descriptor){
		PropertyArray<?> value = initPropertyArray(descriptor);
		this.mapValues.put(descriptor.getId(), value);
	}

	private <T> PropertyArray<T> initPropertyArray(AbstractPropertyDescriptor<T> descriptor) {
		PropertyArray<T> pArray = new PropertyArray<T>(descriptor);
		for(int i = 0; i < columnCount; i++){
			pArray.addValue(i, null);
		}
		return pArray;
	}

	public Collection<PropertyArray<?>> getProperties(){
		return mapValues.values();
	}

	public <T> void setProperty(String propertyId, int column, T value){
		PropertyArray<T> pArray = (PropertyArray<T>) mapValues.get(propertyId);
		setArrayProperty(pArray, column, value);
	}

	public <T> T getProperty(String propertyId, int column){
		PropertyArray<T> pArray = (PropertyArray<T>) mapValues.get(propertyId);
		return pArray.getValue(column);
	}

	private <T> void setArrayProperty(PropertyArray<T> pArray, int column, T value){
		pArray.addValue(column, value);

	}
}
