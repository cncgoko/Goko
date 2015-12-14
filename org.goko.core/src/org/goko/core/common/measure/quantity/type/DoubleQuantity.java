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

import org.goko.core.common.measure.quantity.AbstractQuantity;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.Unit;

public class DoubleQuantity<Q extends Quantity<Q>> extends AbstractQuantity<Q>{
	private double value;


	DoubleQuantity(Unit<Q> unit, double value) {
		super(unit);
		this.value = value;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.AbstractQuantity#internalDoubleValue()
	 */
	@Override
	public double internalDoubleValue() {
		return this.value;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#value()
	 */
	@Override
	public Double value() {		
		return internalDoubleValue();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#value(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public Number value(Unit<Q> unit) {		
		return this.to(unit).value();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#add(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public DoubleQuantity<Q> add(Quantity<Q> q) {
		return new DoubleQuantity<Q>(getUnit(), q.doubleValue(getUnit()) + value);
	}

	
	public DoubleQuantity<Q> add(DoubleQuantity<Q> q) {
		return new DoubleQuantity<Q>(getUnit(), q.doubleValue(getUnit()) + value);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#subtract(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public Quantity<Q> subtract(Quantity<Q> q) {
		return new DoubleQuantity<Q>(getUnit(), value - q.doubleValue(getUnit()));
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#multiply(java.lang.Number)
	 */
	@Override
	public Quantity<Q> multiply(Number n) {
		return new DoubleQuantity<Q>(getUnit(), n.doubleValue() * value);
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(java.lang.Number)
	 */
	@Override
	public Quantity<Q> divide(Number n) {
		return new DoubleQuantity<Q>(getUnit(), value / n.doubleValue());
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public Number divide(Quantity<Q> q) {		
		return value / q.doubleValue(getUnit());
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#abs()
	 */
	@Override
	public DoubleQuantity<Q> abs() {		
		return NumberQuantity.of(Math.abs(value), getUnit());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#negate()
	 */
	@Override
	public Quantity<Q> negate() {
		return NumberQuantity.of(-value, getUnit());
	}


}
