package org.goko.controller.tinyg.controller.prefs;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.config.GkPreference;

public class TinyGPreferences extends GkPreference{	
	public static final String NODE = "org.goko.controller.tinyg";

	public static final String HOMING_ENABLED_AXIS_X 	= "homingEnabledAxisX";
	public static final String HOMING_ENABLED_AXIS_Y 	= "homingEnabledAxisY";
	public static final String HOMING_ENABLED_AXIS_Z 	= "homingEnabledAxisZ";
	public static final String HOMING_ENABLED_AXIS_A 	= "homingEnabledAxisA";
	public static final String PLANNER_BUFFER_SPACE_CHECK = "plannerBufferSpaceCheck";

	private static TinyGPreferences instance;

	private TinyGPreferences(){
		super(NODE);		
	}

	public static TinyGPreferences getInstance() {
		if(instance == null){
			instance = new TinyGPreferences();
			instance.initialiseValues();
		}
		return instance;
	}

	private void initialiseValues() {
		getPreferenceStore().setDefault(HOMING_ENABLED_AXIS_X, "true");
		getPreferenceStore().setDefault(HOMING_ENABLED_AXIS_Y, "true");
		getPreferenceStore().setDefault(HOMING_ENABLED_AXIS_Z, "true");
		getPreferenceStore().setDefault(HOMING_ENABLED_AXIS_A, "false");
		
		getPreferenceStore().setDefault(PLANNER_BUFFER_SPACE_CHECK, "true");
		
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

