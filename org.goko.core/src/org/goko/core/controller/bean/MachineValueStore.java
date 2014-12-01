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
package org.goko.core.controller.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.controller.event.MachineValueUpdateEvent;

/**
 * A store used to store and manage controller values
 *
 * @author PsyKo
 *
 */
public class MachineValueStore extends EventDispatcher{
	/** The actual values */
	private Map<String, MachineValue<?>> valueStore;
	/** The list of supported values */
	private List<MachineValueDefinition> lstMachineValueDefinition;

	public MachineValueStore() {
		super();
		this.valueStore = new HashMap<String, MachineValue<?>>();
		lstMachineValueDefinition = new ArrayList<MachineValueDefinition>();
	}

	public <T> void storeValue(MachineValueDefinition definition, MachineValue<T> value) throws GkException{
		if(!valueStore.containsKey(value.getIdDescriptor())){
			// First registration
			initializeValue(definition, value);
		}else{
			// Update
			updateValue(value.getIdDescriptor(), value);
		}
	}

	public <T> void storeValue(String id, String name, String description, T value) throws GkException{
		storeValue(new MachineValueDefinition(id, name, description, value.getClass()), new MachineValue<T>(id, value));
	}
	/**
	 * Returns the desired value as an Integer
	 * @param id the id of the value
	 * @return an Integer
	 * @throws GkException GkException
	 */
	public MachineValue<Integer> getIntegerValue(String id) throws GkException{
		return getValue(id, Integer.class);
	}
	/**
	 * Returns the desired value as a Boolean
	 * @param id the id of the value
	 * @return a Boolean
	 * @throws GkException GkException
	 */
	public MachineValue<Boolean> getBooleanValue(String id) throws GkException{
		return getValue(id, Boolean.class);
	}
	/**
	 * Returns the desired value as a BigDecimal
	 * @param id the id of the value
	 * @return a BigDecimal
	 * @throws GkException GkException
	 */
	public MachineValue<BigDecimal> getBigDecimalValue(String id) throws GkException{
		return getValue(id, BigDecimal.class);
	}

	/**
	 * Returns the stored value with the given type if possible.
	 * @param id the id of the requested value
	 * @param clazz the requested type
	 * @return MachineValue or <code>null</code> if no value is stored for that id
	 * @throws GkException thrown only if the requested type does not match the stored type
	 */
	public <T> MachineValue<T> findValue(String id, Class<T> clazz) throws GkException{
		MachineValue<?> machineValue = valueStore.get(id);
		if(machineValue != null){
			if(machineValue.getValue() == null){
				return (MachineValue<T>)machineValue;
			}else if(machineValue.getValue().getClass() == clazz){
				MachineValue<T> typedValue = (MachineValue<T>) valueStore.get(id);
				return new MachineValue<T>(typedValue);
			}else{
				throw new GkTechnicalException("ControllerValueStore : unable to get value '"+id+"' for the requested type '"+clazz+"'. Registered as '"+machineValue.getValue().getClass()+"'");
			}
		}
		return null;
	}
	/**
	 * Returns the stored value with the given type if possible
	 * @param id the id of the requested value
	 * @param clazz the requested type
	 * @return MachineValue
	 * @throws GkException thrown if the value does not exist, or if the requested type does not match the stored type
	 */
	public <T> MachineValue<T> getValue(String id, Class<T> clazz) throws GkException{
		if(!valueStore.containsKey(id)){
			throw new GkTechnicalException("ControllerValueStore : value with id '"+id+"' does not exist.'");
		}

		MachineValue<T> machineValue = findValue(id, clazz);

		return machineValue;
	}
	/**
	 * Initialize a value in the storage map
	 * @param name the name of the * @param value the value itself */
	protected <T> void initializeValue(MachineValueDefinition definition,MachineValue<T> value){
		lstMachineValueDefinition.add(definition);
		valueStore.put(value.getIdDescriptor(), value);

		super.notifyListeners(new MachineValueUpdateEvent(value));
	}

	/**
	 * Initialize a value in the storage map
	 * @param id the id of the value
	 * @param value the value itself
	 *
	 * @throws GkTechnicalException
	 */
	public <T> void updateValue(String id, T value) throws GkException{
		MachineValue<?> machineValue = valueStore.get(id);
		if(machineValue == null){
			throw new GkTechnicalException("ControllerValueStore : '"+id+"' not initialized.");
		}else if(machineValue.getValue().getClass() == value.getClass()){
			MachineValue<T> typedValue = (MachineValue<T>) valueStore.get(id);
			T oldValue = typedValue.getValue();
			typedValue.setValue(value);
			// Notify only if the value changed
			if(!value.equals(oldValue)){
				super.notifyListeners(new MachineValueUpdateEvent(new MachineValue<T>(typedValue)));
			}
		}else{
			throw new GkTechnicalException("ControllerValueStore : unable to store '"+id+"''. Type mismatch. Got "+value.getClass()+"', expecting '"+machineValue.getClass()+"'");
		}

	}

	public List<MachineValueDefinition> getMachineValueDefinition(){
		return lstMachineValueDefinition;
	}

	public Class<?> getControllerValueType(String name) {
		if(valueStore.containsKey(name) && valueStore.get(name) != null){
			return valueStore.get(name).getValue().getClass();
		}
		return Object.class;
	}

	public MachineValueDefinition getMachineValueDefinition(String id) throws GkException {
		MachineValueDefinition definition = findMachineValueDefinition(id);
		if(definition == null){
			throw new GkTechnicalException("ControllerValueStore : '"+id+"' does not exist.");
		}
		return definition;
	}

	public MachineValueDefinition findMachineValueDefinition(String id) throws GkException {
		for(MachineValueDefinition definition : lstMachineValueDefinition){
			if(StringUtils.equals(definition.getId(), id)){
				return definition;
			}
		}
		return null;
	}
}
