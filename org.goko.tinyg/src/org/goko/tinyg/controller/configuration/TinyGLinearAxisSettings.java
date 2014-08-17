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
package org.goko.tinyg.controller.configuration;

import java.math.BigDecimal;

import org.goko.tinyg.controller.configuration.type.TinyGBigDecimalSetting;

public class TinyGLinearAxisSettings extends TinyGAxisSettings{

	public TinyGLinearAxisSettings(String motor){
		super(motor);
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.AXIS_MODE,  			new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.VELOCITY_MAXIMUM, 	new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.FEEDRATE_MAXIMUM, 	new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.TRAVEL_MINIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.TRAVEL_MAXIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.JERK_MAXIMUM, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.JERK_HOMING, 			new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.JUNCTION_DEVIATION, 	new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.MINIMUM_SWITCH_MODE, 	new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.MAXIMUM_SWITCH_MODE, 	new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.SEARCH_VELOCITY, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.LATCH_VELOCITY, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.ZERO_BACKOFF, 		new BigDecimal("0")));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.LATCH_BACKOFF, 		new BigDecimal("0")));
	}
}
