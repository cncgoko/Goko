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

/**
 * Definition of an G2Core linear axes configuration
 * 
 * @author Psyko
 * @date 20 janv. 2017
 */
public class G2CoreLinearAxisSettings extends TinyGGroupSettings{

	/**
	 * Constructor
	 * @param axis the name of the axis
	 */
	public G2CoreLinearAxisSettings(String axis){
		super(axis);
		addSetting(G2Core.Configuration.Axes.AXIS_MODE,  		BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.VELOCITY_MAXIMUM, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.FEEDRATE_MAXIMUM, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.TRAVEL_MINIMUM, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.TRAVEL_MAXIMUM, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.JERK_MAXIMUM, 		BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.JERK_HOMING, 		BigDecimal.ZERO);		
		addSetting(G2Core.Configuration.Axes.HOMING_INPUT, 		BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.HOMING_DIRECTION, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.SEARCH_VELOCITY, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.LATCH_VELOCITY, 	BigDecimal.ZERO);		
		addSetting(G2Core.Configuration.Axes.LATCH_BACKOFF, 	BigDecimal.ZERO);
		addSetting(G2Core.Configuration.Axes.ZERO_BACKOFF, 		BigDecimal.ZERO);
	}
}
