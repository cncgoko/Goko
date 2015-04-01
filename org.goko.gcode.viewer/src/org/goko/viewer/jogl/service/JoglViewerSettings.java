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

import javax.vecmath.Vector3f;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.util.IPropertyChangeListener;
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
	public static final String NODE = "org.goko.viewer.jogl";

	public static final String MULTISAMPLING 	= "performances.multisampling";
	public static final String MAJOR_GRID_SPACING 	= "grid.majorSpacing";
	public static final String MINOR_GRID_SPACING 	= "grid.minorSpacing";

	public static final String ROTARY_AXIS_ENABLED 	= "rotaryAxisEnabled";
	public static final String ROTARY_AXIS_DIRECTION 	= "rotaryAxisDirection";
	public static final String ROTARY_AXIS_POSITION_X 	= "rotaryAxisPositionX";
	public static final String ROTARY_AXIS_POSITION_Y 	= "rotaryAxisPositionY";
	public static final String ROTARY_AXIS_POSITION_Z 	= "rotaryAxisPositionZ";

	private ScopedPreferenceStore preferenceStore;
	private static JoglViewerSettings instance;
	private boolean rotaryAxisEnabled;
	private Tuple6b rotaryAxisPosition;
	private EnumRotaryAxisDirection rotaryAxisDirection;
	private boolean rotaryAxisDistanceMode;
	private boolean rotaryAxisTravelPerTurn;

	public enum EnumRotaryAxisDirection{
		X(1,0,0),
		Y(0,1,0),
		Z(0,0,1);

		Vector3f direction;

		private EnumRotaryAxisDirection(float x, float y, float z) {
			this.direction = new Vector3f(x,y,z);
		}

		/**
		 * @return the direction
		 */
		public Vector3f getVector3f() {
			return direction;
		}
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
		preferenceStore.setDefault(MULTISAMPLING, "1");
		preferenceStore.setDefault(MAJOR_GRID_SPACING, "5");
		preferenceStore.setDefault(MINOR_GRID_SPACING, "1");

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
		if(StringUtils.isBlank(preferenceStore.getString(MULTISAMPLING))){
			preferenceStore.setToDefault(MULTISAMPLING);
		}
		if(StringUtils.isBlank(preferenceStore.getString(MAJOR_GRID_SPACING))){
			preferenceStore.setToDefault(MAJOR_GRID_SPACING);
		}
		if(StringUtils.isBlank(preferenceStore.getString(MINOR_GRID_SPACING))){
			preferenceStore.setToDefault(MINOR_GRID_SPACING);
		}

		Tuple6b position = new Tuple6b();
		position.setX( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_X)));
		position.setY( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_Y)));
		position.setZ( new BigDecimal( preferenceStore.getString(ROTARY_AXIS_POSITION_Z)));
		setRotaryAxisPosition(position);

	}
	public void addPropertyChangeListener(IPropertyChangeListener listener){
		preferenceStore.addPropertyChangeListener(listener);
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
	 * Return the vector defining the direction of the rotary axis
	 * @return Vector3f
	 */
	public Vector3f getRotaryAxisDirectionVector() {
		switch(rotaryAxisDirection){
		case X: return new Vector3f(1,0,0);
		case Y: return new Vector3f(0,1,0);
		case Z: return new Vector3f(0,0,1);
		}
		return new Vector3f(1,0,0);
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

	/**
	 * @return the multisampling
	 */
	public Integer getMultisampling() {
		return Integer.valueOf(preferenceStore.getString(MULTISAMPLING));
	}

	/**
	 * @param multisampling the multisampling to set
	 */
	public void setMultisampling(Integer multisampling) {
		preferenceStore.setValue(MULTISAMPLING, String.valueOf(multisampling));
	}

	/**
	 * @return the majorGridSpacing
	 */
	public BigDecimal getMajorGridSpacing() {
		return new BigDecimal(preferenceStore.getString(MAJOR_GRID_SPACING));
	}

	/**
	 * @param majorGridSpacing the majorGridSpacing to set
	 */
	public void setMajorGridSpacing(BigDecimal majorGridSpacing) {
		preferenceStore.setValue(MAJOR_GRID_SPACING, String.valueOf(majorGridSpacing));
	}

	/**
	 * @return the minorGridSpacing
	 */
	public BigDecimal getMinorGridSpacing() {
		return new BigDecimal(preferenceStore.getString(MINOR_GRID_SPACING));
	}

	/**
	 * @param minorGridSpacing the minorGridSpacing to set
	 */
	public void setMinorGridSpacing(BigDecimal minorGridSpacing) {
		preferenceStore.setValue(MINOR_GRID_SPACING, String.valueOf(minorGridSpacing));
	}
}
