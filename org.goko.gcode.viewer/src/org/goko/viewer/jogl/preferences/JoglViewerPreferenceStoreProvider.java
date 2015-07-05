package org.goko.viewer.jogl.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.common.preferences.ScopedPreferenceStore;

public class JoglViewerPreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {		 
		return new ScopedPreferenceStore(ConfigurationScope.INSTANCE, JoglViewerPreference.NODE);
		//*return new JoglViewerSettings.getInstance();
	}

}
