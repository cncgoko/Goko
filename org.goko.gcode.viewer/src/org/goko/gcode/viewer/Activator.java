package org.goko.gcode.viewer;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static IPreferenceStore preferenceStore;

	private static BundleContext context;

	public static final IScopeContext SCOPE_CONTEXT = ConfigurationScope.INSTANCE;
    public static final String PREFERENCE_NODE = "net.openchrom.calculator.ui";

	static BundleContext getContext() {
		return context;
	}


    public static IPreferenceStore getPreferenceStore() {

        if(preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(SCOPE_CONTEXT, PREFERENCE_NODE);
        }
        return preferenceStore;
    }

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//System.loadLibrary("gluegen-rt");


	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
