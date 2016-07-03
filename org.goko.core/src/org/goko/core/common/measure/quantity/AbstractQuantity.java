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
import java.math.MathContext;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.units.AbstractUnit;
import org.goko.core.common.measure.units.Unit;


public abstract class AbstractQuantity<Q extends Quantity<Q>> implements Quantity<Q> {
	private BigDecimal value;
	private Unit<Q> unit;

	protected AbstractQuantity(BigDecimal value, Unit<Q> unit) {
		super();
		this.value = value;
		this.unit = unit;
	}

	protected UnitConverter getConverterTo(Unit<Q> unit) throws GkTechnicalException{
		if(unit instanceof AbstractUnit<?>){
			throw new GkTechnicalException("Incompatible units...");
		}
		return this.getUnit().getConverterToReferenceUnit().then(unit.getConverterToReferenceUnit().inverse());
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#to(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public Q to(Unit<Q> unit) {
		if(unit == getUnit()){
			return createQuantity(value, unit);
		}
		UnitConverter converter = getUnit().getConverterTo(unit);		
		return createQuantity(converter.convert(value), unit);
	}
	
	/**
	 * @return the unit
	 */
	public Unit<Q> getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit<Q> unit) {
		this.unit = unit;
	}

	public boolean equals(Q obj) {		
		return value.compareTo(obj.value(getUnit())) == 0;
	}
	
	public boolean almostEquals(Q obj, double error) {		
		return value.subtract(obj.value(getUnit())).abs().doubleValue() <= error;
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#value(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public BigDecimal value(Unit<Q> unit) {
		if(unit == getUnit()){
			return value;
		}
		return to(unit).value(unit);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#doubleValue(org.goko.core.common.measure.units.Unit)
	 */
	@Override
	public double doubleValue(Unit<Q> unit) {
		return value(unit).doubleValue();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#add(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public Q add(Q q) {
		return createQuantity(value.add(q.value(this.unit)), unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#subtract(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public Q subtract(Q q) {
		return createQuantity(value.subtract(q.value(this.unit)), unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#multiply(java.lang.Number)
	 */
	@Override
	public Q multiply(BigDecimal n) {
		return createQuantity(value.multiply(n), unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#multiply(int)
	 */
	@Override
	public Q multiply(int n) {		
		return createQuantity(value.multiply(new BigDecimal(n)), unit);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(java.lang.Number)
	 */
	@Override
	public Q divide(BigDecimal n) {
		return createQuantity(value.divide(n, MathContext.DECIMAL64), unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(int)
	 */
	@Override
	public Q divide(int n) {		
		return createQuantity(value.divide(new BigDecimal(n), MathContext.DECIMAL64), unit);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#divide(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public BigDecimal divide(Q q) {
		return value.divide(q.value(unit), MathContext.DECIMAL64);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#abs()
	 */
	@Override
	public Q abs() {		
		return createQuantity(value.abs(), unit);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#negate()
	 */
	@Override
	public Q negate() {		
		return createQuantity(value.negate(), unit);
	}
	
	
	protected abstract Q createQuantity(BigDecimal value, Unit<Q> unit);

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#lowerThan(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public boolean lowerThan(Q quantity) {		
		return value.compareTo(quantity.value(getUnit())) < 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#lowerThanOrEqualTo(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public boolean lowerThanOrEqualTo(Q quantity) {
		return value.compareTo(quantity.value(getUnit())) <= 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#greaterThan(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public boolean greaterThan(Q quantity) {
		return value.compareTo(quantity.value(getUnit())) > 0;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.quantity.Quantity#greaterThanOrEqualTo(org.goko.core.common.measure.quantity.Quantity)
	 */
	@Override
	public boolean greaterThanOrEqualTo(Q quantity) {
		return value.compareTo(quantity.value(getUnit())) >= 0;
	}
	
	protected Q parse(String value, List<Unit<Q>> lstUnit) throws GkException{
		// Try to extract unit
		String leftNumericValue = value;
		Unit<Q> foundUnit = null;
		for (Unit<Q> existingUnit : lstUnit) {
			if(StringUtils.contains(value, existingUnit.getSymbol())){
				leftNumericValue = StringUtils.remove(value, existingUnit.getSymbol()).trim();
				foundUnit = (Unit<Q>) existingUnit;
				break;
			}
		}		
		if(foundUnit == null){
			throw new GkTechnicalException("No unit found in quantity ["+leftNumericValue+"]");
		}
		return createQuantity(new BigDecimal(leftNumericValue), foundUnit);
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractQuantity<Q> other = (AbstractQuantity<Q>) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		return QuantityUtils.format((Q)this);
	}
}
