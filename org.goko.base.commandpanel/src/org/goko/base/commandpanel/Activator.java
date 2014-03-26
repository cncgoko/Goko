package org.goko.base.commandpanel;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private static IPreferenceStore preferenceStore;

	@Inject
	private EPartService injectedPartService;

	public static final IScopeContext SCOPE_CONTEXT = ConfigurationScope.INSTANCE;
    public static final String PREFERENCE_NODE = "org.goko.base.commandpanel";

	static BundleContext getContext() {
		return context;
	}


    public static IPreferenceStore getPreferenceStore() {
        if(preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(SCOPE_CONTEXT, PREFERENCE_NODE);
        }

        org.eclipse.e4.ui.internal.workbench.ResourceHandler handler;

        return preferenceStore;
    }

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
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
