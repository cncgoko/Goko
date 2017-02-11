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

/**
 * Definition of an G2Core rotary axes configuration
 * 
 * @author Psyko
 * @date 20 janv. 2017
 */
public class G2CoreRotationalAxisSettings extends G2CoreLinearAxisSettings{

	/**
	 * Constructor
	 * @param axis the name of the axis
	 */
	public G2CoreRotationalAxisSettings(String axis){
		super(axis);
		addSetting(G2Core.Configuration.Axes.RADIUS_SETTING, 	BigDecimal.ZERO);
	}
}
