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

import org.goko.core.common.measure.converter.IdentityConverter;
import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.quantity.Quantity;


/**
 * Abstract implementation of a Unit
 * @author PsyKo
 *
 * @param <Q> the bound quantity
 */
public class BaseUnit<Q extends Quantity<Q>> extends AbstractUnit<Q> {


	public BaseUnit(String symbol, Dimension dimension) {
		super(symbol, dimension);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.AbstractUnit#getReferenceUnit()
	 */
	@Override
	protected Unit<Q> getReferenceUnit() {
		return this;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#getConverterToReferenceUnit()
	 */
	@Override
	public UnitConverter getConverterToReferenceUnit() {
		return new IdentityConverter();
	}

}
