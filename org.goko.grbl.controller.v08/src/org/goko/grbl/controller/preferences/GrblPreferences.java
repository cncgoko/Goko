package org.goko.grbl.controller.preferences;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * Grbl preferences
 * @author PsyKo
 * @deprecated not configurable in Grbl v0.8
 */
@Deprecated
public class GrblPreferences {
	/** Preferences Node name*/
	public static final String NODE = "org.goko.grbl.v8";
	/** Property describing if the homing is enabled on the X axis */
	public static final String HOMING_ENABLED_AXIS_X 	= "homingEnabledAxisX";
	public static final String HOMING_ENABLED_AXIS_Y 	= "homingEnabledAxisY";
	public static final String HOMING_ENABLED_AXIS_Z 	= "homingEnabledAxisZ";

	private ScopedPreferenceStore preferenceStore;
	private static GrblPreferences instance;

	private GrblPreferences(){
		preferenceStore = new ScopedPreferenceStore(ConfigurationScope.INSTANCE,NODE);
	}

	public static GrblPreferences getInstance() {
		if(instance == null){
			instance = new GrblPreferences();
			instance.initialiseValues();
		}
		return instance;
	}

	private void initialiseValues() {
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_X, "true");
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_Y, "true");
		preferenceStore.setDefault(HOMING_ENABLED_AXIS_Z, "true");
		
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_X))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_X);
		}
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_Y))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_Y);
		}
		if(StringUtils.isBlank(preferenceStore.getString(HOMING_ENABLED_AXIS_Z))){
			preferenceStore.setToDefault(HOMING_ENABLED_AXIS_Z);
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

	/**
	 * @return the preferenceStore
	 */
	protected ScopedPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
}
