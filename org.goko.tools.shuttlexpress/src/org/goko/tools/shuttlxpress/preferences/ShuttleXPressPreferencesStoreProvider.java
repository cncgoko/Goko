package org.goko.tools.shuttlxpress.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

/**
 * Store provider for the Shuttle XPress integration preferences 
 * @author Psyko
 * @date 25 avr. 2016
 */
public class ShuttleXPressPreferencesStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return ShuttleXPressPreferences.getInstance();
	}

}
