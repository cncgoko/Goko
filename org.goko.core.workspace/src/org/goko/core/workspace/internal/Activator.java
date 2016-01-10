/**
 * 
 */
package org.goko.core.workspace.internal;

import org.goko.core.workspace.service.IWorkspaceUIService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class Activator implements BundleActivator{
	/** Workspace UI Service */
	private IWorkspaceUIService workspaceUIService;
	private BundleContext context;
	/** Activator instance */
	private static Activator instance;

	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;		
		instance = this;
	}

	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public static IWorkspaceUIService getWorkspaceUIService(){
		if(getInstance().workspaceUIService == null){
			getInstance().workspaceUIService = getInstance().getService(IWorkspaceUIService.class);
		}
		return getInstance().workspaceUIService;
	}

	protected <S> S getService(Class<S> serviceClass){
		ServiceReference<S> reference = context.getServiceReference(serviceClass);
		return context.getService(reference);
	}
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
