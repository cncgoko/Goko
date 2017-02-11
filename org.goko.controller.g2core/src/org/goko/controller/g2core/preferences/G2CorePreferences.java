package org.goko.controller.g2core.preferences;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.config.GkPreference;

public class G2CorePreferences extends GkPreference{	
	public static final String NODE = "org.goko.controller.g2core";
	
	public static final String HOMING_ENABLED_AXIS_X 	= "homingEnabledAxisX";
	public static final String HOMING_ENABLED_AXIS_Y 	= "homingEnabledAxisY";
	public static final String HOMING_ENABLED_AXIS_Z 	= "homingEnabledAxisZ";
	public static final String HOMING_ENABLED_AXIS_A 	= "homingEnabledAxisA";
	public static final String PLANNER_BUFFER_SPACE_CHECK = "plannerBufferSpaceCheck";

	public static final String STATUS_REPORT_MACHINE_STATE		= "srMachineState";
	public static final String STATUS_REPORT_VELOCITY			= "srVelocity";
	public static final String STATUS_REPORT_FEEDRATE			= "srFeedrate";
	public static final String STATUS_REPORT_UNITS				= "srUnits";
	public static final String STATUS_REPORT_COORDINATE_SYSTEM	= "srCoordinateSystem";
	public static final String STATUS_REPORT_MOTION_MODE		= "srMotionMode";
	public static final String STATUS_REPORT_PLANE				= "srPlane";
	public static final String STATUS_REPORT_PATH_CONTROL		= "srPathControl";
	public static final String STATUS_REPORT_DISTANCE_MODE		= "srDistanceMode";
	public static final String STATUS_REPORT_ARC_DISTANCE_MODE	= "srArcDistanceMode";
	public static final String STATUS_REPORT_FEEDRATE_MODE		= "srFeedrateMode";
	public static final String STATUS_REPORT_TOOL 				= "srTool";
	public static final String STATUS_REPORT_G92				= "srG92";
	
	public static final String STATUS_REPORT_POS_X		= "srPosX";
	public static final String STATUS_REPORT_POS_Y		= "srPosY";
	public static final String STATUS_REPORT_POS_Z		= "srPosZ";
	public static final String STATUS_REPORT_POS_A		= "srPosA";
	public static final String STATUS_REPORT_POS_B		= "srPosB";
	public static final String STATUS_REPORT_POS_C		= "srPosC";
	
	public static final String STATUS_REPORT_WPOS		= "srWPos";
	public static final String STATUS_REPORT_MPOS		= "srMPos";

	
	private static G2CorePreferences instance;

	private G2CorePreferences(){
		super(NODE);		
	}

	public static G2CorePreferences getInstance() {
		if(instance == null){
			instance = new G2CorePreferences();
			instance.initialiseValues();
		}
		return instance;
	}

	private void initialiseValues() {

		
		if(StringUtils.isBlank(getPreferenceStore().getString(HOMING_ENABLED_AXIS_X))){
			getPreferenceStore().setToDefault(HOMING_ENABLED_AXIS_X);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(HOMING_ENABLED_AXIS_Y))){
			getPreferenceStore().setToDefault(HOMING_ENABLED_AXIS_Y);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(HOMING_ENABLED_AXIS_Z))){
			getPreferenceStore().setToDefault(HOMING_ENABLED_AXIS_Z);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(HOMING_ENABLED_AXIS_A))){
			getPreferenceStore().setToDefault(HOMING_ENABLED_AXIS_A);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(PLANNER_BUFFER_SPACE_CHECK))){
			getPreferenceStore().setToDefault(PLANNER_BUFFER_SPACE_CHECK);
		}
	}
	
	public boolean isHomingConfigured() {
		return isHomingEnabledAxisX()
			|| isHomingEnabledAxisY()
			|| isHomingEnabledAxisZ()
			|| isHomingEnabledAxisA();
	}
	
	public boolean isHomingEnabledAxisX() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(HOMING_ENABLED_AXIS_X));
	}


	public void setHomingEnabledAxisX(boolean enabled) {
		getPreferenceStore().setValue(HOMING_ENABLED_AXIS_X, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisY() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(HOMING_ENABLED_AXIS_Y));
	}


	public void setHomingEnabledAxisY(boolean enabled) {
		getPreferenceStore().setValue(HOMING_ENABLED_AXIS_Y, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisZ() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(HOMING_ENABLED_AXIS_Z));
	}


	public void setHomingEnabledAxisZ(boolean enabled) {
		getPreferenceStore().setValue(HOMING_ENABLED_AXIS_Z, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisA() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(HOMING_ENABLED_AXIS_A));
	}


	public void setHomingEnabledAxisA(boolean enabled) {
		getPreferenceStore().setValue(HOMING_ENABLED_AXIS_A, Boolean.valueOf(enabled));
	}
	
	public boolean isPlannerBufferSpaceCheck() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(PLANNER_BUFFER_SPACE_CHECK));
	}

	public void setPlannerBufferSpaceCheck(boolean enabled) {
		getPreferenceStore().setValue(PLANNER_BUFFER_SPACE_CHECK, Boolean.valueOf(enabled));
	}
}

