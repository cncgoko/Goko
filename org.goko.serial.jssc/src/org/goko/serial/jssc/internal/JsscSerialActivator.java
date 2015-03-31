package org.goko.serial.jssc.internal;

import jssc.SerialPort;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.serial.jssc.service.JsscParameter;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Jssc Serial activator
 *
 * @author PsyKo
 *
 */
public class JsscSerialActivator implements BundleActivator {
	/** JSSC Prefernce Store */
	private static ScopedPreferenceStore preferenceStore;
	/** Preference scope */
	public static final IScopeContext SCOPE_CONTEXT = ConfigurationScope.INSTANCE;
	/** Node name */
    public static final String PREFERENCE_NODE = "org.goko.serial.jssc";
    /** Bundle context */
	private static BundleContext context;

	/**
	 * Returns the context
	 * @return {@link BundleContext}
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Returns the Preference Store
	 * @return {@link IPreferenceStore}
	 */
    public static ScopedPreferenceStore getPreferenceStore() {
        if(preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(SCOPE_CONTEXT, PREFERENCE_NODE);
            initSerialDefaultPreferences();
        }
        return preferenceStore;
    }

    /**
     * Initialize the preference store
     */
	private static void initSerialDefaultPreferences() {
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.BAUDRATE.toString(), 	String.valueOf(SerialPort.BAUDRATE_115200));
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.DATABITS.toString(), 	String.valueOf(SerialPort.DATABITS_8));
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.STOPBITS.toString(), 	String.valueOf(SerialPort.STOPBITS_1));
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.PARITY.toString(), 	String.valueOf(SerialPort.PARITY_NONE));
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.RCSCTS.toString(), 	String.valueOf(true));
		JsscSerialActivator.getPreferenceStore().setDefault(JsscParameter.XONXOFF.toString(), 	String.valueOf(true));


		if(StringUtils.isBlank(JsscSerialActivator.getPreferenceStore().getString(JsscParameter.BAUDRATE.toString()))){
			JsscSerialActivator.getPreferenceStore().setToDefault(JsscParameter.BAUDRATE.toString());
		}
		if(StringUtils.isBlank(JsscSerialActivator.getPreferenceStore().getString(JsscParameter.DATABITS.toString()))){
			JsscSerialActivator.getPreferenceStore().setToDefault(JsscParameter.DATABITS.toString());
		}
		if(StringUtils.isBlank(JsscSerialActivator.getPreferenceStore().getString(JsscParameter.STOPBITS.toString()))){
			JsscSerialActivator.getPreferenceStore().setToDefault(JsscParameter.STOPBITS.toString());
		}
		if(StringUtils.isBlank(JsscSerialActivator.getPreferenceStore().getString(JsscParameter.RCSCTS.toString()))){
			JsscSerialActivator.getPreferenceStore().setToDefault(JsscParameter.RCSCTS.toString());
		}
		if(StringUtils.isBlank(JsscSerialActivator.getPreferenceStore().getString(JsscParameter.XONXOFF.toString()))){
			JsscSerialActivator.getPreferenceStore().setToDefault(JsscParameter.XONXOFF.toString());
		}
	}


	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		JsscSerialActivator.context = bundleContext;
	}


	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		JsscSerialActivator.context = null;
	}

}
