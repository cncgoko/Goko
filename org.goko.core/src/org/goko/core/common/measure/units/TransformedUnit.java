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

package org.goko.core.common.measure.units;

import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.quantity.Quantity;

public class TransformedUnit<Q extends Quantity<Q>> extends AbstractUnit<Q> {
	/**
	 * The parent unit
	 */
	private AbstractUnit<Q> parentUnit;
	/**
	 * The converter to the parent unit
	 */
	private UnitConverter converter;


	public TransformedUnit(AbstractUnit<Q> parentUnit, UnitConverter converter) {
		this(parentUnit.getSymbol(), parentUnit, converter);
	}

	public TransformedUnit(String symbol, AbstractUnit<Q> parentUnit, UnitConverter converter) {
		super(symbol, parentUnit.getDimension());
		this.parentUnit = parentUnit;
		this.converter = converter;
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.AbstractUnit#getReferenceUnit()
	 */
	@Override
	protected Unit<Q> getReferenceUnit() {
		return parentUnit.getReferenceUnit();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#getConverterToReferenceUnit()
	 */
	@Override
	public UnitConverter getConverterToReferenceUnit() {
		return converter.then(parentUnit.getConverterToReferenceUnit());
	}

}
