/*
 *
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
package org.goko.tinyg.controller.configuration;

import java.math.BigDecimal;


public class TinyGAxisSettings extends TinyGGroupSettings {

	public static final String AXIS_MODE 			= "am";
	public static final String VELOCITY_MAXIMUM 	= "vm";
	public static final String FEEDRATE_MAXIMUM 	= "fr";
	public static final String TRAVEL_MAXIMUM 		= "tm";
	public static final String JERK_MAXIMUM 		= "jm";
	public static final String JERK_HOMING 			= "jh";
	public static final String JUNCTION_DEVIATION	= "jd";
	public static final String RADIUS_SETTING 		= "ra";
	public static final String MINIMUM_SWITCH_MODE 	= "sn";
	public static final String MAXIMUM_SWITCH_MODE 	= "sx";
	public static final String SEARCH_VELOCITY		= "sv";
	public static final String LATCH_VELOCITY 		= "lv";
	public static final String ZERO_BACKOFF 		= "zb";

	public TinyGAxisSettings(String motor){
		super(motor);
		addSetting(new TinyGSetting<BigDecimal>(AXIS_MODE,  new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(VELOCITY_MAXIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(FEEDRATE_MAXIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(TRAVEL_MAXIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(JERK_MAXIMUM, 			new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(JERK_HOMING, 			new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(JUNCTION_DEVIATION, 	new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(RADIUS_SETTING, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(MINIMUM_SWITCH_MODE, 	new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(MAXIMUM_SWITCH_MODE, 	new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(SEARCH_VELOCITY, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(LATCH_VELOCITY, 		new BigDecimal("0")));
		addSetting(new TinyGSetting<BigDecimal>(ZERO_BACKOFF, 			new BigDecimal("0")));
	}
}
