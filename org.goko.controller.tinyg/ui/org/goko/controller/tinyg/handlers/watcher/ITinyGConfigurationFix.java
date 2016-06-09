/**
 * 
 */
package org.goko.controller.tinyg.handlers.watcher;

import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public interface ITinyGConfigurationFix {

	boolean shouldApply(TinyGConfiguration configuration);
	
	void apply(TinyGConfiguration configuration);
	
	String getDescription();
}
