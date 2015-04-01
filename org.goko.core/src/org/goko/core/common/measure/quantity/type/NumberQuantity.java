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

package org.goko.core.common.measure.quantity.type;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.Unit;

public class NumberQuantity {

	public static <Q extends Quantity<Q>> DoubleQuantity<Q> of(double value, Unit<Q> unit){
		return new DoubleQuantity<Q>(unit, value);
	}

	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> of(BigDecimal value, Unit<Q> unit){
		return new BigDecimalQuantity<Q>(unit, value);
	}
}
