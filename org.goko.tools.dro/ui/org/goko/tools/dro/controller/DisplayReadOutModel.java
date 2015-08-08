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
package org.goko.tools.dro.controller;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;

public class DisplayReadOutModel extends AbstractModelObject {

	private WritableMap observedValues;
	private WritableList observedValuesDefinition;

	public DisplayReadOutModel() {
		observedValues = new WritableMap(String.class, MachineValue.class);
		observedValuesDefinition = new WritableList();
	}

	public Map<String, MachineValue> getObservedValues(){
		return observedValues;
	}

	public List<MachineValueDefinition> getObservedValuesDefinition(){
		return observedValuesDefinition;
	}

	public WritableList getWritableObservedValuesDefinition(){
		return observedValuesDefinition;
	}
	public WritableMap getWritableObservedValues(){
		return observedValues;
	}

	public void addObservedValueDefinition(MachineValueDefinition machineValueDefinition ){
		if(!observedValuesDefinition.contains(machineValueDefinition)){
			observedValuesDefinition.add(machineValueDefinition);
		}
	}
	public void addObservedValue(String id, MachineValue value){
		observedValues.put(id,value);
	}
}