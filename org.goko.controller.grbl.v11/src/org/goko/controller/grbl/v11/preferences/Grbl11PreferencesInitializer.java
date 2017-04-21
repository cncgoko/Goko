/**
 * 
 */
package org.goko.controller.grbl.v11.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * @author PsyKo
 *
 */
public class Grbl11PreferencesInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		Grblv11Preferences prefs = Grblv11Preferences.getInstance();
		prefs.setPollingPeriod(Time.valueOf(100, TimeUnit.MILLISECOND));
		
	}

}
