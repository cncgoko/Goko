/**
 * 
 */
package org.goko.controller.g2core.preferences;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreferenceInitializer;

/**
 * @author PsyKo
 *
 */
public class G2CorePreferencesInitializer extends GkPreferenceInitializer{

	/** (inheritDoc)
	 * @see org.goko.core.config.GkPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	protected void initializeDefaultGkPreferences() throws GkException {
		G2CorePreferences prefs = G2CorePreferences.getInstance();
		
		prefs.setDefault(G2CorePreferences.HOMING_ENABLED_AXIS_X, "false");
		prefs.setDefault(G2CorePreferences.HOMING_ENABLED_AXIS_Y, "false");
		prefs.setDefault(G2CorePreferences.HOMING_ENABLED_AXIS_Z, "false");
		prefs.setDefault(G2CorePreferences.HOMING_ENABLED_AXIS_A, "false");
		
		prefs.setDefault(G2CorePreferences.PLANNER_BUFFER_SPACE_CHECK, "true");
		
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_MACHINE_STATE, 		"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_VELOCITY, 				"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_FEEDRATE,				"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_UNITS, 				"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_COORDINATE_SYSTEM, 	"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_MOTION_MODE, 			"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_PLANE, 				"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_PATH_CONTROL, 			"false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_DISTANCE_MODE, 		"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_ARC_DISTANCE_MODE, 	"false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_FEEDRATE_MODE, 		"false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_TOOL,				 	"false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_G92, 					"false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_X, 				"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_Y,                 "true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_Z,                 "true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_A,                 "false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_B,                 "false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_POS_C,                 "false");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_WPOS,                	"true");
		prefs.setDefault(G2CorePreferences.STATUS_REPORT_MPOS,                	"true");		


	}

}
