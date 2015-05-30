package org.goko.core.common.measure.quantity;

import org.goko.core.common.measure.units.Unit;


public interface Quantity<Q extends Quantity<Q>> {

	Quantity<Q> to(Unit<Q> unit);

	double doubleValue();
	
	double doubleValue(Unit<Q> unit);

	Number value();
	
	Number value(Unit<Q> unit);
	
	Unit<Q> getUnit();
	
	Quantity<Q> add(Quantity<Q> q);
	
	Quantity<Q> subtract(Quantity<Q> q);
	
	Quantity<Q> multiply(Number n);
	
	Quantity<Q> divide(Number n);
	
}