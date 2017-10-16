/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
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
		GCodePreference prefs = GCodePreference.getInstance();
		prefs.setDefault(GCodePreference.SKIP_COMMENT, true);
		prefs.setDefault(GCodePreference.SKIP_LINE_NUMBER, true);
		prefs.setDefault(GCodePreference.TRUNCATE_DECIMAL, true);
		prefs.setDefault(GCodePreference.DECIMAL_DIGIT_COUNT, 5);
		prefs.setDefault(GCodePreference.DECIMAL_DIGIT_COUNT, 5);
		prefs.setDefault(GCodePreference.ARC_TOLERANCE_CHECK_ENABLED, true);
		prefs.setDefault(GCodePreference.ARC_TOLERANCE_THRESHOLD, Length.valueOf("0.002", LengthUnit.MILLIMETRE));
	}

}
