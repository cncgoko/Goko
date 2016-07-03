/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.converter.MultiplyConverter;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.TransformedUnit;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 15 janv. 2016
 */
public class LengthUnit {
	/** The map of known units */
	private static Map<String, Unit<Length>> mapUnitBySymbol = new HashMap<String, Unit<Length>>();
	
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
	
	/* *************************************************************************************************************************
	 *
	 *   Non metric units
	 *   
	 * ************************************************************************************************************************ */
	  
	/**
	 * A unit of length equal to <code>0.3048 m</code> (standard name
	 * <code>ft</code>).
	 */
	public static final Unit<Length> 	 FOOT = add(new TransformedUnit<Length>("ft", METRE, new MultiplyConverter("0.304800")));
	/**
	 * A unit of length equal to <code>0.0254 m</code> (standard name
	 * <code>in</code>).
	 */
	public static final Unit<Length> 	 INCH = add(new TransformedUnit<Length>("in", (AbstractUnit<Length>) FOOT, new MultiplyConverter(BigDecimal.ONE.divide(new BigDecimal("12.0"), MathContext.DECIMAL64))));
	
	
	
	/**
	 * Registers a Unit to the map of handled units
	 * @param unit the unit to register
	 * @return the registered unit 
	 */
	private static <T extends Unit<Length>> T add(T unit){
		mapUnitBySymbol.put(unit.getSymbol(), unit);
		return unit;
	}
	
	/**
	 * Get the list of known units
	 * @return the list of known units
	 */
	public static List<Unit<Length>> getAll(){
		return new ArrayList<Unit<Length>>(mapUnitBySymbol.values());
	}

}
