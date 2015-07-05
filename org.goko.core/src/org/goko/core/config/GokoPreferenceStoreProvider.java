package org.goko.core.config;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.common.preferences.ScopedPreferenceStore;

public class GokoPreferenceStoreProvider implements IPreferenceStoreProvider {

	@Override
	public IPreferenceStore getPreferenceStore() {
		return new ScopedPreferenceStore(ConfigurationScope.INSTANCE, GokoPreference.NODE_ID);		
	}

}
