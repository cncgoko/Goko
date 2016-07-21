package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;

public class RenderingFormatPreferenceStoreProvider implements IPreferenceStoreProvider {

	/** (inheritDoc)
	 * @see org.goko.common.preferences.IPreferenceStoreProvider#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {		 
		return RenderingFormatPreference.getInstance();
	}

}
