/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.commons.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.grbl.commons.configuration.settings.GrblSetting;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;


/**
 * Grbl configuration definition
 *
 * @author PsyKo
 *
 */
public abstract class AbstractGrblConfiguration<C extends AbstractGrblConfiguration<C>> {
	private List<GrblSetting<?>> lstGrblSetting;

	public AbstractGrblConfiguration(){
		this.lstGrblSetting = new ArrayList<GrblSetting<?>>();
		initSettings();
	}

	protected abstract void initSettings();

	public void setValue(String identifier, String value) {
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equals(grblSetting.getIdentifier(), identifier)){
				grblSetting.setValueFromString(value);
			}
		}
	}
	
	/**
	 * Sets the setting value
	 * @param group the TinyGGroupSettings
	 * @param identifier the identifier
	 * @param value the value to set
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	public <T> void setSetting(String identifier, T value) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(value != null && grblSetting.getType() != value.getClass()){
					throw new GkTechnicalException("Setting '"+identifier+"' type mismatch. Expecting "+grblSetting.getType()+"', got'"+value.getClass()+"'. ");
				}
				((GrblSetting<T>)grblSetting).setValue(value);
				return;
			}
		}
	}
	
	/**
	 * Returns the setting as the specified type or null if not found
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T findSetting(String identifier, Class<T> clazz) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(grblSetting.getType() != clazz){
					throw new GkTechnicalException("Cannot retrieve setting '"+identifier+"' type. Requesting "+clazz+"', got'"+grblSetting.getType()+"'. ");
				}
				return (T) grblSetting.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Returns the setting as the specified type
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T getSettingValue(String identifier, Class<T> clazz) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){
				if(grblSetting.getType() != clazz){
					throw new GkTechnicalException("Cannot retrieve setting '"+identifier+"' type. Requesting "+clazz+"', got'"+grblSetting.getType()+"'. ");
				}
				return (T) grblSetting.getValue();
			}
		}
		throw new GkFunctionalException("Setting '"+identifier+"' is unknown");
	}
	
	/**
	 * Returns the setting as the specified type
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public String getSettingStringValue(String identifier) throws GkException{
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(StringUtils.equalsIgnoreCase( grblSetting.getIdentifier(), identifier ) ){				
				return grblSetting.getValueAsString();
			}
		}
		throw new GkFunctionalException("Setting '"+identifier+"' is unknown");
	}
	
	protected void registerSetting(GrblSetting<?> setting){
		lstGrblSetting.add(setting);
	}

	public abstract C getCopy();
	
	/**
	 * Copy the values of the target configuration to this configuration
	 * @param cfg the configuration to copy 
	 */
	public void copyFrom(C cfg){
		List<GrblSetting<?>> lstTargetSettings = cfg.getLstGrblSetting();
		for (GrblSetting<?> grblSetting : lstTargetSettings) {
			this.registerSetting(grblSetting.copy());
		}
	}
	
	/**
	 * Determines if this configuration was completely assigned using at least once the setValue(..) method on every setting
	 * @return <code>true</code> if all settings were assigned, <code>false</code> otherwise
	 */
	public boolean isCompletelyLoaded(){
		for (GrblSetting<?> grblSetting : lstGrblSetting) {
			if(!grblSetting.isAssigned()){				
				return false;
			}
		}		
		return true;
	}
	/**
	 * @return the lstGrblSetting
	 */
	public List<GrblSetting<?>> getLstGrblSetting() {
		return lstGrblSetting;
	}

	/**
	 * @param lstGrblSetting the lstGrblSetting to set
	 */
	public void setLstGrblSetting(List<GrblSetting<?>> lstGrblSetting) {
		this.lstGrblSetting = lstGrblSetting;
	}

}