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

import org.goko.core.common.measure.quantity.AbstractQuantity;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.Unit;

public class BigDecimalQuantity<Q extends Quantity<Q>> extends AbstractQuantity<Q> {
	private BigDecimal value;


	BigDecimalQuantity(Unit<Q> unit, BigDecimal value) {
		super(unit);
		this.value = value;
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#doubleValue()
	 */
	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}


	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

}
