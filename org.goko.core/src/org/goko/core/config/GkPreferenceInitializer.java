package org.goko.core.config;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Goko Preference initializer
 * 
 * @author PsyKo
 *
 */
public abstract class GkPreferenceInitializer extends AbstractPreferenceInitializer {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GkPreferenceInitializer.class);
	
	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public final void initializeDefaultPreferences() {
		try {
			initializeDefaultGkPreferences();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	protected abstract void initializeDefaultGkPreferences() throws GkException;
}
