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

import org.goko.core.common.measure.converter.MultiplyConverter;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.TransformedUnit;
import org.goko.core.common.measure.units.Unit;

public class US {

    /**
     * The SI base unit for length quantities (standard name <code>m</code>).
     * One metre was redefined in 1983 as the distance traveled by light in
     * a vacuum in 1/299,792,458 of a second.
     */
	public static final BaseUnit<Length> METER = SI.METRE;

	/**
	 * A unit of length equal to <code>0.3048 m</code> (standard name
	 * <code>ft</code>).
	 */
	public static final Unit<Length> 	 FOOT = new TransformedUnit<Length>("ft", METER, new MultiplyConverter(0.3048));
	/**
	 * A unit of length equal to <code>0.0254 m</code> (standard name
	 * <code>in</code>).
	 */
	public static final Unit<Length> 	 INCH = new TransformedUnit<Length>("in", (AbstractUnit<Length>) FOOT, new MultiplyConverter(1.0/12));
}
