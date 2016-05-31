/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.eclipse.jface.action.IContributionItem;
import org.goko.core.gcode.element.IGCodeProvider;

/**
 * @author Psyko
 * @date 28 mai 2016
 */
public interface IGCodeProviderContributionItem {
	
	/**
	 * Create an action for the given provider
	 * @param provider the provider
	 * @return Action
	 */
	IContributionItem getItem(IGCodeProvider provider);
}
