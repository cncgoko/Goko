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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.Unit;

/**
 * Class handling the several known units  
 * 
 * @author Psyko
 */
public class Units {
	/** The map of known units */
	private static Map<String, Unit<?>> mapUnitBySymbol = new HashMap<String, Unit<?>>();
	
	 /**
     * The SI base unit for length quantities (standard name <code>m</code>).
     * One metre was redefined in 1983 as the distance traveled by light in
     * a vacuum in 1/299,792,458 of a second.
     */
	public static final BaseUnit<Length> METRE = add(new BaseUnit<Length>("m", QuantityDimension.LENGTH));
	
	 /**
     * Derived from Metre
     * One millimetre equals one thousandth of a metre
     */
	public static final Unit<Length> MILLIMETRE = add(SIPrefix.MILLI(METRE));
		
	/**
	 * Registers a Unit to the map of handled units
	 * @param unit the unit to register
	 * @return the registered unit 
	 */
	private static <Q extends Quantity<Q>, T extends Unit<Q>> T add(T unit){
		mapUnitBySymbol.put(unit.getSymbol(), unit);
		return unit;
	}
	
	/**
	 * Get the list of known units
	 * @return the list of known units
	 */
	public static List<Unit<?>> getAll(){
		return new ArrayList<>(mapUnitBySymbol.values());
	}

}
