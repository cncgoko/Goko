/**
 * 
 */
package org.goko.controller.grbl.v09.handlers.watcher;

import org.goko.controller.grbl.v09.configuration.GrblConfiguration;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public interface IGrblConfigurationFix {

	boolean shouldApply(String identifier, GrblConfiguration configuration);
	
	void apply(GrblConfiguration configuration);
	
	String getDescription();
}
