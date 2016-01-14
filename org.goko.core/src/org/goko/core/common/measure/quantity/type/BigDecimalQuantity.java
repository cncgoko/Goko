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
import java.math.RoundingMode;

import org.goko.core.common.measure.converter.UnitConverter;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(value)+getUnit().getSymbol();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.AbstractQuantity#internalDoubleValue()
	 */
	@Override
	protected double internalDoubleValue() {
		return this.value.doubleValue();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#value()
	 */
	@Override
	public BigDecimal value() {
		return value;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#value(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public BigDecimal value(Unit<Q> unit) {
		return this.to(unit).value();
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#to(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public BigDecimalQuantity<Q> to(Unit<Q> unit) {
		if(unit == getUnit()){
			return this;
		}
		UnitConverter converter = getUnit().getConverterTo(unit);
		return NumberQuantity.of(new BigDecimal(converter.convert(internalDoubleValue())), unit);
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#add(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public BigDecimalQuantity<Q> add(Quantity<Q> q) {
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() + q.doubleValue(getUnit())));
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.AbstractQuantity#add(org.goko.core.common.measure.quantity.AbstractQuantity)
	 */
	public BigDecimalQuantity<Q> add(BigDecimalQuantity<Q> q) {
		return new BigDecimalQuantity<Q>(getUnit(), value.add( q.value(getUnit()) ));
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#subtract(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public BigDecimalQuantity<Q> subtract(Quantity<Q> q) {
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() - q.doubleValue(getUnit())));
	}


	public BigDecimalQuantity<Q> subtract(BigDecimalQuantity<Q> q) {
		return new BigDecimalQuantity<Q>(getUnit(), value.subtract( q.to(getUnit()).getValue()));
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#multiply(java.lang.Number)
	 */
	@Override
	public BigDecimalQuantity<Q> multiply(Number n) {
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() * n.doubleValue()));
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(java.lang.Number)
	 */
	@Override
	public BigDecimalQuantity<Q> divide(Number n) {
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() / n.doubleValue()));
	}

	public BigDecimalQuantity<Q> divide(BigDecimal n) {
		return new BigDecimalQuantity<Q>(getUnit(), value.divide(n, 10, RoundingMode.HALF_UP));
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public Number divide(Quantity<Q> q) {
		return value.divide(new BigDecimal(q.doubleValue(getUnit())), value.scale(), BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal divide(BigDecimalQuantity<Q> q) {
		return value.divide(q.value(getUnit()), 10, RoundingMode.HALF_UP); // Hack. Rounding mode should not be used in this case
	}

	public boolean equals(BigDecimalQuantity<Q> obj) {
		return value.equals(obj.value(getUnit()));
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BigDecimalQuantity<Q> other = (BigDecimalQuantity<Q>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#abs()
	 */
	@Override
	public BigDecimalQuantity<Q> abs() {
		return NumberQuantity.of(value.abs(), getUnit());
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#negate()
	 */
	@Override
	public BigDecimalQuantity<Q> negate() {
		return NumberQuantity.of(value.negate(), getUnit());
	}

	public boolean lowerThan(BigDecimalQuantity<Q> quantity) {
		return value.compareTo(quantity.value(getUnit())) < 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#lowerThanOrEqualTo(org.goko.core.common.measure.quantity.Quantity)
	 */
	public boolean lowerThanOrEqualTo(BigDecimalQuantity<Q> quantity) {
		return value.compareTo(quantity.value(getUnit())) <= 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#greaterThan(org.goko.core.common.measure.quantity.Quantity)
	 */
	public boolean greaterThan(BigDecimalQuantity<Q> quantity) {
		return value.compareTo(quantity.value(getUnit())) > 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#greaterThanOrEqualTo(org.goko.core.common.measure.quantity.Quantity)
	 */
	public boolean greaterThanOrEqualTo(BigDecimalQuantity<Q> quantity) {
		return value.compareTo(quantity.value(getUnit())) >= 0;
	}



}
