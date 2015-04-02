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
package org.goko.tinyg.configuration.bindings.wrapper;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.tinyg.controller.configuration.TinyGGroupSettings;
import org.goko.tinyg.controller.configuration.TinyGLinearAxisSettings;
import org.goko.tinyg.controller.configuration.TinyGSetting;

public class TinyGLinearAxisSettingsWrapper extends AbstractModelObject{
	protected TinyGConfiguration configuration;
	protected String groupIdentifier;

	public TinyGLinearAxisSettingsWrapper(TinyGConfiguration configuration, String groupIdentifier) {
		if(configuration == null){
			this.configuration = new TinyGConfiguration(); // FIXME, à enlever
		}else{
			this.configuration = configuration;
		}
		this.groupIdentifier = groupIdentifier;
	}

	public void setAm(BigDecimal axisMode) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.AXIS_MODE, getAm(), axisMode);
	}

	public BigDecimal getAm() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.AXIS_MODE, BigDecimal.class);
	}

	public void setVm(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.VELOCITY_MAXIMUM, getVm(), value);
	}

	public BigDecimal getVm() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.VELOCITY_MAXIMUM, BigDecimal.class);
	}

	public void setFr(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.FEEDRATE_MAXIMUM, getFr(), value);
	}

	public BigDecimal getFr() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.FEEDRATE_MAXIMUM, BigDecimal.class);
	}

	public void setTn(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.TRAVEL_MINIMUM, getTn(), value);
	}

	public BigDecimal getTn() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.TRAVEL_MINIMUM, BigDecimal.class);
	}

	public void setTm(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.TRAVEL_MAXIMUM, getTm(), value);
	}

	public BigDecimal getTm() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.TRAVEL_MAXIMUM, BigDecimal.class);
	}

	public void setJm(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.JERK_MAXIMUM, getJm(), value);
	}

	public BigDecimal getJm() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.JERK_MAXIMUM, BigDecimal.class);
	}

	public void setJh(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.JERK_HOMING, getJh(), value);
	}

	public BigDecimal getJh() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.JERK_HOMING, BigDecimal.class);
	}

	public void setJd(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.JUNCTION_DEVIATION, getJd(), value);
	}

	public BigDecimal getJd() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.JUNCTION_DEVIATION, BigDecimal.class);
	}

	public void setSn(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.MINIMUM_SWITCH_MODE, getSn(), value);
	}

	public BigDecimal getSn() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.MINIMUM_SWITCH_MODE, BigDecimal.class);
	}

	public void setSx(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.MAXIMUM_SWITCH_MODE, getSx(), value);
	}

	public BigDecimal getSx() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.MAXIMUM_SWITCH_MODE, BigDecimal.class);
	}

	public void setSv(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.SEARCH_VELOCITY, getSv(), value);
	}

	public BigDecimal getLv() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.MAXIMUM_SWITCH_MODE, BigDecimal.class);
	}

	public void setLv(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.LATCH_VELOCITY, getLv(), value);
	}

	public BigDecimal getSv() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.LATCH_VELOCITY, BigDecimal.class);
	}

	public void setLb(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.LATCH_BACKOFF, getLb(), value);
	}

	public BigDecimal getLb() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.LATCH_BACKOFF, BigDecimal.class);
	}

	public void setZb(BigDecimal value) throws GkException{
		setSettingValue(TinyGLinearAxisSettings.ZERO_BACKOFF, getZb(), value);
	}

	public BigDecimal getZb() throws GkException{
		return configuration.getSetting(groupIdentifier, TinyGLinearAxisSettings.ZERO_BACKOFF, BigDecimal.class);
	}



	protected void setSettingValue(String settingIdentifier, Object oldValue, Object newValue) throws GkException{
		configuration.setSetting(groupIdentifier, settingIdentifier, newValue);
		firePropertyChange(settingIdentifier, oldValue, newValue);
	}
	/**
	 * @return the configuration
	 */
	public TinyGConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(TinyGConfiguration newConfiguration) {
		this.configuration = newConfiguration;
		if(configuration != null){
			TinyGGroupSettings group = configuration.getGroup(groupIdentifier);
			if(CollectionUtils.isNotEmpty(group.getSettings())){
				for (TinyGSetting setting : group.getSettings()) {
					firePropertyChange(setting.getIdentifier(), null, setting.getValue());
				}
			}
		}
	}

	protected void refreshAllValues(){

	}
}
