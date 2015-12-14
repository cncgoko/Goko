package org.goko.tools.serial.jssc.preferences.connection;

import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.core.config.GkPreference;

public class SerialConnectionPreference extends GkPreference {
	/** Node name */
    public static final String PREFERENCE_NODE = "org.goko.tools.serial.jssc";
    private static SerialConnectionPreference _instance;
    
	public SerialConnectionPreference() {
		super(PREFERENCE_NODE);
	}

	/**
	 * Returns the Preference Store
	 * @return {@link IPreferenceStore}
	 */
    public static SerialConnectionPreference getInstance() {
        if(_instance == null) {
        	_instance = new SerialConnectionPreference();        	
        }
        return _instance;
    }
}
