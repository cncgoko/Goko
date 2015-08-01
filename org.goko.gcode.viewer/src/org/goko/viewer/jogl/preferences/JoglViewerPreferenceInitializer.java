package org.goko.viewer.jogl.preferences;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.config.GkPreferenceInitializer;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.bean.Tuple6b;

public class JoglViewerPreferenceInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException {
		JoglViewerPreference prefs = JoglViewerPreference.getInstance();
		prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_ENABLED, true);
		prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_DIRECTION, "X");
		prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_POSITION_X, "0");
		prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_POSITION_Y, "0");
		prefs.setDefault(JoglViewerPreference.ROTARY_AXIS_POSITION_Z, "0");
		int ms = prefs.getInt(JoglViewerPreference.MULTISAMPLING);
		prefs.setDefault(JoglViewerPreference.MULTISAMPLING, "1");
		prefs.setDefault(JoglViewerPreference.MAJOR_GRID_SPACING, "10");
		prefs.setDefault(JoglViewerPreference.MINOR_GRID_SPACING, "1");
		
		Tuple6b position = new Tuple6b();
		position.setX( NumberQuantity.of(new BigDecimal( prefs.getString(JoglViewerPreference.ROTARY_AXIS_POSITION_X)), GokoPreference.getInstance().getLengthUnit()));
		position.setY( NumberQuantity.of(new BigDecimal( prefs.getString(JoglViewerPreference.ROTARY_AXIS_POSITION_Y)), GokoPreference.getInstance().getLengthUnit()));
		position.setZ( NumberQuantity.of(new BigDecimal( prefs.getString(JoglViewerPreference.ROTARY_AXIS_POSITION_Z)), GokoPreference.getInstance().getLengthUnit()));
		
		prefs.setRotaryAxisPosition(position);
	}

}
