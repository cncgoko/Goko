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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.controller.tinyg.commons.configuration.settings.TinyGBigDecimalSetting;
import org.goko.controller.tinyg.commons.configuration.settings.TinyGStringSetting;
import org.goko.core.common.exception.GkException;

/**
 * A TinyG Group setting
 * 
 * @author PsyKo
 */
public class TinyGGroupSettings {
	/** The group identifier */
	private String groupIdentifier;
	/** The list of settings */
	private List<TinyGSetting<?>> settings;

	/**
	 * Constructor
	 * @param group the id of the group
	 */
	public TinyGGroupSettings(String group){
		this.groupIdentifier = group;
		this.settings = new ArrayList<TinyGSetting<?>>();
	}

	/**
	 * Add a setting to the group
	 * @param setting the setting to add
	 * @throws GkException GkException
	 */
	public void addSetting(TinyGSetting<?> setting){
		this.settings.add(setting);
	}
	
	/**
	 * Utility method to add a Big Decimal setting
	 * @param identifier identifier of the setting
	 * @param value value of the setting
	 */
	public void addSetting(String identifier, BigDecimal value){
		addSetting(new TinyGBigDecimalSetting(identifier, value));
	}
	
	/**
	 * Utility method to add a String setting
	 * @param identifier identifier of the setting
	 * @param value value of the setting
	 */
	public void addSetting(String identifier, String value){
		addSetting(new TinyGStringSetting(identifier, value));
	}
	
	/**
	 * Utility method to add a readonly Big Decimal setting
	 * @param identifier identifier of the setting
	 * @param value value of the setting
	 */
	public void addSettingReadOnly(String identifier, BigDecimal value){
		addSetting(new TinyGBigDecimalSetting(identifier, value, true));
	}
	
	/**
	 * Utility method to add a readonly String setting
	 * @param identifier identifier of the setting
	 * @param value value of the setting
	 */
	public void addSettingReadOnly(String identifier, String value){
		addSetting(new TinyGStringSetting(identifier, value, true));
	}
	
	/**
	 * @return the groupIdentifier
	 */
	public String getGroupIdentifier() {
		return groupIdentifier;
	}

	/**
	 * @param groupIdentifier the groupIdentifier to set
	 */
	public void setGroupIdentifier(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;
	}

	/**
	 * @return the settings
	 */
	public List<TinyGSetting<?>> getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(List<TinyGSetting<?>> settings) {
		this.settings = settings;
	}
	
	/**
	 * Determines if this group was completely assigned using at least once the setValue(..) method on every setting
	 * @return <code>true</code> if all settings were assigned, <code>false</code> otherwise
	 */
	public boolean isCompletelyLoaded(){
		for (TinyGSetting<?> tinyGSetting : settings) {
			if(!tinyGSetting.isReadOnly() && !tinyGSetting.isAssigned()){
				System.err.print(tinyGSetting.getIdentifier()+" is not init ");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns a copy of this group object
	 * @return TinyGGroupSettings
	 */
	protected TinyGGroupSettings copy(){
		TinyGGroupSettings group = new TinyGGroupSettings(groupIdentifier);
		for (TinyGSetting<?> tinyGSetting : settings) {			
			group.addSetting(tinyGSetting.copy());
		}
		return group;
	}
}
