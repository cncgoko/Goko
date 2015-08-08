package org.goko.core.rs274ngcv3.config;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * Initializer for GCode service 
 * 
 * @author PsyKo
 *
 */
public class RS274PreferenceInitializer extends GkPreferenceInitializer {

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		RS274Preference prefs = RS274Preference.getInstance();
		prefs.setDefault(RS274Preference.KEY_DECIMAL_COUNT, 	RS274Preference.DEFAULT_DECIMAL_COUNT);
		prefs.setDefault(RS274Preference.KEY_TRUNCATE_ENABLED,	RS274Preference.DEFAULT_TRUNCATE_ENABLED);
	}

}
