/**
 * 
 */
package org.goko.core.execution.monitor.uiprovider.menu.executionqueue;

import org.eclipse.jface.action.IContributionItem;

/**
 * 
 * @author Psyko
 * @date 16 oct. 2016
 */
public interface IExecutionQueueContributionItem {
	
	/**
	 * Create an action for the given execution queue
	 * @param provider the provider
	 * @return Action
	 */
	IContributionItem getItem();
}
