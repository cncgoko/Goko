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

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.units.Unit;

public class Time extends AbstractQuantity<Time> {
	public static final Time ZERO = new Time(BigDecimal.ZERO, TimeUnit.SECOND);
	/**
	 * @param value
	 * @param unit
	 */
	protected Time(BigDecimal value, Unit<Time> unit) {
		super(value, unit);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#valueOf(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public Time createQuantity(BigDecimal value, Unit<Time> unit) {		
		return Time.valueOf(value, unit);
	}
	
	public static Time valueOf(long value, Unit<Time> unit) {		
		return new Time(new BigDecimal(value), unit);
	}
	
	public static Time valueOf(String value, Unit<Time> unit) {		
		return new Time(new BigDecimal(value), unit);
	}
	
	public static Time valueOf(BigDecimal value, Unit<Time> unit) {		
		return new Time(value, unit);
	}
	
	public static Time valueOf(int value, Unit<Time> unit) {		
		return new Time(new BigDecimal(value), unit);
	}
	
	public static Time parse(String value) throws GkException {		
		return Time.ZERO.parse(value, TimeUnit.getAll());
	}
}
