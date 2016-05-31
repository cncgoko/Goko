package org.goko.tools.editor.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class EditorPreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {		 
		return EditorPreference.getInstance();
	}

}
