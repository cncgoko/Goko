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
	 * @param oldConfig the previous {@link AbstractTinyGConfiguration}
	 * @param newConfig the previous {@link AbstractTinyGConfiguration}
	 */
	void onConfigurationChanged(C oldConfig, C newConfig);

	/**
	 * Notifications of the change of a specific setting
	 * @param groupIdentifier identifier of the group 
	 * @param settingIdentifier identifier of the setting 
	 * @param config the target configuration
	 */
	void onConfigurationSettingChanged(C oldConfig, C newConfig,  String groupIdentifier, String settingIdentifier);
	
}
