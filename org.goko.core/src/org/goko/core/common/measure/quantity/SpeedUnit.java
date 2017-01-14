/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.units.ComposedUnit;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class SpeedUnit {
	/** The map of known units */
	private static Map<String, Unit<Speed>> mapUnitBySymbol = new HashMap<String, Unit<Speed>>();
	
	 /**
     * Meters per seconds speed unit
     */
	public static final Unit<Speed> METRE_PER_SECOND = add(new ComposedUnit<Speed, Length, Time>("m/s", QuantityDimension.SPEED, LengthUnit.METRE, TimeUnit.SECOND));
	
	 /**
     * Millimeters per seconds speed unit
     */
	public static final Unit<Speed> MILLIMETRE_PER_SECOND = add(new ComposedUnit<Speed, Length, Time>("mm/s", QuantityDimension.SPEED, LengthUnit.MILLIMETRE, TimeUnit.SECOND));
	
	
	 /**
     * Millimeters per minute speed unit
     */
	public static final Unit<Speed> MILLIMETRE_PER_MINUTE = add(new ComposedUnit<Speed, Length, Time>("mm/min", QuantityDimension.SPEED, LengthUnit.MILLIMETRE, TimeUnit.MINUTE));
	
	/**
     * Inches per minute speed unit
     */
	public static final Unit<Speed> INCH_PER_MINUTE = add(new ComposedUnit<Speed, Length, Time>("in/min", QuantityDimension.SPEED, LengthUnit.INCH, TimeUnit.MINUTE));
	/**
     * Inches per second speed unit
     */
	public static final Unit<Speed> INCH_PER_SECOND = add(new ComposedUnit<Speed, Length, Time>("in/s", QuantityDimension.SPEED, LengthUnit.INCH, TimeUnit.SECOND));
	
	/**
	 * Registers a Unit to the map of handled units
	 * @param unit the unit to register
	 * @return the registered unit 
	 */
	private static <T extends Unit<Speed>> T add(T unit){
		mapUnitBySymbol.put(unit.getSymbol(), unit);
		return unit;
	}
	
	/**
	 * Get the list of known units
	 * @return the list of known units
	 */
	public static List<Unit<Speed>> getAll(){
		return new ArrayList<Unit<Speed>>(mapUnitBySymbol.values());
	}

}
