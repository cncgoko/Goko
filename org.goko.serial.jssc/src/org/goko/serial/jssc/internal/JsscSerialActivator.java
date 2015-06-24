package org.goko.serial.jssc.internal;

import org.goko.serial.jssc.preferences.connection.SerialConnectionPreference;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Jssc Serial activator
 *
 * @author PsyKo
 *
 */
public class JsscSerialActivator implements BundleActivator {

    /** Bundle context */
	private static BundleContext context;

	/**
	 * Returns the context
	 * @return {@link BundleContext}
	 */
	static BundleContext getContext() {
		return context;
	}

	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		JsscSerialActivator.context = bundleContext;
		SerialConnectionPreference.getInstance();
	}


	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		JsscSerialActivator.context = null;
	}

}
