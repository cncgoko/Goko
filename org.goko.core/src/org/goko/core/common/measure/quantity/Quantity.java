package org.goko.core.common.measure.quantity;

import java.math.BigDecimal;

import org.goko.core.common.measure.units.Unit;


public interface Quantity<Q extends Quantity<Q>> {

	Q to(Unit<Q> unit);

	double doubleValue(Unit<Q> unit);

	BigDecimal value(Unit<Q> unit);

	Unit<Q> getUnit();

	Q add(Q q);

	Q subtract(Q q);

	Q multiply(BigDecimal n);
	
	Q multiply(int n);

	Q divide(BigDecimal n);
	
	Q divide(int n);

	Number divide(Q q);

	Q abs();

	Q negate();

	boolean lowerThan(Q quantity);

	boolean lowerThanOrEqualTo(Q quantity);

	boolean greaterThan(Q quantity);

	boolean greaterThanOrEqualTo(Q quantity);
	
	boolean almostEquals(Q quantity, double error);
}