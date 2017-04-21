package org.goko.controller.grbl.v11.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class Grbl11PreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return Grblv11Preferences.getInstance();
	}

}
