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

package org.goko.core.common.measure.quantity;

import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.Unit;


public abstract class AbstractQuantity<Q extends Quantity<Q>> implements Quantity<Q> {
	private Unit<Q> unit;

	protected AbstractQuantity(Unit<Q> unit) {
		super();
		this.unit = unit;
	}

	protected UnitConverter getConverterTo(Unit<Q> unit) throws GkTechnicalException{
		if(unit instanceof AbstractUnit<?>){
			throw new GkTechnicalException("Incompatible units...");
		}
		return this.getUnit().getConverterToReferenceUnit().then(unit.getConverterToReferenceUnit().inverse());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#doubleValue(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public double doubleValue(Unit<Q> unit){
		if(this.unit.equals(unit)){
			return internalDoubleValue();
		}
		return this.to(unit).doubleValue(unit);
	}
	
	protected abstract double internalDoubleValue();
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#to(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public Quantity<Q> to(Unit<Q> unit) {
		if(unit == getUnit()){
			return this;
		}
		UnitConverter converter = getUnit().getConverterTo(unit);		
		return NumberQuantity.of(converter.convert(doubleValue(unit)), unit);
	}
	
	/**
	 * @return the unit
	 */
	public Unit<Q> getUnit() {
		return unit;
	}

//	public abstract T add(Quantity<Q> q);
//	
//	public abstract T add(T q);
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit<Q> unit) {
		this.unit = unit;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractQuantity<?> other = (AbstractQuantity<?>) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}	
	
}
