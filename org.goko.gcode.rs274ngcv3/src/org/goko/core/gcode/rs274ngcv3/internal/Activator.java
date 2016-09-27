package org.goko.core.gcode.rs274ngcv3.internal;

import java.util.ResourceBundle;

import org.goko.core.common.i18n.MessageResource;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator{
	private static Activator instance;
	private IRS274NGCService rs274ngcService;	
	
	public Activator() {
		instance = this;
	}
	
	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {	
		MessageResource.registerResourceBundle(ResourceBundle.getBundle("org.goko.core.gcode.rs274ngcv3.Messages"));
	//	ServiceReference<IRS274NGCService> rs274Servicereference = context.getServiceReference(IRS274NGCService.class);
	//	rs274ngcService = context.getService(rs274Servicereference);		
	}

	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

//	public static IRS274NGCService getRS274NGCService(){
//		return getInstance().rs274ngcService;
//	}

	/**
	 * @return the instance of this activator
	 */
	public static Activator getInstance() {
		if(instance == null){
			instance = new Activator();
		}
		return instance;
	}

}
