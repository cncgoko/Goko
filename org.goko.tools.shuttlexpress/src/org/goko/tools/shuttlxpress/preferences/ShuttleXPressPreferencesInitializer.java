/**
 * 
 */
package org.goko.tools.shuttlxpress.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * Initializer for the Shuttle XPress integration preferences 
 * @author Psyko
 * @date 25 avr. 2016
 */
public class ShuttleXPressPreferencesInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		ShuttleXPressPreferences.getInstance();		
	}
}
