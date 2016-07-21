/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * @author Psyko
 * @date 20 juil. 2016
 */
public class RenderingFormatPreferenceInitializer extends GkPreferenceInitializer {

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		RenderingFormatPreference prefs = RenderingFormatPreference.getInstance();
		prefs.setDefault(RenderingFormatPreference.SKIP_COMMENT, true);
		prefs.setDefault(RenderingFormatPreference.SKIP_LINE_NUMBER, true);
		prefs.setDefault(RenderingFormatPreference.TRUNCATE_DECIMAL, true);
		prefs.setDefault(RenderingFormatPreference.DECIMAL_DIGIT_COUNT, 5);

	}

}
