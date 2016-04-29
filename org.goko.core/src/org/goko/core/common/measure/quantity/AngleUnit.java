/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class AngleUnit {
	/** The map of known units */
	private static Map<String, Unit<Angle>> mapUnitBySymbol = new HashMap<String, Unit<Angle>>();
	
	 /**
     * The SI base unit for angle quantities (standard name <code>rad</code>).
     * An angle's measurement in radians is numerically equal to the length of a corresponding arc of a unit circle,
     */
	public static final BaseUnit<Angle> RADIAN = add(new BaseUnit<Angle>("rad", QuantityDimension.ANGLE));
	
	/**
	 * Degrees (angular degrees, symbol ï¿½ ) 
	 */
	public static final TransformedUnit<Angle> DEGREE_ANGLE = add(new TransformedUnit<Angle>("°", RADIAN, new MultiplyConverter(BigDecimal.valueOf(Math.PI).divide(new BigDecimal("180"), RoundingMode.HALF_UP))));
	
	/**
	 * Registers a Unit to the map of handled units
	 * @param unit the unit to register
	 * @return the registered unit 
	 */
	private static <T extends Unit<Angle>> T add(T unit){
		mapUnitBySymbol.put(unit.getSymbol(), unit);
		return unit;
	}
	
	/**
	 * Get the list of known units
	 * @return the list of known units
	 */
	public static List<Unit<Angle>> getAll(){
		return new ArrayList<Unit<Angle>>(mapUnitBySymbol.values());
	}

}
