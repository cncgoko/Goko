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

public class IdentityConverter extends AbstractUnitConverter{

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#inverse()
	 */
	@Override
	public UnitConverter inverse() {
		return this;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.UnitConverter#convert(double)
	 */
	@Override
	public BigDecimal convert(BigDecimal value) {
		return value;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.converter.AbstractUnitConverter#isIdentity()
	 */
	@Override
	public final boolean isIdentity() {
		return true;
	}

}
