/**
 * 
 */
package org.goko.controller.tinyg.commons.configuration.watcher;

import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public interface ITinyGConfigurationFix<C extends AbstractTinyGConfiguration<C>> {

	boolean shouldApply(C configuration);
	
	void apply(C configuration);
	
	String getDescription();
}
