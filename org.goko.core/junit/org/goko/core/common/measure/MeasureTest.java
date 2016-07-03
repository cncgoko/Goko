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

package org.goko.core.common.measure;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.junit.Test;

public class MeasureTest {
	/** Length comparison tolerance */
	public static final double TOLERANCE = 0.00000000001;
	
	/**
	 * Checks that the two given quantity are almost equals (equals to a given tolerance)
	 * @param a Quantity A
	 * @param b Quantity B
	 */
	private <Q extends Quantity<Q>> void assertAlmostEqual(Q a, Q b){
		assertNotNull(a);
		assertNotNull(b);
		assertTrue(a.almostEquals(b, TOLERANCE));
	}
	
	/**
	 * Checks that the two given quantity are not almost equals (equals to a given tolerance)
	 * @param a Quantity A
	 * @param b Quantity B
	 */
	private <Q extends Quantity<Q>> void assertNotAlmostEqual(Q a, Q b){
		assertNotNull(a);
		assertNotNull(b);
		assertFalse(a.almostEquals(b, TOLERANCE));
	}
	
	@Test 
	public void testAssert(){
		// Test assert within tolerance
		assertAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE).add(Time.valueOf(BigDecimal.valueOf(TOLERANCE), TimeUnit.MINUTE)), Time.valueOf(1, TimeUnit.MINUTE));
		assertAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE).subtract(Time.valueOf(BigDecimal.valueOf(TOLERANCE), TimeUnit.MINUTE)), Time.valueOf(1, TimeUnit.MINUTE));
		// Test assert outside tolerance
		assertNotAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE).add(Time.valueOf(BigDecimal.valueOf(TOLERANCE*(1+TOLERANCE)), TimeUnit.MINUTE)), Time.valueOf(1, TimeUnit.MINUTE));
		assertNotAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE).subtract(Time.valueOf(BigDecimal.valueOf(TOLERANCE*(1+TOLERANCE)), TimeUnit.MINUTE)), Time.valueOf(1, TimeUnit.MINUTE));
	}
	
	@Test
	public void testLength(){
		Length inch = Length.valueOf("1", LengthUnit.INCH);
		Length foot = Length.valueOf("1", LengthUnit.FOOT);
		Length mm = Length.valueOf("1", LengthUnit.MILLIMETRE);
		Length m = Length.valueOf("1", LengthUnit.METRE);		

		// ********************** Generic quantity mechanism testing **********************
		// Comparison testing
		Length lower = m.subtract(mm);
		Length upper = m.add(mm);
		
		assertTrue(m.greaterThan(lower));
		assertFalse(m.greaterThan(upper));
		assertFalse(m.greaterThan(m));
		assertFalse(m.lowerThan(m));
		assertTrue(m.greaterThanOrEqualTo(m));
		assertTrue(m.lowerThanOrEqualTo(m));
		
		assertTrue(m.lowerThan(upper));
		assertFalse(m.lowerThan(lower));

		// Conversion testing
		assertAlmostEqual(m, mm.multiply(1000));
		assertAlmostEqual(mm, m.divide(1000));

		// ********************** Metric ********************** 
		// Equality testing
		assertAlmostEqual(m.add(mm), Length.valueOf(1001, LengthUnit.MILLIMETRE));
		assertAlmostEqual(m, Length.valueOf("1", LengthUnit.METRE));
		assertAlmostEqual(m, Length.valueOf("1000", LengthUnit.MILLIMETRE));
		// Test zero element
		assertTrue( m.add(Length.ZERO).equals(m));
		
		// ********************** Imperial **********************
		// Equality testing
		assertAlmostEqual(foot.add(inch), Length.valueOf(13, LengthUnit.INCH));		
		assertAlmostEqual(inch, Length.valueOf("1", LengthUnit.INCH));
		assertAlmostEqual(foot, Length.valueOf("12", LengthUnit.INCH));
		
		// ********************** Mixed **********************		
		assertAlmostEqual(inch, Length.valueOf("25.4", LengthUnit.MILLIMETRE));		
	}
	
	@Test
	public void testSpeed(){
		Speed mmPerMinute = Speed.valueOf(123, SpeedUnit.MILLIMETRE_PER_MINUTE);
		Length distance = Length.valueOf("1230", LengthUnit.MILLIMETRE);
		Time time = Time.valueOf(new BigDecimal("0.8"), TimeUnit.HOUR);
		
		assertAlmostEqual(distance.divide(mmPerMinute), Time.valueOf(10, TimeUnit.MINUTE));
		assertAlmostEqual(mmPerMinute.multiply(time), Length.valueOf(5904, LengthUnit.MILLIMETRE));
		assertAlmostEqual(mmPerMinute.multiply(Time.ZERO), Length.ZERO);
	
		// Test zero element
		assertTrue( mmPerMinute.add(Speed.ZERO).equals(mmPerMinute));
		
		// Conversion tests
		Speed mmPerMin = Speed.valueOf(1524, SpeedUnit.MILLIMETRE_PER_MINUTE);
		Speed inchPerSec = Speed.valueOf(1, SpeedUnit.INCH_PER_SECOND);
		Speed mmPerSec = Speed.valueOf(1, SpeedUnit.MILLIMETRE_PER_SECOND);
		
		assertAlmostEqual(mmPerMin, inchPerSec);
		assertNotAlmostEqual(inchPerSec, mmPerSec);				
	}
	
	@Test
	public void testTime(){
		Time hour = Time.valueOf(new BigDecimal("1"), TimeUnit.HOUR);
		Time minute = Time.valueOf(new BigDecimal("1"), TimeUnit.MINUTE);
		
		assertAlmostEqual(hour.add(minute), Time.valueOf(61, TimeUnit.MINUTE));
		
		assertAlmostEqual(Time.valueOf(1, TimeUnit.SECOND), Time.valueOf(1000, TimeUnit.MILLISECOND));
		assertAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE), Time.valueOf(60, TimeUnit.SECOND));
		assertAlmostEqual(Time.valueOf(1, TimeUnit.HOUR), Time.valueOf(60, TimeUnit.MINUTE));
		assertAlmostEqual(Time.valueOf(1, TimeUnit.DAY), Time.valueOf(24, TimeUnit.HOUR));
				
		assertNotAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE), Time.valueOf(61, TimeUnit.SECOND));
		assertNotAlmostEqual(Time.valueOf(1, TimeUnit.MINUTE), Time.valueOf(1, TimeUnit.SECOND));
	}
}
