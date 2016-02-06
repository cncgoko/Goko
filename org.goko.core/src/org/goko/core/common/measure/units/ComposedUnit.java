/**
 * 
 */
package org.goko.core.common.measure.units;

import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.quantity.Quantity;

/**
 * @author PsyKo
 * @date 3 févr. 2016
 */
public class ComposedUnit<Q extends Quantity<Q>, T extends Quantity<T>, U extends Quantity<U>> extends AbstractUnit<Q> {	
	private Unit<T> topUnit;
	private Unit<U> bottomUnit;

	public ComposedUnit(String symbol, Dimension<Q> dimension, Unit<T> topUnit, Unit<U> bottomUnit) {
		super(symbol, dimension);
		this.topUnit = topUnit;
		this.bottomUnit = bottomUnit;		
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.AbstractUnit#getReferenceUnit()
	 */
	@Override
	protected Unit<Q> getReferenceUnit() {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.measure.units.Unit#getConverterToReferenceUnit()
	 */
	@Override
	public UnitConverter getConverterToReferenceUnit() {
		return bottomUnit.getConverterToReferenceUnit().inverse().then(topUnit.getConverterToReferenceUnit());
	}
}
