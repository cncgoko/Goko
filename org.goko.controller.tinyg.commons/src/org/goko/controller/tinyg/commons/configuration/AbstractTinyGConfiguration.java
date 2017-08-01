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
package org.goko.controller.tinyg.commons.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;



/**
 * Abstract implementation of TinyG based board configuration
 *
 * @author PsyKo
 *
 */
public abstract class AbstractTinyGConfiguration<C extends AbstractTinyGConfiguration<C>> {
	/** The list of handled groups */
	private List<TinyGGroupSettings> groups;

	/**
	 * Constructor
	 */
	public AbstractTinyGConfiguration(){
		groups   = new ArrayList<TinyGGroupSettings>();		
	}
	
	/**
	 * Add the given group
	 * @param group the group to add 
	 */
	public void addGroup(TinyGGroupSettings group){
		groups.add(group);
	}

	/**
	 * @return the groups
	 */
	public List<TinyGGroupSettings> getGroups() {
		return groups;
	}

	/**
	 * Returns the group for the given identifier
	 * @param identifier the identifier of the requested group
	 * @return TinyGGroupSettings
	 */
	public TinyGGroupSettings getGroup(String identifier) {
		for (TinyGGroupSettings tinyGGroupSettings : groups) {
			if(StringUtils.equals(tinyGGroupSettings.getGroupIdentifier(), identifier)){
				return tinyGGroupSettings;
			}
		}
		return null;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<TinyGGroupSettings> groups) {
		this.groups = groups;
	}

	/**
	 * Returns the setting as the specified type amongst the given list of settings
	 * @param identifier the identifier
	 * @param lstSettings th settings to go through
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> T getSetting(String identifier, List<TinyGSetting<?>> lstSettings, Class<T> clazz) throws GkException{
		for(TinyGSetting setting : lstSettings){
			if(StringUtils.equalsIgnoreCase( setting.getIdentifier(), identifier ) ){
				if(setting.getType() != clazz){
					throw new GkTechnicalException("Cannot retrieve setting '"+identifier+"' type. Requesting "+clazz+"', got'"+setting.getType()+"'. ");				
				}
				return (T) setting.getValue();
			}
		}
		throw new GkFunctionalException("Setting '"+identifier+"' is unknown");
	}

	/**
	 * Returns the setting as a String
	 * @param groupIdentifier the identifier of the group
	 * @param identifier the identifier
	 * @return the value as a String
	 * @throws GkException GkException
	 */
	public String getSetting(String groupIdentifier, String identifier) throws GkException{
		return getSetting(groupIdentifier, identifier, String.class);
	}
	
	/**
	 * Returns the setting as the specified type or null if not found
	 * @param groupIdentifier the identifier of the group
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T findSetting(String groupIdentifier, String identifier, Class<T> clazz) throws GkException{
		for(TinyGGroupSettings grpSetting : groups){
			if(StringUtils.equalsIgnoreCase( grpSetting.getGroupIdentifier(), groupIdentifier ) ){
				T setting = getSetting(identifier,grpSetting.getSettings(), clazz);				
				return setting;
			}
		}
		return null;
	}
	/**
	 * Returns the setting as the specified type
	 * @param groupIdentifier the identifier of the group
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T getSetting(String groupIdentifier, String identifier, Class<T> clazz) throws GkException{
		for(TinyGGroupSettings grpSetting : groups){
			if(StringUtils.equalsIgnoreCase( grpSetting.getGroupIdentifier(), groupIdentifier ) ){
				T setting = getSetting(identifier,grpSetting.getSettings(), clazz);
				if(setting == null){
					throw new GkFunctionalException("Setting '"+identifier+"' is unknown for group "+groupIdentifier);
				}
				return setting;
			}
		}
		throw new GkFunctionalException("Unknown  group "+groupIdentifier);

	}

	/**
	 * Sets the setting value
	 * @param groupIdentifier the identifier of the group
	 * @param identifier the identifier
	 * @param value the value to set
	 * @throws GkException GkException
	 */
	public <T> void setSetting(String groupIdentifier, String identifier, T value) throws GkException{
		for(TinyGGroupSettings grpSetting : groups){
			if(StringUtils.equalsIgnoreCase( grpSetting.getGroupIdentifier(), groupIdentifier ) ){
				setSetting(grpSetting, identifier, value);				
				return;
			}
		}
		throw new GkFunctionalException("Setting '"+identifier+"' from group '"+groupIdentifier+"' is unknown");
	}

