package org.goko.core.config;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class GokoConfigPreferenceStoreProvider implements IPreferenceStoreProvider {

	@Override
	public IPreferenceStore getPreferenceStore() {		
		return GokoPreference.getInstance().getPreferenceStore();
	}

}
