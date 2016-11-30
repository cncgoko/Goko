/**
 * 
 */
package org.goko.controller.grbl.v09.configuration;

/**
 * @author Psyko
 * @date 28 nov. 2016
 */
public interface IGrblConfigurationListener {

	/**
	 * Listener for notification changes 
	 * @param configuration the new {@link TinyGConfiguration}
	 * @param identifier the identifier of the setting
	 */
	void onConfigurationChanged(GrblConfiguration configuration, String identifier);
		
}