	/**
	 * Sets the setting value
	 * @param group the TinyGGroupSettings
	 * @param identifier the identifier
	 * @param value the value to set
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	private <T> void setSetting(TinyGGroupSettings group, String identifier, T value) throws GkException{
		for(TinyGSetting<?> setting : group.getSettings()){
			if(StringUtils.equalsIgnoreCase( setting.getIdentifier(), identifier ) ){
				if(value != null && setting.getType() != value.getClass()){
					throw new GkTechnicalException("Setting '"+identifier+"' type mismatch. Expecting "+setting.getType()+"', got'"+value.getClass()+"'. ");
				}
				((TinyGSetting<T>)setting).setValue(value);
				return;
			}
		}
	}
	
	/**
	 * Determines if this configuration was completely assigned using at least once the setValue(..) method on every setting
	 * @return <code>true</code> if all settings were assigned, <code>false</code> otherwise
	 */
	public boolean isCompletelyLoaded(){
		for (TinyGGroupSettings tinyGGroupSettings : groups) {
			if(!tinyGGroupSettings.isCompletelyLoaded()){
				return false;
			}
		}		
		return true;
	}
	
	public void copyFrom(final AbstractTinyGConfiguration<C> baseConfig){		
		for (TinyGGroupSettings tinyGGroupSettings : baseConfig.getGroups()) {
			addGroup(tinyGGroupSettings.copy());
		}
	}
	
	/**
	 * Creates a new instance of this configuration
	 * @return C
	 */
	protected abstract C newInstance();

	/**
	 * Returns a copy of this configuration
	 * @return C
	 */
	public C getCopy(){
		C newInstance = newInstance();
		newInstance.copyFrom(this);
		return newInstance;
	}
	

	/**
	 * Returns a configuration containing only the values that differ from the base configuration. Other values are null
	 * @param baseConfig the base configuration
	 * @param newConfig the new configuration
	 * @return a differential configuration
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	public C getDifferentialConfiguration(C otherConfig) throws GkException{
		C diffConfig = newInstance();
		if(!isCompletelyLoaded() && otherConfig.isCompletelyLoaded()){
			// We switch from a partial config to a complete config
			diffConfig.copyFrom(otherConfig);
		}else{ // if(!isCompletelyLoaded() && !otherConfig.isCompletelyLoaded()){
			// Otherwise, let's compute it setting by setting
			diffConfig.copyFrom(this);
			
			for(TinyGGroupSettings group : getGroups()){
				List<TinyGSetting<?>> settings = group.getSettings();
				for (TinyGSetting<?> tinyGSetting : settings) {
					Object baseValue = tinyGSetting.getValue();				
					Object newValue = otherConfig.getSetting(group.getGroupIdentifier(), tinyGSetting.getIdentifier(), tinyGSetting.getType());
					if(!ObjectUtils.equals(baseValue, newValue)){
						diffConfig.setSetting(group.getGroupIdentifier(), tinyGSetting.getIdentifier(), newValue);
					}else{
						diffConfig.setSetting(group.getGroupIdentifier(), tinyGSetting.getIdentifier(), null);
					}
				}
			}
		}
		return diffConfig;
	}
	
	/**
	 * Build the given configuration using the JSon object as input and support for recursive levels
	 * @param config the target configuration
	 * @param json the JSon object to get values from
	 * @throws GkException GkException
	 */
	public void setFromJson(JsonObject json) throws GkException{
		setFromJson(json, null);
	}
	
	/**
	 * Build the given configuration using the JSon object as input and support for recursive levels
	 * @param config the target configuration
	 * @param json the JSon object to get values from
	 * @param identifierPrefix the current identifier prefix for JSon group handling
	 * @throws GkException GkException
	 */
	protected void setFromJson(JsonObject json, String identifierPrefix) throws GkException{
		JsonObject jsonObj = json;
		for(String name : jsonObj.names()){
			JsonValue subObj = jsonObj.get(name);
			if(subObj.isObject()){
				setFromJson((JsonObject)subObj, name);
			}else{
				if(StringUtils.isBlank(identifierPrefix)){
					setSetting(getDefaultGroup(), name, getValue(subObj));
				}else{
					setSetting(identifierPrefix, name, getValue(subObj));
				}
			}
		}
	}

	/**
	 * Returns the default group (usually the system group)
	 * @return the default group 
	 */
	protected abstract String getDefaultGroup();
	
	/**
	 * Returns the value of the given JsonValue
	 * @param jsonValue the JsonValue to get value of 
	 * @return Object
	 */
	private static Object getValue(JsonValue jsonValue){
		if(jsonValue.isNumber()){
			return jsonValue.asBigDecimal();
		}else if(jsonValue.isString()){
			return jsonValue.asString();
		}
		return null;

	}
}
