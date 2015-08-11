/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.common.measure.dimension;

import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.Unit;

public class QuantityDimension implements Dimension {
	public static final Dimension NONE = new QuantityDimension(AbstractUnit.GENERIC);
	public static final Dimension LENGTH = new QuantityDimension("m");
	public static final Dimension ANGLE  = new QuantityDimension("°");

	private Unit<?> internUnit;

	public QuantityDimension(String symbol) {
		internUnit = new BaseUnit<>(symbol, NONE);
	}

	public QuantityDimension(Unit<?> unit) {
		this.internUnit = unit;
	}

}
