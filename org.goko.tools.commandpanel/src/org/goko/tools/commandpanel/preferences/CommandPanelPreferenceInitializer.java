package org.goko.tools.commandpanel.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

public class CommandPanelPreferenceInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException {		
		// Non existing values will be removed at initialisation		
		CommandPanelPreference.getInstance().setDefault(CommandPanelPreference.A_AXIS_ENABLED, false);
		CommandPanelPreference.getInstance().setDefault(CommandPanelPreference.B_AXIS_ENABLED, false);
		CommandPanelPreference.getInstance().setDefault(CommandPanelPreference.C_AXIS_ENABLED, false);
	}

}
