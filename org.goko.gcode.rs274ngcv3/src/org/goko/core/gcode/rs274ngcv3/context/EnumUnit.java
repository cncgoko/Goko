/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.rs274ngcv3.context;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.units.Unit;

/**
 * GCode units
 *
 * @author PsyKo
 *
 */
public enum EnumUnit {
	MILLIMETERS(LengthUnit.MILLIMETRE, SpeedUnit.MILLIMETRE_PER_MINUTE),
	INCHES(LengthUnit.INCH, SpeedUnit.INCH_PER_MINUTE);
	
	private Unit<Length> unit;
	private Unit<Speed> feedUnit;
	
	private EnumUnit(Unit<Length> unit, Unit<Speed> feedUnit) {
		this.unit = unit;
		this.feedUnit = feedUnit;
	}

	public Unit<Length> getUnit() {
		return unit;
	}

	/**
	 * @return the feedUnit
	 */
	public Unit<Speed> getFeedUnit() {
		return feedUnit;
	}
}
