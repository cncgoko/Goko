package org.goko.gcode.rs274ngcv3.config;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.common.preferences.ScopedPreferenceStore;

public class RS274PreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return new ScopedPreferenceStore(ConfigurationScope.INSTANCE, RS274Preference.NODE_ID);
		//return RS274Config.getInstance();
	}

}
