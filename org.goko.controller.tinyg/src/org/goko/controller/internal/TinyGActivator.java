package org.goko.controller.internal;

import java.util.ResourceBundle;

import org.goko.core.common.i18n.MessageResource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class TinyGActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		MessageResource.registerResourceBundle(ResourceBundle.getBundle("org.goko.controller.i18n.Messages"));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
