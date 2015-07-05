package org.goko.core.common.measure.quantity;

import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;

public final class QuantityUtils {
	
	public static <Q extends Quantity<Q>> Quantity<Q> add(Quantity<Q> a, Quantity<Q> b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		return a.add(b);
	}
	
	/**
	 * Performs the addition of the two given quantity and make sure the type of quantity remains the same 
	 * Unit conversion is used to make sure the sum is made in the corresponding units
	 * @param a quantity A
	 * @param b quantity B
	 * @return the sum of the two values 
	 */
	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> add(BigDecimalQuantity<Q> a, BigDecimalQuantity<Q> b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		return a.add(b);
	}
	
	/**
	 * Performs the subtraction of the two given quantity and make sure the type of quantity remains the same 
	 * Unit conversion is used to make sure the sum is made in the corresponding units
	 * @param a quantity A
	 * @param b quantity B
	 * @return the subtraction of the two values 
	 */
	public static <Q extends Quantity<Q>> BigDecimalQuantity<Q> subtract(BigDecimalQuantity<Q> a, BigDecimalQuantity<Q> b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		return a.subtract(b);
	}
	/**
	 * Returns the minimum quantity between the 2 given Quantity. Unit conversion
	 * is used to make sure the comparison is made in the corresponding units
	 * @param a quantity A
	 * @param b quantity B
	 * @return the quantity representing the lowest amount 
	 */
	public static <T extends Quantity<Q>, Q extends Quantity<Q>> T min(T a, T b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		if(a.to(b.getUnit()).doubleValue() < b.doubleValue()){
			return a;
		}
		return b;
	}
	
	/**
	 * Returns the maximum quantity between the 2 given Quantity. Unit conversion
	 * is used to make sure the comparison is made in the corresponding units
	 * @param a quantity A
	 * @param b quantity B
	 * @return the quantity representing the lowest amount 
	 */
	public static <T extends Quantity<Q>, Q extends Quantity<Q>> T max(T a, T b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		if(a.to(b.getUnit()).doubleValue() > b.doubleValue()){
			return a;
		}
		return b;
	}
}
