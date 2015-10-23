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
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.type.DoubleQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.TransformedUnit;
import org.goko.core.common.measure.units.Unit;

public class SI {
	 /**
     * The SI base unit for length quantities (standard name <code>m</code>).
     * One metre was redefined in 1983 as the distance traveled by light in
     * a vacuum in 1/299,792,458 of a second.
     */
	public static final BaseUnit<Length> METRE = new BaseUnit<Length>("m", QuantityDimension.LENGTH);
	
	 /**
     * Derived from Metre
     * One millimetre equals one thousandth of a metre
     */
	public static final Unit<Length> MILLIMETRE = SIPrefix.MILLI(METRE);
	
	 /**
     * The SI base unit for angle quantities (standard name <code>rad</code>).
     * An angle's measurement in radians is numerically equal to the length of a corresponding arc of a unit circle,
     */
	public static final BaseUnit<Angle> RADIAN = new BaseUnit<Angle>("rad", QuantityDimension.ANGLE);
	
	/**
	 * Degrees (angular degrees, symbol ï¿½ ) 
	 */
	public static final TransformedUnit<Angle> DEGREE_ANGLE = new TransformedUnit<Angle>("°", RADIAN, new MultiplyConverter(180.0/Math.PI));

	 /**
     * The SI base unit for time quantities (standard name <code>s</code>).
     * The second is the duration of 9192631770 periods of the radiation corresponding to the transition between the two hyperfine levels of the ground state of the caesium 133 atom.
     */
	public static final BaseUnit<Time> SECOND = new BaseUnit<Time>("s", QuantityDimension.TIME);
	
	/**
	 * Derived from SECONDS
	 * One millisecond = 1/1000 seconds
	 */
	public static final TransformedUnit<Time> MILLISECOND = new TransformedUnit<Time>("ms", SECOND, new MultiplyConverter(1.0/1000.0));
	
	/**
	 * Derived from SECONDS
	 * One minute = 60 seconds
	 */
	public static final TransformedUnit<Time> MINUTE = new TransformedUnit<Time>("min", SECOND, new MultiplyConverter(60));
	
	/**
	 * Derived from MINUTE
	 * One hour = 60 minutes
	 */
	public static final TransformedUnit<Time> HOUR = new TransformedUnit<Time>("h", MINUTE, new MultiplyConverter(60));
	
	/**
	 * Derived from HOUR
	 * One day = 24 hours
	 */
	public static final TransformedUnit<Time> DAY  = new TransformedUnit<Time>("d", HOUR, new MultiplyConverter(24));
	
}
