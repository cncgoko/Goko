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

import org.goko.core.common.measure.converter.MultiplyConverter;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.TransformedUnit;
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
     * The SI base unit for angle quantities (standard name <code>rad</code>).
     * An angle's measurement in radians is numerically equal to the length of a corresponding arc of a unit circle,
     */
	public static final BaseUnit<Angle> RADIAN = add(new BaseUnit<Angle>("rad", QuantityDimension.ANGLE));
	
	/**
	 * Degrees (angular degrees, symbol ï¿½ ) 
	 */
	public static final TransformedUnit<Angle> DEGREE_ANGLE = add(new TransformedUnit<Angle>("°", RADIAN, new MultiplyConverter(180.0/Math.PI)));

	 /**
     * The SI base unit for time quantities (standard name <code>s</code>).
     * The second is the duration of 9192631770 periods of the radiation corresponding to the transition between the two hyperfine levels of the ground state of the caesium 133 atom.
     */
	public static final BaseUnit<Time> SECOND = add(new BaseUnit<Time>("s", QuantityDimension.TIME));
	
	/**
	 * Derived from SECONDS
	 * One millisecond = 1/1000 seconds
	 */
	public static final TransformedUnit<Time> MILLISECOND = add(new TransformedUnit<Time>("ms", SECOND, new MultiplyConverter(1.0/1000.0)));
	
	/**
	 * Derived from SECONDS
	 * One minute = 60 seconds
	 */
	public static final TransformedUnit<Time> MINUTE = add(new TransformedUnit<Time>("min", SECOND, new MultiplyConverter(60)));
	
	/**
	 * Derived from MINUTE
	 * One hour = 60 minutes
	 */
	public static final TransformedUnit<Time> HOUR = add(new TransformedUnit<Time>("h", MINUTE, new MultiplyConverter(60)));
	
	/**
	 * Derived from HOUR
	 * One day = 24 hours
	 */
	public static final TransformedUnit<Time> DAY  = add(new TransformedUnit<Time>("d", HOUR, new MultiplyConverter(24)));
	
	
	
	/* *************************************************************************************************************************
	 *
	 *   Non metric units
	 *   
	 * ************************************************************************************************************************ */
	  
	/**
	 * A unit of length equal to <code>0.3048 m</code> (standard name
	 * <code>ft</code>).
	 */
	public static final Unit<Length> 	 FOOT = add(new TransformedUnit<Length>("ft", METRE, new MultiplyConverter(0.3048)));
	/**
	 * A unit of length equal to <code>0.0254 m</code> (standard name
	 * <code>in</code>).
	 */
	public static final Unit<Length> 	 INCH = add(new TransformedUnit<Length>("in", (AbstractUnit<Length>) FOOT, new MultiplyConverter(1.0/12)));
	
	
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
