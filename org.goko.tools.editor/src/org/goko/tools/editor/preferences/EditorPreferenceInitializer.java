package org.goko.tools.editor.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

public class EditorPreferenceInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException {
		EditorPreference prefs = EditorPreference.getInstance();
		//prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_ENABLED, true);
		
		prefs.setFontSize(11);		
	}

}
