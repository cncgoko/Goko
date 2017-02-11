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
package org.goko.controller.g2core.configuration;

import java.math.BigDecimal;

import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;

public class G2CoreMotorSettings extends TinyGGroupSettings {

	public G2CoreMotorSettings(String motor){
		super(motor);
		addSetting(G2Core.Configuration.Motors.MOTOR_MAPPING, 		BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.STEP_ANGLE, 			BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.TRAVEL_PER_REVOLUTION,BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.MICROSTEPS, 			BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.STEPS_PER_UNIT,		BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.POLARITY, 			BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.POWER_MODE, 			BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Motors.POWER_LEVEL,			BigDecimal.ZERO);
	}


}
