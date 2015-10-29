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
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.Unit;

public class NumberQuantity {

	public static <Q extends Quantity<Q>> DoubleQuantity<Q> of(double value, Unit<Q> unit){
		return new DoubleQuantity<Q>(unit, value);
	}

	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> of(BigDecimal value, Unit<Q> unit){
		return new BigDecimalQuantity<Q>(unit, value);
	}
	
	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> of(String value, Unit<Q> unit){
		return new BigDecimalQuantity<Q>(unit, new BigDecimal(value));
	}
	
	/**
	 * Addition of 2 BigDecimalQuantity with <code>null</code> handling
	 * @param v1 value 1
	 * @param v2 value 2
	 * @return the sum of the 2 values. A <code>null</code> input is considered zero
	 */
	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> add(BigDecimalQuantity<Q> v1, BigDecimalQuantity<Q> v2){
		if(v1 != null && v2 != null){
			return v1.add(v2);
		}else if(v1 != null){
			return v1;
		}
		return v2;
	}

	public static <Q extends Quantity<Q>> Quantity<Q> zero(Unit<Q> unit) {
		return NumberQuantity.of(0, unit);
	}	

}
