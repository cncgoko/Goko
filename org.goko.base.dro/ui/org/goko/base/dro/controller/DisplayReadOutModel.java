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
package org.goko.base.dro.controller;

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
//
//	private String positionX;
//	private String positionY;
//	private String positionZ;
//	private String velocity;
//	private String state;
//	/**
//	 * @return the positionX
//	 */
//	public synchronized String getPositionX() {
//		return positionX;
//	}
//	/**
//	 * @param positionX the positionX to set
//	 */
//	public synchronized void setPositionX(String positionX) {
//		firePropertyChange("positionX", this.positionX, this.positionX = positionX);
//	}
//	/**
//	 * @return the positionY
//	 */
//	public synchronized String getPositionY() {
//		return positionY;
//	}
//	/**
//	 * @param positionY the positionY to set
//	 */
//	public synchronized void setPositionY(String positionY) {
//		firePropertyChange("positionY", this.positionY, this.positionY = positionY);
//	}
//	/**
//	 * @return the positionZ
//	 */
//	public synchronized String getPositionZ() {
//		return positionZ;
//	}
//	/**
//	 * @param positionZ the positionZ to set
//	 */
//	public synchronized void setPositionZ(String positionZ) {
//		firePropertyChange("positionZ", this.positionZ, this.positionZ = positionZ);
//	}
//	/**
//	 * @return the velocity
//	 */
//	public synchronized String getVelocity() {
//		return velocity;
//	}
//	/**
//	 * @param velocity the velocity to set
//	 */
//	public synchronized void setVelocity(String velocity) {
//		firePropertyChange("velocity", this.velocity, this.velocity = velocity);
//	}
//	/**
//	 * @return the state
//	 */
//	public String getState() {
//		return state;
//	}
//	/**
//	 * @param state the state to set
//	 */
//	public void setState(String state) {
//		firePropertyChange("state", this.state, this.state = state);
//	}

}

//
///*
//*
//*   Goko
//*   Copyright (C) 2013  PsyKo
//*
//*   This program is free software: you can redistribute it and/or modify
//*   it under the terms of the GNU General Public License as published by
//*   the Free Software Foundation, either version 3 of the License, or
//*   (at your option) any later version.
//*
//*   This program is distributed in the hope that it will be useful,
//*   but WITHOUT ANY WARRANTY; without even the implied warranty of
//*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//*   GNU General Public License for more details.
//*
//*   You should have received a copy of the GNU General Public License
//*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
//*
//*/
//package org.goko.base.dro.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.eclipse.core.databinding.observable.list.WritableList;
//import org.eclipse.core.databinding.observable.map.WritableMap;
//import org.goko.common.bindings.AbstractModelObject;
//import org.goko.core.controller.bean.MachineValue;
//import org.goko.core.controller.bean.MachineValueDefinition;
//
//public class DisplayReadOutModel extends AbstractModelObject {
//
//	private WritableMap<String, MachineValue> observedValues;
//	private WritableList<MachineValueDefinition> observedValuesDefinition;
//
//	public DisplayReadOutModel() {
//		observedValues = new WritableMap<String, MachineValue>(String.class, MachineValue.class);
//		observedValuesDefinition = new WritableList<MachineValueDefinition>();
//	}
//
//	public Map<String, MachineValue> getObservedValues(){
//		return observedValues;
//	}
//
//	public List<MachineValueDefinition> getObservedValuesDefinition(){
//		return observedValuesDefinition;
//	}
//
//	public WritableList<MachineValueDefinition> getWritableObservedValuesDefinition(){
//		return observedValuesDefinition;
//	}
//	public WritableMap<String, MachineValue> getWritableObservedValues(){
//		return observedValues;
//	}
//
//	public void addObservedValueDefinition(MachineValueDefinition machineValueDefinition ){
//		if(!observedValuesDefinition.contains(machineValueDefinition)){
//			observedValuesDefinition.add(machineValueDefinition);
//		}
//	}
//	public void addObservedValue(String id, MachineValue value){
//		observedValues.put(id,value);
//	}
////
////	private String positionX;
////	private String positionY;
////	private String positionZ;
////	private String velocity;
////	private String state;
////	/**
////	 * @return the positionX
////	 */
////	public synchronized String getPositionX() {
////		return positionX;
////	}
////	/**
////	 * @param positionX the positionX to set
////	 */
////	public synchronized void setPositionX(String positionX) {
////		firePropertyChange("positionX", this.positionX, this.positionX = positionX);
////	}
////	/**
////	 * @return the positionY
////	 */
////	public synchronized String getPositionY() {
////		return positionY;
////	}
////	/**
////	 * @param positionY the positionY to set
////	 */
////	public synchronized void setPositionY(String positionY) {
////		firePropertyChange("positionY", this.positionY, this.positionY = positionY);
////	}
////	/**
////	 * @return the positionZ
////	 */
////	public synchronized String getPositionZ() {
////		return positionZ;
////	}
////	/**
////	 * @param positionZ the positionZ to set
////	 */
////	public synchronized void setPositionZ(String positionZ) {
////		firePropertyChange("positionZ", this.positionZ, this.positionZ = positionZ);
////	}
////	/**
////	 * @return the velocity
////	 */
////	public synchronized String getVelocity() {
////		return velocity;
////	}
////	/**
////	 * @param velocity the velocity to set
////	 */
////	public synchronized void setVelocity(String velocity) {
////		firePropertyChange("velocity", this.velocity, this.velocity = velocity);
////	}
////	/**
////	 * @return the state
////	 */
////	public String getState() {
////		return state;
////	}
////	/**
////	 * @param state the state to set
////	 */
////	public void setState(String state) {
////		firePropertyChange("state", this.state, this.state = state);
////	}
//
//}
