package org.goko.base.commandpanel;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private static IEclipsePreferences preferences;

	public static final IScopeContext SCOPE_CONTEXT = ConfigurationScope.INSTANCE;
    public static final String PREFERENCE_NODE 		= "org.goko.base.commandpanel";

	static BundleContext getContext() {
		return context;
	}


    public static IEclipsePreferences getPreferences() {
        return preferences;
    }

    private static void initDefaultPreferences() {
    	if(preferences.get(CommandPanelParameter.JOG_FEEDRATE, null) == null){
    		preferences.put(CommandPanelParameter.JOG_FEEDRATE, "1000");
    	}
	}
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		preferences = InstanceScope.INSTANCE.getNode(PREFERENCE_NODE);
		initDefaultPreferences();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		preferences.flush();
	}

}
