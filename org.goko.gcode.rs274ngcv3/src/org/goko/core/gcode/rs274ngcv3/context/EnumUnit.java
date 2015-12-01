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

import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;

/**
 * GCode units
 *
 * @author PsyKo
 *
 */
public enum EnumUnit {
	MILLIMETERS(SIPrefix.MILLI(Units.METRE)),
	INCHES(Units.INCH);
	
	private Unit<Length> unit;

	private EnumUnit(Unit<Length> unit) {
		this.unit = unit;
	}

	public Unit<Length> getUnit() {
		return unit;
	}
}
