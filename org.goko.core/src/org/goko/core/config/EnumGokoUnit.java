package org.goko.core.config;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;

/**
 * Goko available units
 *
 * @author PsyKo
 *
 */
public enum EnumGokoUnit {
	MILLIMETERS("millimeters", SIPrefix.MILLI(Units.METRE)),
	INCHES("inches", Units.INCH);

	String code;
	Unit<Length> unit;

	private EnumGokoUnit(String code, Unit<Length> unit) {
		this.code = code;
		this.unit = unit;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	public static EnumGokoUnit getEnum(String code) throws GkTechnicalException{
		for (EnumGokoUnit enumValue : values()) {
			if(StringUtils.equals(code, enumValue.getCode())){
				return enumValue;
			}
		}
		throw new GkTechnicalException("Enum with code '"+code+"' does not exist.");
	}

	/**
	 * @return the unit
	 */
	public Unit<Length> getUnit() {
		return unit;
	}
}