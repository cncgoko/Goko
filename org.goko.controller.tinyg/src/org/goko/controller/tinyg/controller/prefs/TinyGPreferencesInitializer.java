/**
 * 
 */
package org.goko.controller.tinyg.controller.prefs;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * @author PsyKo
 *
 */
public class TinyGPreferencesInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		TinyGPreferences prefs = TinyGPreferences.getInstance();
		prefs.setPlannerBufferSpaceCheck(true);
		
	}

}
