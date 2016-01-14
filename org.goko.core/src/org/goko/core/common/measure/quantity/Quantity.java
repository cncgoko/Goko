package org.goko.core.common.measure.quantity;

import org.goko.core.common.measure.units.Unit;


public interface Quantity<Q extends Quantity<Q>> {

	Quantity<Q> to(Unit<Q> unit);

	double doubleValue(Unit<Q> unit);

	Number value();

	Number value(Unit<Q> unit);

	Unit<Q> getUnit();

	Quantity<Q> add(Quantity<Q> q);

	Quantity<Q> subtract(Quantity<Q> q);

	Quantity<Q> multiply(Number n);

	Quantity<Q> divide(Number n);

	Number divide(Quantity<Q> q);

	Quantity<Q> abs();

	Quantity<Q> negate();

	boolean lowerThan(Q quantity);

	boolean lowerThanOrEqualTo(Q quantity);

	boolean greaterThan(Q quantity);

	boolean greaterThanOrEqualTo(Q quantity);
}