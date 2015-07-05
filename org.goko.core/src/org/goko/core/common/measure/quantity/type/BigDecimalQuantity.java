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

	@Override
	public String toString() {
		return String.valueOf(value)+" "+getUnit().getSymbol();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#doubleValue()
	 */
	@Override
	public double doubleValue() {
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
		return NumberQuantity.of(new BigDecimal(converter.convert(doubleValue())), unit); 
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#add(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public BigDecimalQuantity<Q> add(Quantity<Q> q) {		
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() + q.to(getUnit()).doubleValue()));
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
		return new BigDecimalQuantity<Q>(getUnit(), new BigDecimal(value.doubleValue() - q.to(getUnit()).doubleValue()));
	}

	public BigDecimalQuantity<Q> subtract(BigDecimalQuantity<Q> q) {		
		return new BigDecimalQuantity<Q>(getUnit(), value.subtract( new BigDecimal(q.to(getUnit()).doubleValue()) ));
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigDecimalQuantity other = (BigDecimalQuantity) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
