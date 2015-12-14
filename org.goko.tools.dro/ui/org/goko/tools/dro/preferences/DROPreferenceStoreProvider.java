package org.goko.tools.dro.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class DROPreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return DROPreferences.getInstance();//new ScopedPreferenceStore(InstanceScope.INSTANCE,"org.goko.tools.droservice");
	}

}
