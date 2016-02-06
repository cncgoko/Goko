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

package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;
import java.math.MathContext;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.units.Unit;

public class Length extends AbstractQuantity<Length> {
	public static final Length ZERO = new Length(BigDecimal.ZERO, Units.MILLIMETRE);
	/**
	 * @param value
	 * @param unit
	 */
	protected Length(BigDecimal value, Unit<Length> unit) {
		super(value, unit);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#valueOf(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public Length createQuantity(BigDecimal value, Unit<Length> unit) {		
		return Length.valueOf(value, unit);
	}
	
	public static Length valueOf(BigDecimal value, Unit<Length> unit) {		
		return new Length(value, unit);
	}
	
	public static Length valueOf(String value, Unit<Length> unit) {		
		return new Length(new BigDecimal(value), unit);
	}
	
	public static Length valueOf(int value, Unit<Length> unit) {		
		return new Length(new BigDecimal(value), unit);
	}
	
	public static Length parse(String value) throws GkException {		
		return Length.ZERO.parse(value, LengthUnit.getAll());
	}
	
	public Time divide(Speed speed){
		return Time.valueOf(this.value(LengthUnit.METRE).divide(speed.value(SpeedUnit.METRE_PER_SECOND), MathContext.DECIMAL64), TimeUnit.SECOND);
	}
}
