package org.goko.serial.jssc.preferences.connection;

import jssc.SerialPort;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreference;
import org.goko.core.config.GkPreferenceInitializer;
import org.goko.serial.jssc.service.JsscParameter;

public class SerialConnectionPreferenceInitializer extends GkPreferenceInitializer {

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException{
		GkPreference prefs = SerialConnectionPreference.getInstance();
		prefs.setDefault(JsscParameter.BAUDRATE.toString(), 	String.valueOf(SerialPort.BAUDRATE_115200));
		prefs.setDefault(JsscParameter.DATABITS.toString(), 	String.valueOf(SerialPort.DATABITS_8));
		prefs.setDefault(JsscParameter.STOPBITS.toString(), 	String.valueOf(SerialPort.STOPBITS_1));
		prefs.setDefault(JsscParameter.PARITY.toString(), 	String.valueOf(SerialPort.PARITY_NONE));
		prefs.setDefault(JsscParameter.RCSCTS.toString(), 	String.valueOf(true));
		prefs.setDefault(JsscParameter.XONXOFF.toString(), 	String.valueOf(true));
	}

}
