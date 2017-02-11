/**
 * 
 */
package org.goko.controller.g2core.controller.preferences;

import org.apache.commons.lang3.BooleanUtils;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.core.config.GkPreference;

/**
 * @author Psyko
 * @date 11 janv. 2017
 */
public class G2CorePreferences extends GkPreference {
	/** Preferences Node name */
	public static final String NODE = "org.goko.controller.g2core";
	/** Singleton instance */
	private static G2CorePreferences instance;

	/**
	 * Constructor
	 */
	private G2CorePreferences(){
		super(NODE);
	}

	/**
	 * Singleton access
	 * @return G2CorePreferences
	 */
	public static G2CorePreferences getInstance() {
		if(instance == null){
			instance = new G2CorePreferences();
		}
		return instance;
	}
	
	/**
	 * Getter for the HomingSequenceConfigured flag
	 * @return <code>true</code> if the sequence is configured, <code>false</code> otherwise
	 */
	public boolean isHomingSequenceConfigured() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(G2Core.Preferences.HOMING_SEQUENCE_CONFIGURED));
	}

	/**
	 * Setter for the HomingSequenceConfigured flag
	 * @param configured <code>true</code> if the sequence is configured, <code>false</code> otherwise
	 */
	public void setHomingSequenceConfigured(boolean configured) {
		getPreferenceStore().setValue(G2Core.Preferences.HOMING_SEQUENCE_CONFIGURED, Boolean.valueOf(configured));
	}
	/**
	 * Getter for the X Axis Homing Sequence flag
	 * @return <code>true</code> if the homing is enabled on X axis, <code>false</code> otherwise
	 */
	public boolean isHomingEnabledAxisX() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(G2Core.Preferences.HOMING_ENABLED_AXIS_X));
	}

	/**
	 * Setter for the X Axis Homing Sequence flag
	 * @param enabled <code>true</code> if the homing is enabled on X axis, <code>false</code> otherwise
	 */
	public void setHomingEnabledAxisX(boolean enabled) {
		getPreferenceStore().setValue(G2Core.Preferences.HOMING_ENABLED_AXIS_X, Boolean.valueOf(enabled));
	}
	
	/**
	 * Getter for the Y Axis Homing Sequence flag
	 * @return <code>true</code> if the homing is enabled on Y axis, <code>false</code> otherwise
	 */
	public boolean isHomingEnabledAxisY() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(G2Core.Preferences.HOMING_ENABLED_AXIS_Y));
	}

	/**
	 * Setter for the Y Axis Homing Sequence flag
	 * @param enabled <code>true</code> if the homing is enabled on Y axis, <code>false</code> otherwise
	 */
	public void setHomingEnabledAxisY(boolean enabled) {
		getPreferenceStore().setValue(G2Core.Preferences.HOMING_ENABLED_AXIS_Y, Boolean.valueOf(enabled));
	}
	
	/**
	 * Getter for the Z Axis Homing Sequence flag
	 * @return <code>true</code> if the homing is enabled on Z axis, <code>false</code> otherwise
	 */
	public boolean isHomingEnabledAxisZ() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(G2Core.Preferences.HOMING_ENABLED_AXIS_Z));
	}

	/**
	 * Setter for the Z Axis Homing Sequence flag
	 * @param enabled <code>true</code> if the homing is enabled on Z axis, <code>false</code> otherwise
	 */
	public void setHomingEnabledAxisZ(boolean enabled) {
		getPreferenceStore().setValue(G2Core.Preferences.HOMING_ENABLED_AXIS_Z, Boolean.valueOf(enabled));
	}
	
	/**
	 * Getter for the A Axis Homing Sequence flag
	 * @return <code>true</code> if the homing is enabled on A axis, <code>false</code> otherwise
	 */
	public boolean isHomingEnabledAxisA() {
		return BooleanUtils.toBoolean(getPreferenceStore().getString(G2Core.Preferences.HOMING_ENABLED_AXIS_A));
	}

	/**
	 * Setter for the A Axis Homing Sequence flag
	 * @param enabled <code>true</code> if the homing is enabled on A axis, <code>false</code> otherwise
	 */
	public void setHomingEnabledAxisA(boolean enabled) {
		getPreferenceStore().setValue(G2Core.Preferences.HOMING_ENABLED_AXIS_A, Boolean.valueOf(enabled));
	}
	
}
