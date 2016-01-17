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

/**
 * Unit converter used to concatenate 2 converters
 * 
 * @author PsyKo
 *
 */
public class ConcatenatedConverter extends AbstractUnitConverter {
	/** Left operand */
	private UnitConverter left;
	/** Right operand*/
	private UnitConverter right;


	ConcatenatedConverter(UnitConverter left, UnitConverter right) {
		super();
		this.left = left;
		this.right = right;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#inverse()
	 */
	@Override
	public UnitConverter inverse() {
		return new ConcatenatedConverter(right.inverse(), left.inverse());
	}


	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#convert(double)
	 */
	@Override
	public BigDecimal convert(BigDecimal value) {
		return left.convert(right.convert(value));
	}

}
