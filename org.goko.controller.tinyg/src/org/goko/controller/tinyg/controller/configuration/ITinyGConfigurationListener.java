/**
 * 
 */
package org.goko.controller.tinyg.controller.configuration;

/**
 * @author Psyko
 * @date 6 juin 2016
 */
public interface ITinyGConfigurationListener {

	/**
	 * Listener for notification changes 
	 * @param configuration the new {@link TinyGConfiguration}
	 */
	void onConfigurationChanged(TinyGConfiguration configuration);
	
}
