/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.measure.converter.MultiplyConverter;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.TransformedUnit;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class TimeUnit {
	/** The map of known units */
	private static Map<String, Unit<Time>> mapUnitBySymbol = new HashMap<String, Unit<Time>>();
		

	 /**
    * The SI base unit for time quantities (standard name <code>s</code>).
    * The second is the duration of 9192631770 periods of the radiation corresponding to the transition between the two hyperfine levels of the ground state of the caesium 133 atom.
    */
	public static final BaseUnit<Time> SECOND = add(new BaseUnit<Time>("s", QuantityDimension.TIME));
	
	/**
	 * Derived from SECONDS
	 * One millisecond = 1/1000 seconds
	 */
	public static final TransformedUnit<Time> MILLISECOND = add(new TransformedUnit<Time>("ms", SECOND, new MultiplyConverter("0.001")));
	
	/**
	 * Derived from SECONDS
	 * One minute = 60 seconds
	 */
	public static final TransformedUnit<Time> MINUTE = add(new TransformedUnit<Time>("min", SECOND, new MultiplyConverter("60.0")));
	
	/**
	 * Derived from MINUTE
	 * One hour = 60 minutes
	 */
	public static final TransformedUnit<Time> HOUR = add(new TransformedUnit<Time>("h", MINUTE, new MultiplyConverter("60.0")));
	
	/**
	 * Derived from HOUR
	 * One day = 24 hours
	 */
	public static final TransformedUnit<Time> DAY  = add(new TransformedUnit<Time>("d", HOUR, new MultiplyConverter("24.0")));
	
	/**
	 * Registers a Unit to the map of handled units
	 * @param unit the unit to register
	 * @return the registered unit 
	 */
	private static <T extends Unit<Time>> T add(T unit){
		mapUnitBySymbol.put(unit.getSymbol(), unit);
		return unit;
	}
	
	/**
	 * Get the list of known units
	 * @return the list of known units
	 */
	public static List<Unit<Time>> getAll(){
		return new ArrayList<Unit<Time>>(mapUnitBySymbol.values());
	}
}
