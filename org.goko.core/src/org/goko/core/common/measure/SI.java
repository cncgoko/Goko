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

package org.goko.core.common.measure;

import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.BaseUnit;

public class SI {
    /**
     * The SI base unit for length quantities (standard name <code>m</code>).
     * One metre was redefined in 1983 as the distance traveled by light in
     * a vacuum in 1/299,792,458 of a second.
     */
	public static final BaseUnit<Length> METRE = new BaseUnit<Length>("m", QuantityDimension.LENGTH);

}
