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

	@Override
	public Quantity<Q> to(Unit<Q> unit) {
		if(unit == getUnit()){
			return this;
		}
		UnitConverter converter = getUnit().getConverterTo(unit);
		return NumberQuantity.of(converter.convert(doubleValue()), unit);
	}
	/**
	 * @return the unit
	 */
	public Unit<Q> getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit<Q> unit) {
		this.unit = unit;
	}
}
