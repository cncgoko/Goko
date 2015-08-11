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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.measure.converter.IdentityConverter;
import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.quantity.Generic;
import org.goko.core.common.measure.quantity.Quantity;

public abstract class AbstractUnit<Q extends Quantity<Q>> implements Unit<Q> {
	/** Generic unit (no dimension, no unit) */
	public static final AbstractUnit<Generic> GENERIC = new BaseUnit<Generic>(StringUtils.EMPTY, null);
	/** The symbol for this unit */
	protected String symbol;
	/** The dimension for this unit */
	protected Dimension dimension;

	public AbstractUnit(String symbol, Dimension dimension) {
		super();
		this.symbol = symbol;
		this.dimension = dimension;
	}

	protected abstract Unit<Q> getReferenceUnit();

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#getConverterTo(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public UnitConverter getConverterTo(Unit<Q> unit) {
		if(unit == this){
			return new IdentityConverter();
		}
		return getConverterToReferenceUnit().then(unit.getConverterToReferenceUnit().inverse());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#transform(org.goko.core.common.measure.converter.UnitConverter)
	 */
	@Override
	public Unit<Q> transform(UnitConverter converter) {
		return new TransformedUnit<Q>(this, converter);
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#transform(org.goko.core.common.measure.converter.UnitConverter, java.lang.String)
	 */
	@Override
	public Unit<Q> transform(UnitConverter converter, String symbol) {
		return new TransformedUnit<Q>(symbol, this, converter);
	}

	/**
	 * @return the symbol
	 */
	@Override
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the dimension
	 */
	@Override
	public Dimension getDimension() {
		return dimension;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		AbstractUnit other = (AbstractUnit) obj;
		if (dimension == null) {
			if (other.dimension != null)
				return false;
		} else if (!dimension.equals(other.dimension))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
}
