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

import org.goko.core.common.measure.converter.MultiplyConverter;
import org.goko.core.common.measure.converter.UnitConverter;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.BaseUnit;
import org.goko.core.common.measure.units.Unit;

public class SIPrefix {
	private static UnitConverter MEGA  = new MultiplyConverter("1000000.0");
	private static UnitConverter KILO  = new MultiplyConverter("1000.0");
	private static UnitConverter HECTO = new MultiplyConverter("100.0");
	private static UnitConverter DECA  = new MultiplyConverter("10.0");
	private static UnitConverter DECI  = new MultiplyConverter("0.1");
	private static UnitConverter CENTI = new MultiplyConverter("0.01");
	private static UnitConverter MILLI = new MultiplyConverter("0.001");
	private static UnitConverter MICRO = new MultiplyConverter("0.000001");
	private static UnitConverter NANO  = new MultiplyConverter("0.000000001");


	public static <Q extends Quantity<Q>> Unit<Q> MEGA(BaseUnit<Q> unit){
		return unit.transform(MEGA, "M"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> KILO(BaseUnit<Q> unit){
		return unit.transform(KILO, "k"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> HECTO(BaseUnit<Q> unit){
		return unit.transform(HECTO, "h"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> DECA(BaseUnit<Q> unit){
		return unit.transform(DECA, "da"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> DECI(BaseUnit<Q> unit){
		return unit.transform(DECI, "d"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> CENTI(BaseUnit<Q> unit){
		return unit.transform(CENTI, "c"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> MILLI(BaseUnit<Q> unit){
		return unit.transform(MILLI, "m"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> MICRO(BaseUnit<Q> unit){
		return unit.transform(MICRO, "µ"+unit.getSymbol());
	}
	public static <Q extends Quantity<Q>> Unit<Q> NANO(BaseUnit<Q> unit){
		return unit.transform(NANO, "n"+unit.getSymbol());
	}


}
