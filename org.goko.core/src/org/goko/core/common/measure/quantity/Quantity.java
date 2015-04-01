package org.goko.core.common.measure.quantity;

import org.goko.core.common.measure.units.Unit;


public interface Quantity<Q extends Quantity<Q>> {

	Quantity<Q> to(Unit<Q> unit);

	double doubleValue();

	Unit<Q> getUnit();
}