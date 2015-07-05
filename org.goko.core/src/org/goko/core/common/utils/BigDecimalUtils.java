package org.goko.core.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalUtils {
	/**
	 * Compute the square root of x to a given scale, x >= 0. Use Newton's
	 * algorithm.
	 * 
	 * @param x
	 *            the value of x
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal sqrt(BigDecimal x, int scale) {
		// Check that x >= 0.
		if (x.signum() < 0) {
			throw new IllegalArgumentException("x < 0");
		}

		// n = x*(10^(2*scale))
		BigInteger n = x.movePointRight(scale << 1).toBigInteger();

		// The first approximation is the upper half of n.
		int bits = (n.bitLength() + 1) >> 1;
		BigInteger ix = n.shiftRight(bits);
		BigInteger ixPrev;

		// Loop until the approximations converge
		// (two successive approximations are equal after rounding).
		do {
			ixPrev = ix;

			// x = (x + n/x)/2
			ix = ix.add(n.divide(ix)).shiftRight(1);

			Thread.yield();
		} while (ix.compareTo(ixPrev) != 0);

		return new BigDecimal(ix, scale);
	}
}