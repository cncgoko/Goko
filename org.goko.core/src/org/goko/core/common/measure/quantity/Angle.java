package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.units.Unit;

public class Angle extends AbstractQuantity<Angle>{
	public static final Angle ZERO = new Angle(BigDecimal.ZERO, AngleUnit.DEGREE_ANGLE);
	
	/**
	 * Constructor
	 * @param value
	 * @param unit
	 */
	protected Angle(BigDecimal value, Unit<Angle> unit) {
		super(value, unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.AbstractQuantity#createQuantity(java.math.BigDecimal, org.goko.core.common.measure.units.Unit)
	 */
	@Override
	protected Angle createQuantity(BigDecimal value, Unit<Angle> unit) {		
		return Angle.valueOf(value, unit);
	}

	public static Angle valueOf(BigDecimal value, Unit<Angle> unit) {		
		return new Angle(value, unit);
	}
	
	public static Angle clone(Angle angle) {		
		return new Angle(angle.value(angle.getUnit()), angle.getUnit());
	}
	
	public static Angle valueOf(String value, Unit<Angle> unit) {		
		return new Angle(new BigDecimal(value), unit);
	}
	
	public static Angle valueOf(int value, Unit<Angle> unit) {		
		return new Angle(new BigDecimal(value), unit);
	}
	
	public static Angle parse(String value) throws GkException {		
		return Angle.ZERO.parse(value, AngleUnit.getAll());
	}
}
