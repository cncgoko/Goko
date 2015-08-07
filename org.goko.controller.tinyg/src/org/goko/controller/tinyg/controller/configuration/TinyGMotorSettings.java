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

import org.goko.controller.tinyg.controller.configuration.type.TinyGBigDecimalSetting;

public class TinyGMotorSettings extends TinyGGroupSettings {
	private static final String MOTOR_AXIS_MAPPING 		= "ma";
	private static final String STEP_ANGLE 				= "sa";
	private static final String TRAVEL_PER_REVOLUTION 	= "tr";
	private static final String MICROSTEPS 				= "mi";
	private static final String POLARITY 				= "po";
	private static final String POWER_MODE 				= "pm";



	public TinyGMotorSettings(String motor){
		super(motor);
		addSetting(new TinyGBigDecimalSetting(MOTOR_AXIS_MAPPING, 	BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(STEP_ANGLE, 			BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TRAVEL_PER_REVOLUTION,BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(MICROSTEPS, 			BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(POLARITY, 			BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(POWER_MODE, 			BigDecimal.ZERO));
	}


}
