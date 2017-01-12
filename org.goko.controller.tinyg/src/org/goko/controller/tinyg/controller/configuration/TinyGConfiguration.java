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
package org.goko.controller.tinyg.controller.configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;
import org.goko.controller.tinyg.controller.configuration.type.TinyGBigDecimalSetting;
import org.goko.controller.tinyg.controller.configuration.type.TinyGStringSetting;
import org.goko.core.common.exception.GkException;



/**
 * A Class containing all the parameters of TinyG's configuration
 *
 * @author PsyKo
 *
 */
public class TinyGConfiguration extends AbstractTinyGConfiguration<TinyGConfiguration>{
	public static final String SYSTEM_SETTINGS = "sys";

	public static final String X_AXIS_SETTINGS = "x";
	public static final String Y_AXIS_SETTINGS = "y";
	public static final String Z_AXIS_SETTINGS = "z";
	public static final String A_AXIS_SETTINGS = "a";
	public static final String B_AXIS_SETTINGS = "b";
	public static final String C_AXIS_SETTINGS = "c";

	public static final String MOTOR_1_SETTINGS = "1";
	public static final String MOTOR_2_SETTINGS = "2";
	public static final String MOTOR_3_SETTINGS = "3";
	public static final String MOTOR_4_SETTINGS = "4";

	public static final String FIRMWARE_BUILD 			= "fb";
	public static final String FIRMWARE_VERSION 		= "fv";
	public static final String HARDWARE_VERSION 		= "hv";
	public static final String UNIQUE_ID				= "id";

	public static final String JUNCTION_ACCELERATION 	= "ja";
	public static final String CHORDAL_TOLERANCE 		= "ct";
	public static final String SWITCH_TYPE 				= "st";
	public static final String MOTOR_DISABLE_TIMEOUT	= "mt";

	public static final String JSON_MODE 				= "ej";
	public static final String JSON_VERBOSITY 			= "jv";
	public static final String JSON_SYNTAX	 			= "js";
	public static final String TEXT_MODE_VERBOSITY 		= "tv";
	public static final String QUEUE_REPORT_VERBOSITY	= "qv";
	public static final String STATUS_REPORT_VERBOSITY 	= "sv";
	public static final String STATUS_REPORT_INTERVAL 	= "si";
	//public static final String IGNORE_CR_LF_ON_RX 		= "ic";
	public static final String ENABLE_CR_ON_TX 			= "ec";
	public static final String ENABLE_CHARACTER_ECHO 	= "ee";
	public static final String ENABLE_FLOW_CONTROL 		= "ex";
	public static final String ENABLE_SOFT_LIMIT		= "sl";
	public static final String BAUD_RATE 				= "baud";

	public static final String DEFAULT_PLANE_SELECTION	= "gpl";
	public static final String DEFAULT_UNITS_MODE		= "gun";
	public static final String DEFAULT_COORDINATE_SYSTEM= "gco";
	public static final String DEFAULT_PATH_CONTROL		= "gpa";
	public static final String DEFAULT_DISTANCE_MODE	= "gdi";

	// Axis settings
//	public static final String AXIS_MODE 			= "am";
//	public static final String VELOCITY_MAXIMUM 	= "vm";
//	public static final String FEEDRATE_MAXIMUM 	= "fr";
//	public static final String TRAVEL_MAXIMUM 		= "tm";
//	public static final String JERK_MAXIMUM 		= "jm";
//	public static final String JERK_HOMING 			= "jh";
//	public static final String JUNCTION_DEVIATION	= "jd";
//	public static final String RADIUS_SETTING 		= "ra";
//	public static final String MINIMUM_SWITCH_MODE 	= "sn";
//	public static final String MAXIMUM_SWITCH_MODE 	= "sx";
//	public static final String SEARCH_VELOCITY		= "sv";
//	public static final String LATCH_VELOCITY 		= "lv";
//	public static final String ZERO_BACKOFF 		= "zb";

	// Motor settings
	public static final String MOTOR_MAPPING		= "ma";
	public static final String STEP_ANGLE			= "sa";
	public static final String TRAVEL_PER_REVOLUTION= "tr";
	public static final String MICROSTEPS			= "mi";
	public static final String POLARITY				= "po";
	public static final String POWER_MANAGEMENT_MODE= "pm";

