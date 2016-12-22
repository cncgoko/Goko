/**
 * 
 */
package org.goko.tools.serial.jssc.preferences.console;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.tools.serial.jssc.preferences.connection.SerialConnectionPreference;

/**
 * @author Psyko
 * @date 18 déc. 2016
 */
public class JsscConsolePreferenceStoreProvider implements IPreferenceStoreProvider{

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return SerialConnectionPreference.getInstance();	
	}

}
