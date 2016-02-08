package org.goko.core.config;

import org.goko.core.common.exception.GkTechnicalException;

/**
 * Goko general preference initializer
 * 
 * @author PsyKo
 *
 */
public class GokoPreferenceInitializer extends GkPreferenceInitializer {

	// Un plugin ne peut initialiser une config uniquement si le nom de la config est l'id du plugin 
	
	
	/** (inheritDoc)
	 * @throws GkTechnicalException 
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkTechnicalException {
		GokoPreference prefs = GokoPreference.getInstance();
		prefs.setDefault(GokoPreference.KEY_DISTANCE_UNIT, GokoPreference.DEFAULT_DISTANCE_UNIT_CODE);
		prefs.setDistanceUnit(GokoPreference.DEFAULT_DISTANCE_UNIT);		
		prefs.setDefault(GokoPreference.KEY_DISTANCE_DIGIT_COUNT, GokoPreference.DEFAULT_DISTANCE_DIGIT_COUNT);
		prefs.setDefault(GokoPreference.KEY_TARGET_BOARD, GokoPreference.DEFAULT_TARGET_BOARD);
		prefs.setDefault(GokoPreference.KEY_CHECK_UPDATE, GokoPreference.DEFAULT_CHECK_UPDATE);		
		prefs.setDefault(GokoPreference.KEY_CHECK_UPDATE_FREQUENCY, GokoPreference.DEFAULT_CHECK_UPDATE_FREQUENCY);		
		prefs.setDefault(GokoPreference.KEY_SYSTEM_CLEAR_PERSISTED_STATE, GokoPreference.DEFAULT_SYSTEM_CLEAR_PERSISTED_STATE);		
	}

}
