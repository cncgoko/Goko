package org.goko.tools.shuttlxpress.preferences;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.config.GkPreference;

/**
 * Shuttle XPress integration preferences 
 * @author Psyko
 * @date 25 avr. 2016
 */
public class ShuttleXPressPreferences extends GkPreference{
	/** Name of the node storing the preferences */
	public static final String NODE = "org.goko.tools.shuttlexpress";
	/** NAme of the rapid jog minimum speed property */
	public static final String RAPID_JOG_MIN_SPEED 	= "rapidJogMinSpeed";
	/** NAme of the rapid jog maximum speed property */
	public static final String RAPID_JOG_MAX_SPEED 	= "rapidJogMaxSpeed";
	/** NAme of the precise jog step property */
	public static final String PRECISE_JOG_STEP 	= "preciseJogStep";
	/** NAme of the precise jog speed property */
	public static final String PRECISE_JOG_SPEED 	= "preciseJogSpeed";
	/** Singleton instance */
	private static ShuttleXPressPreferences instance;

	/**
	 * Constructor
	 */
	private ShuttleXPressPreferences(){
		super(NODE);		
	}

	/**
	 * Singleton like access
	 * @return
	 */
	public static ShuttleXPressPreferences getInstance() {
		if(instance == null){
			instance = new ShuttleXPressPreferences();
			instance.initialiseValues();
		}
		return instance;
	}

	/**
	 * Initialise default values
	 */
	private void initialiseValues() {
		getPreferenceStore().setDefault(RAPID_JOG_MIN_SPEED, "100mm/min");
		getPreferenceStore().setDefault(RAPID_JOG_MAX_SPEED, "1500mm/min");
		getPreferenceStore().setDefault(PRECISE_JOG_STEP, "0.1mm");
		getPreferenceStore().setDefault(PRECISE_JOG_SPEED, "150mm/min");
		
		if(StringUtils.isBlank(getPreferenceStore().getString(RAPID_JOG_MIN_SPEED))){
			getPreferenceStore().setToDefault(RAPID_JOG_MIN_SPEED);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(RAPID_JOG_MAX_SPEED))){
			getPreferenceStore().setToDefault(RAPID_JOG_MAX_SPEED);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(PRECISE_JOG_STEP))){
			getPreferenceStore().setToDefault(PRECISE_JOG_STEP);
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(PRECISE_JOG_SPEED))){
			getPreferenceStore().setToDefault(PRECISE_JOG_SPEED);
		}
	}

	/**
	 * @return the rapidJogMinimumSpeed
	 * @throws GkException 
	 */
	public Speed getRapidJogMinimumSpeed() throws GkException {
		return Speed.parse(getPreferenceStore().getString(RAPID_JOG_MIN_SPEED));
	}

	/**
	 * @return the rapidJogMaximumSpeed
	 * @throws GkException 
	 */
	public Speed getRapidJogMaximumSpeed() throws GkException {
		return Speed.parse(getPreferenceStore().getString(RAPID_JOG_MAX_SPEED));
	}

	/**
	 * @return the preciseJogStep
	 * @throws GkException 
	 */
	public Length getPreciseJogStep() throws GkException {
		return Length.parse(getPreferenceStore().getString(PRECISE_JOG_STEP));
	}

	/**
	 * @return the preciseJogSpeed
	 * @throws GkException 
	 */
	public Speed getPreciseJogSpeed() throws GkException {
		return Speed.parse(getPreferenceStore().getString(PRECISE_JOG_SPEED));
	}

}

