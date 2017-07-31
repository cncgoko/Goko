/**
 * 
 */
package org.goko.core.common.measure.quantity;

import java.util.Comparator;

/**
 * @author Psyko
 * @date 24 juil. 2017
 */
public class QuantityComparator<T extends Quantity<T>> implements Comparator<T> {

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		if(o1.lowerThan(o2)){
			return -1;
		}else if(o1.greaterThan(o2)){
			return 1;
		}
		return 0;
	}

}
