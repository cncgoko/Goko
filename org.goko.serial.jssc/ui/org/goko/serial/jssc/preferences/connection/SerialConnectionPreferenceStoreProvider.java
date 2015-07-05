package org.goko.serial.jssc.preferences.connection;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.common.preferences.ScopedPreferenceStore;

public class SerialConnectionPreferenceStoreProvider implements IPreferenceStoreProvider {

	@Override
	public IPreferenceStore getPreferenceStore() {	
		return new ScopedPreferenceStore(ConfigurationScope.INSTANCE, SerialConnectionPreference.PREFERENCE_NODE);
		//return SerialConnectionPreference.getInstance();
	}

}
