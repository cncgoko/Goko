/**
 * 
 */
package org.goko.core.gcode.execution;

import java.util.Comparator;

/**
 * @author PsyKo
 * @date 5 déc. 2015
 */
public class ExecutionOrderComparator implements Comparator<IExecutionToken<?>> {

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IExecutionToken<?> o1, IExecutionToken<?> o2) {		
		return o1.getExecutionOrder() - o2.getExecutionOrder();
	}

}
