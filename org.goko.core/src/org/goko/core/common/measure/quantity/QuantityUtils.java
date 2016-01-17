package org.goko.core.common.measure.quantity;

public final class QuantityUtils {
		
	/**
	 * Performs the addition of the two given quantity and make sure the type of quantity remains the same 
	 * Unit conversion is used to make sure the sum is made in the corresponding units
	 * @param a quantity A
	 * @param b quantity B
	 * @return the sum of the two values 
	 */
	public static <Q extends Quantity<Q>> Q add(Q a, Q b){
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
	public static <Q extends Quantity<Q>> Q subtract(Q a, Q b){
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
	public static <Q extends Quantity<Q>> Q min(Q a, Q b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		if(a.value(b.getUnit()).compareTo(b.value(b.getUnit())) < 0){
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
	public static <Q extends Quantity<Q>> Q max(Q a, Q b){
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		if(a.value(b.getUnit()).compareTo(b.value(b.getUnit())) > 0){
			return a;
		}
		return b;
	}
}
