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
package org.goko.controller.tinyg.controller.configuration;



public class TinyGAxisSettings extends TinyGGroupSettings {

	public static final String AXIS_MODE 			= "am";
	public static final String VELOCITY_MAXIMUM 	= "vm";
	public static final String FEEDRATE_MAXIMUM 	= "fr";
	public static final String TRAVEL_MINIMUM 		= "tn";
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
	public static final String LATCH_BACKOFF 		= "lb";

	public TinyGAxisSettings(String motor){
		super(motor);
	}
}
