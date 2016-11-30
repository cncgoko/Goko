package org.goko.tools.dro.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;
import org.goko.core.controller.bean.DefaultControllerValues;

public class DROPreferenceInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException {
		DROPreferences prefs = DROPreferences.getInstance();
		//prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_ENABLED, true);
		String defValues = DefaultControllerValues.STATE + DROPreferences.DISPLAYED_VALUES_SEPARATOR +
				DefaultControllerValues.POSITION_X + DROPreferences.DISPLAYED_VALUES_SEPARATOR +
				DefaultControllerValues.POSITION_Y + DROPreferences.DISPLAYED_VALUES_SEPARATOR +
				DefaultControllerValues.POSITION_Z + DROPreferences.DISPLAYED_VALUES_SEPARATOR;
		
		// Non existing values will be removed at initialisation		
		DROPreferences.getInstance().setDefault(DROPreferences.KEY_DISPLAYED_VALUES, defValues);
	}

}