	private List<TinyGGroupSettings> groups;

	public TinyGConfiguration(){
		this(true);
	}
	
	private TinyGConfiguration(boolean initializeGroups){
		groups   = new ArrayList<TinyGGroupSettings>();
		if(initializeGroups){
			initSettings();
		}
	}

	private void initSettings() {
		groups.add(new TinyGMotorSettings("1"));
		groups.add(new TinyGMotorSettings("2"));
		groups.add(new TinyGMotorSettings("3"));
		groups.add(new TinyGMotorSettings("4"));

		groups.add( new TinyGLinearAxisSettings(X_AXIS_SETTINGS));
		groups.add( new TinyGLinearAxisSettings(Y_AXIS_SETTINGS));
		groups.add( new TinyGLinearAxisSettings(Z_AXIS_SETTINGS));
		groups.add( new TinyGRotationalAxisSettings(A_AXIS_SETTINGS));
		//groups.add( new TinyGAxisSettings(B_AXIS_SETTINGS));
		//groups.add( new TinyGAxisSettings(C_AXIS_SETTINGS));

		TinyGGroupSettings sysgroup = new TinyGGroupSettings(SYSTEM_SETTINGS);
		sysgroup.addSetting(new TinyGBigDecimalSetting(FIRMWARE_BUILD 					,BigDecimal.ZERO, true));
		sysgroup.addSetting(new TinyGBigDecimalSetting(FIRMWARE_VERSION 				,BigDecimal.ZERO, true));
		sysgroup.addSetting(new TinyGBigDecimalSetting(HARDWARE_VERSION 			 	,BigDecimal.ZERO, true));
		sysgroup.addSetting(new TinyGStringSetting(UNIQUE_ID						,"0", true));

		sysgroup.addSetting(new TinyGBigDecimalSetting(JUNCTION_ACCELERATION 		,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(CHORDAL_TOLERANCE 			,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(SWITCH_TYPE 					,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(MOTOR_DISABLE_TIMEOUT		,BigDecimal.ZERO));

		sysgroup.addSetting(new TinyGBigDecimalSetting(JSON_MODE 					,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(JSON_VERBOSITY 				,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(JSON_SYNTAX	 				,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(TEXT_MODE_VERBOSITY 			,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(QUEUE_REPORT_VERBOSITY		,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(STATUS_REPORT_VERBOSITY 		,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(STATUS_REPORT_INTERVAL 		,BigDecimal.ZERO));
	//	sysgroup.addSetting(new TinyGBigDecimalSetting(IGNORE_CR_LF_ON_RX 			,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(ENABLE_CR_ON_TX 				,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(ENABLE_CHARACTER_ECHO 		,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(ENABLE_FLOW_CONTROL 			,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(BAUD_RATE 					,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(ENABLE_SOFT_LIMIT			,BigDecimal.ZERO));

		sysgroup.addSetting(new TinyGBigDecimalSetting(DEFAULT_PLANE_SELECTION	,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(DEFAULT_UNITS_MODE			,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(DEFAULT_COORDINATE_SYSTEM 	,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(DEFAULT_PATH_CONTROL		,BigDecimal.ZERO));
		sysgroup.addSetting(new TinyGBigDecimalSetting(DEFAULT_DISTANCE_MODE 		,BigDecimal.ZERO));

		addGroup(sysgroup);
	}

	/**
	 * Returns the setting as a String
	 * @param identifier the identifier
	 * @return the String value
	 * @throws GkException GkException
	 */
	public String getSetting(String identifier) throws GkException{
		return getSetting(identifier, String.class);
	}

	/**
	 * Returns the setting as the specified type
	 * @param identifier the identifier
	 * @param clazz the expected type
	 * @return the value as clazz
	 * @throws GkException GkException
	 */
	public <T> T getSetting(String identifier, Class<T> clazz) throws GkException{
		return getSetting(SYSTEM_SETTINGS, identifier, clazz);
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
		
	public TinyGConfiguration copy(){
		TinyGConfiguration copy = new TinyGConfiguration(false);
		copy.copyFrom(this);
		return copy;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration#newInstance()
	 */
	@Override
	protected TinyGConfiguration newInstance() {
		return new TinyGConfiguration(false);
	}
}
