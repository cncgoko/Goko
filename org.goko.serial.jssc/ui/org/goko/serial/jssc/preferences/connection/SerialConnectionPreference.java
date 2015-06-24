package org.goko.serial.jssc.preferences.connection;

import jssc.SerialPort;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.common.preferences.IPreferenceStoreProvider;
import org.goko.core.config.GkPreference;
import org.goko.serial.jssc.service.JsscParameter;

public class SerialConnectionPreference extends GkPreference {
	/** Node name */
    public static final String PREFERENCE_NODE = "org.goko.serial.jssc";
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
        	_instance.initialize();
        }
        return _instance;
    }

    /**
     * Initialize the preference store
     */
	protected void initialize() {
		getPreferenceStore().setDefault(JsscParameter.BAUDRATE.toString(), 	String.valueOf(SerialPort.BAUDRATE_115200));
		getPreferenceStore().setDefault(JsscParameter.DATABITS.toString(), 	String.valueOf(SerialPort.DATABITS_8));
		getPreferenceStore().setDefault(JsscParameter.STOPBITS.toString(), 	String.valueOf(SerialPort.STOPBITS_1));
		getPreferenceStore().setDefault(JsscParameter.PARITY.toString(), 	String.valueOf(SerialPort.PARITY_NONE));
		getPreferenceStore().setDefault(JsscParameter.RCSCTS.toString(), 	String.valueOf(true));
		getPreferenceStore().setDefault(JsscParameter.XONXOFF.toString(), 	String.valueOf(true));


		if(StringUtils.isBlank(getPreferenceStore().getString(JsscParameter.BAUDRATE.toString()))){
			getPreferenceStore().setToDefault(JsscParameter.BAUDRATE.toString());
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(JsscParameter.DATABITS.toString()))){
			getPreferenceStore().setToDefault(JsscParameter.DATABITS.toString());
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(JsscParameter.STOPBITS.toString()))){
			getPreferenceStore().setToDefault(JsscParameter.STOPBITS.toString());
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(JsscParameter.RCSCTS.toString()))){
			getPreferenceStore().setToDefault(JsscParameter.RCSCTS.toString());
		}
		if(StringUtils.isBlank(getPreferenceStore().getString(JsscParameter.XONXOFF.toString()))){
			getPreferenceStore().setToDefault(JsscParameter.XONXOFF.toString());
		}
	}
}
