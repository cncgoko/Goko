/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.units.Unit;

/**
 * @author PsyKo
 * @date 2 févr. 2016
 */
public class Speed extends AbstractQuantity<Speed> {
	public static final Speed ZERO = new Speed(BigDecimal.ZERO, SpeedUnit.METRE_PER_SECOND);
	
	/**
	 * @param value
	 * @param unit
	 */
	protected Speed(BigDecimal value, Unit<Speed> unit) {
		super(value, unit);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.AbstractQuantity#createQuantity(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Speed createQuantity(BigDecimal value, Unit<Speed> unit) {
		return new Speed(value, unit);
	}
	
	public static Speed valueOf(BigDecimal value, Unit<Speed> unit) {		
		return new Speed(value, unit);
	}
	
	public static Speed valueOf(String value, Unit<Speed> unit) {		
		return new Speed(new BigDecimal(value), unit);
	}
	
	public static Speed valueOf(int value, Unit<Speed> unit) {		
		return new Speed(new BigDecimal(value), unit);
	}
	
	public static Speed parse(String value) throws GkException {		
		return Speed.ZERO.parse(value, SpeedUnit.getAll());
	}
	
	public Length multiply(Time time){
		return Length.valueOf(value(SpeedUnit.METRE_PER_SECOND).multiply(time.value(TimeUnit.SECOND)), LengthUnit.METRE);
	}

}
