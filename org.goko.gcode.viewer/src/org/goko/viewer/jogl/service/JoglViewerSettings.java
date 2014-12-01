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
package org.goko.viewer.jogl.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.log.GkLog;

/**
 * Jogl Viewer settings
 *
 * @author PsyKo
 *
 */
public class JoglViewerSettings {
	private static final GkLog LOG = GkLog.getLogger(JoglViewerSettings.class);
	private static final String NODE = "org.goko.viewer.jogl";
	private static final String ROTARY_AXIS_ENABLED 	= "rotaryAxisEnabled";
	private static final String ROTARY_AXIS_DIRECTION 	= "rotaryAxisDirection";
	private static final String ROTARY_AXIS_POSITION_X 	= "rotaryAxisPositionX";
	private static final String ROTARY_AXIS_POSITION_Y 	= "rotaryAxisPositionY";
	private static final String ROTARY_AXIS_POSITION_Z 	= "rotaryAxisPositionZ";
	private ScopedPreferenceStore preferenceStore;
	private static JoglViewerSettings instance;
	private boolean rotaryAxisEnabled;
	private Tuple6b rotaryAxisPosition;
	private EnumRotaryAxisDirection rotaryAxisDirection;
	private boolean rotaryAxisDistanceMode;
	private boolean rotaryAxisTravelPerTurn;

	public enum EnumRotaryAxisDirection{
		X,
		Y,
		Z;
	}

	private JoglViewerSettings(){
		preferenceStore = new ScopedPreferenceStore(ConfigurationScope.INSTANCE,NODE);
	}

	public static JoglViewerSettings getInstance() {
		if(instance == null){
			instance = new JoglViewerSettings();
			instance.initialiseValues();
		}
		return instance;
	}

	private void initialiseValues() {
		preferenceStore.setDefault(ROTARY_AXIS_ENABLED, true);
		preferenceStore.setDefault(ROTARY_AXIS_DIRECTION, "X");
		preferenceStore.setDefault(ROTARY_AXIS_POSITION_X, "0");
		preferenceStore.setDefault(ROTARY_AXIS_POSITION_Y, "0");
		preferenceStore.setDefault(ROTARY_AXIS_POSITION_Z, "0");

		setRotaryAxisEnabled(preferenceStore.getBoolean(ROTARY_AXIS_ENABLED));
		if(StringUtils.isBlank(preferenceStore.getString(ROTARY_AXIS_DIRECTION))){
			preferenceStore.setToDefault(ROTARY_AXIS_DIRECTION);
		}
		setRotaryAxisDirection( EnumRotaryAxisDirection.valueOf(preferenceStore.getString(ROTARY_AXIS_DIRECTION)));

		if(StringUtils.isBlank(preferenceStore.getString(ROTARY_AXIS_POSITION_X))){
			preferenceStore.setToDefault(ROTARY_AXIS_POSITION_X);
		}
		if(StringUtils.isBlank(preferenceStore.getString(ROTARY_AXIS_POSITION_Y))){
			preferenceStore.setToDefault(ROTARY_AXIS_POSITION_Y);
		}
		if(StringUtils.isBlank(preferenceStore.getString(ROTARY_AXIS_POSITION_Z))){
			preferenceStore.setToDefault(ROTARY_AXIS_POSITION_Z);
		}

		Tuple6b position = new Tuple6b();
		position.setX( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_X)));
		position.setY( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_Y)));
		position.setZ( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_Z)));
		setRotaryAxisPosition(position);

	}

	/**
	 * @return the rotaryAxisPosition
	 */
	public Tuple6b getRotaryAxisPosition() {
		return rotaryAxisPosition;
	}
	/**
	 * @param rotaryAxisPosition the rotaryAxisPosition to set
	 */
	public void setRotaryAxisPosition(Tuple6b rotaryAxisPosition) {
		this.rotaryAxisPosition = rotaryAxisPosition;
		preferenceStore.setValue(ROTARY_AXIS_POSITION_X, rotaryAxisPosition.getX().toPlainString());
		preferenceStore.setValue(ROTARY_AXIS_POSITION_Y, rotaryAxisPosition.getY().toPlainString());
		preferenceStore.setValue(ROTARY_AXIS_POSITION_Z, rotaryAxisPosition.getZ().toPlainString());
	}
	/**
	 * @return the rotaryAxisDirection
	 */
	public EnumRotaryAxisDirection getRotaryAxisDirection() {
		return rotaryAxisDirection;
	}
	/**
	 * @param rotaryAxisDirection the rotaryAxisDirection to set
	 */
	public void setRotaryAxisDirection(EnumRotaryAxisDirection rotaryAxisDirection) {
		this.rotaryAxisDirection = rotaryAxisDirection;
		preferenceStore.setValue(ROTARY_AXIS_DIRECTION, rotaryAxisDirection.name());
	}
	/**
	 * @return the rotaryAxisDistanceMode
	 */
	public boolean isRotaryAxisDistanceMode() {
		return rotaryAxisDistanceMode;
	}
	/**
	 * @param rotaryAxisDistanceMode the rotaryAxisDistanceMode to set
	 */
	public void setRotaryAxisDistanceMode(boolean rotaryAxisDistanceMode) {
		this.rotaryAxisDistanceMode = rotaryAxisDistanceMode;
	}
	/**
	 * @return the rotaryAxisEnabled
	 */
	public boolean isRotaryAxisEnabled() {
		return rotaryAxisEnabled;
	}

	/**
	 * @param rotaryAxisEnabled the rotaryAxisEnabled to set
	 */
	public void setRotaryAxisEnabled(boolean rotaryAxisEnabled) {
		this.rotaryAxisEnabled = rotaryAxisEnabled;
		preferenceStore.setValue(ROTARY_AXIS_ENABLED, rotaryAxisEnabled);
	}

	/**
	 * @return the rotaryAxisTravelPerTurn
	 */
	public boolean isRotaryAxisTravelPerTurn() {
		return rotaryAxisTravelPerTurn;
	}

	/**
	 * @param rotaryAxisTravelPerTurn the rotaryAxisTravelPerTurn to set
	 */
	public void setRotaryAxisTravelPerTurn(boolean rotaryAxisTravelPerTurn) {
		this.rotaryAxisTravelPerTurn = rotaryAxisTravelPerTurn;
	}

	private void savePreferences(){
		try {
			preferenceStore.save();
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	public void save() {
		savePreferences();

	}
}
