package org.goko.tinyg.controller.prefs;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class TinyGPreferences{	
	public static final String NODE = "org.goko.tinyg";

	public static final String HOMING_ENABLED_AXIS_X 	= "homingEnabledAxisX";
	public static final String HOMING_ENABLED_AXIS_Y 	= "homingEnabledAxisY";
	public static final String HOMING_ENABLED_AXIS_Z 	= "homingEnabledAxisZ";
	public static final String HOMING_ENABLED_AXIS_A 	= "homingEnabledAxisA";
	public static final String PLANNER_BUFFER_SPACE_CHECK = "plannerBufferSpaceCheck";

	
	private ScopedPreferenceStore preferenceStore;
	private static TinyGPreferences instance;

	private TinyGPreferences(){
		preferenceStore = new ScopedPreferenceStore(ConfigurationScope.INSTANCE,NODE);
	}

	public static TinyGPreferences getInstance() {
		if(instance == null){
			instance = new TinyGPreferences();
			instance.initialiseValues();
		}
		return instance;
	}

	private void initialiseValues() {
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_X, "true");
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_Y, "true");
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_Z, "true");
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_A, "false");
		
		preferenceStore.setDefault(PLANNER_BUFFER_SPACE_CHECK, "true");
		
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_X))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_X);
		}
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_Y))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_Y);
		}
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_Z))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_Z);
		}
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_A))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_A);
		}
		if(StringUtils.isBlank(preferenceStore.getString(PLANNER_BUFFER_SPACE_CHECK))){
			preferenceStore.setToDefault(PLANNER_BUFFER_SPACE_CHECK);
		}
	}
	
	public void save() throws GkException{
		try {
			preferenceStore.save();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	public boolean isHomingEnabledAxisX() {
		return BooleanUtils.toBoolean(preferenceStore.getString(HOMING_ENABLED_AXIS_X));
	}


	public void setHomingEnabledAxisX(boolean enabled) {
		preferenceStore.setValue(HOMING_ENABLED_AXIS_X, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisY() {
		return BooleanUtils.toBoolean(preferenceStore.getString(HOMING_ENABLED_AXIS_Y));
	}


	public void setHomingEnabledAxisY(boolean enabled) {
		preferenceStore.setValue(HOMING_ENABLED_AXIS_Y, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisZ() {
		return BooleanUtils.toBoolean(preferenceStore.getString(HOMING_ENABLED_AXIS_Z));
	}


	public void setHomingEnabledAxisZ(boolean enabled) {
		preferenceStore.setValue(HOMING_ENABLED_AXIS_Z, Boolean.valueOf(enabled));
	}
	
	public boolean isHomingEnabledAxisA() {
		return BooleanUtils.toBoolean(preferenceStore.getString(HOMING_ENABLED_AXIS_A));
	}


	public void setHomingEnabledAxisA(boolean enabled) {
		preferenceStore.setValue(HOMING_ENABLED_AXIS_A, Boolean.valueOf(enabled));
	}
	
	public boolean isPlannerBufferSpaceCheck() {
		String t = preferenceStore.getString(PLANNER_BUFFER_SPACE_CHECK);
		return BooleanUtils.toBoolean(t);
	}

	public void setPlannerBufferSpaceCheck(boolean enabled) {
		preferenceStore.setValue(PLANNER_BUFFER_SPACE_CHECK, Boolean.valueOf(enabled));
	}

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	
}

