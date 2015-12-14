package org.goko.tools.serial.jssc.preferences.connection;

import org.goko.core.common.exception.GkException;
import org.goko.core.config.GkPreference;
import org.goko.core.config.GkPreferenceInitializer;
import org.goko.core.connection.serial.SerialParameter;
import org.goko.tools.serial.jssc.service.JsscParameter;

public class SerialConnectionPreferenceInitializer extends GkPreferenceInitializer {

	/** (inheritDoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultGkPreferences()
	 */
	@Override
	public void initializeDefaultGkPreferences() throws GkException{
		GkPreference prefs = SerialConnectionPreference.getInstance();
		prefs.setDefault(JsscParameter.BAUDRATE.toString(), 	String.valueOf(SerialParameter.BAUDRATE_115200));
		prefs.setDefault(JsscParameter.DATABITS.toString(), 	String.valueOf(SerialParameter.DATABITS_8));
		prefs.setDefault(JsscParameter.STOPBITS.toString(), 	String.valueOf(SerialParameter.STOPBITS_1));
		prefs.setDefault(JsscParameter.PARITY.toString(), 		String.valueOf(SerialParameter.PARITY_NONE));
		prefs.setDefault(JsscParameter.FLOWCONTROL.toString(), 	String.valueOf(SerialParameter.FLOWCONTROL_RTSCTS));		
	}

}
