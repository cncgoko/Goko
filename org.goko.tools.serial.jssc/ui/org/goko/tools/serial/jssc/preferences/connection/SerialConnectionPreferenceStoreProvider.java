package org.goko.tools.serial.jssc.preferences.connection;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class SerialConnectionPreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {	
		return SerialConnectionPreference.getInstance();	
	}

}
