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

import org.goko.controller.tinyg.commons.configuration.settings.TinyGBigDecimalSetting;

/**
 * Group settings for axis without homing feature
 * @author Psyko
 * @date 15 févr. 2017
 */
public class TinyGSecondaryRotationalAxisSettings extends TinyGAxisSettings{

	public TinyGSecondaryRotationalAxisSettings(String motor){
		super(motor);
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.AXIS_MODE,  			BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.VELOCITY_MAXIMUM, 	BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.FEEDRATE_MAXIMUM, 	BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.TRAVEL_MINIMUM, 		BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.TRAVEL_MAXIMUM, 		BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.JERK_MAXIMUM, 		BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.JUNCTION_DEVIATION, 	BigDecimal.ZERO));
		addSetting(new TinyGBigDecimalSetting(TinyGAxisSettings.RADIUS_SETTING, BigDecimal.ZERO));
	}
}
