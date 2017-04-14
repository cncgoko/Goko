/**
 * 
 */
package org.goko.controller.grbl.commons.configuration;

/**
 * Configuration listener
 * 
 * @author Psyko
 * @date 2 avr. 2017
 */
public interface IGrblConfigurationListener<C extends AbstractGrblConfiguration<C>> {

	/**
	 * Listener for notification changes 
	 * @param configuration the new {@link AbstractTinyGConfiguration}
	 */
	void onConfigurationChanged(C configuration);
	
	/**
	 * Listener for notification change on specific setting
	 * @param configuration the target configuration
	 * @param identifier the identifier of the changed setting
	 * @param oldValue the old value 
	 * @param newValue the new value
	 */
	void onConfigurationSettingChanged(C configuration, String identifier, String oldValue, String newValue);
	
}
