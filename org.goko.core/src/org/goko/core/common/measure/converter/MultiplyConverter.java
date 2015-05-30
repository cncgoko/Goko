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


public class MultiplyConverter extends AbstractUnitConverter {
	private double factor;

	/**
	 * Construcor
	 * @param factor the multiplying factor
	 */
	public MultiplyConverter(double factor) {
		super();
		this.factor = factor;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#inverse()
	 */
	@Override
	public UnitConverter inverse() {
		return new MultiplyConverter(1.0 / factor);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.AbstractUnitConverter#convert(double)
	 */
	@Override
	public double convert(double value) {
		return factor * value;
	}
	
}
