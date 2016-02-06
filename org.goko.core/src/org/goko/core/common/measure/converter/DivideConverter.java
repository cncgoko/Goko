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

package org.goko.core.common.measure.converter;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Simple converter performing multiplication 
 * 
 * @author PsyKo
 *
 */
public class DivideConverter extends AbstractUnitConverter {
	/** Scale factor */
	private BigDecimal factor;

	/**
	 * Constructor
	 * @param factor the multiplying factor
	 */
	public DivideConverter(BigDecimal factor) {
		super();
		this.factor = factor;
	}
	
	/**
	 * Constructor
	 * @param factor the multiplying factor
	 */
	public DivideConverter(String factor) {
		this(new BigDecimal(factor));
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#inverse()
	 */
	@Override
	public UnitConverter inverse() {
		return new MultiplyConverter(factor);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.AbstractUnitConverter#convert(double)
	 */
	@Override
	public BigDecimal convert(BigDecimal value) {
		return value.divide(factor, MathContext.DECIMAL128);
	}
	
}
