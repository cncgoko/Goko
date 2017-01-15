/**
 * 
 */
package org.goko.controller.tinyg.commons.configuration;

/**
 * Configuration listener 
 * @author Psyko
 * @date 11 janv. 2017
 */
public interface ITinyGConfigurationListener<C extends AbstractTinyGConfiguration<C>> {

	/**
	 * Listener for notification changes 
	 * @param configuration the new {@link AbstractTinyGConfiguration}
	 */
	void onConfigurationChanged(C configuration);
	
}
