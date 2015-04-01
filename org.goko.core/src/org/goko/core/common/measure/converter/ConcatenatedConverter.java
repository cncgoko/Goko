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


public class ConcatenatedConverter extends AbstractUnitConverter {
	private UnitConverter left;
	private UnitConverter right;


	ConcatenatedConverter(UnitConverter left, UnitConverter right) {
		super();
		this.left = left;
		this.right = right;
	}

	/**
	 * @return
	 */
	@Override
	public UnitConverter inverse() {
		return new ConcatenatedConverter(right.inverse(), left.inverse());
	}

	/**
	 * @param value
	 * @return
	 */
	@Override
	public double convert(double value) {
		return left.convert(right.convert(value));
	}

}
