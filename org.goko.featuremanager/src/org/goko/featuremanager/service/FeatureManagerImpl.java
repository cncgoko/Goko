package org.goko.featuremanager.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.Activator;
import org.goko.featuremanager.installer.GkInstallableUnit;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Implementation of the default plugin manager 
 * 
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 */
public class FeatureManagerImpl implements IFeatureManager {
	private static final GkLog LOG = GkLog.getLogger(FeatureManagerImpl.class);
	private static final String SERVICE_ID = "org.goko.feature.manager";	
	private IProvisioningAgent provisioningAgent;
	private IProfileRegistry profileRegistry;
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {		
		return SERVICE_ID;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		 BundleContext bundleContext = Activator.getContext();
		 ServiceReference reference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);		 
		 setProvisioningAgent((IProvisioningAgent)bundleContext.getService(reference));
		 setProfileRegistry((IProfileRegistry) getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the provisioningAgent
	 */
	protected IProvisioningAgent getProvisioningAgent() {
		return provisioningAgent;
	}

	/**
	 * @param provisioningAgent the provisioningAgent to set
	 */
	protected void setProvisioningAgent(IProvisioningAgent provisioningAgent) {
		this.provisioningAgent = provisioningAgent;
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getInstallableUnits()
	 */
	@Override
	public List<GkInstallableUnit> getInstallableUnits(IProgressMonitor monitor) throws GkException {
		List<GkInstallableUnit> lstUnits = new ArrayList<GkInstallableUnit>();
		//get the repository managers and define our repositories
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
		
		try {
			manager.addRepository(new URI("http://update.goko.fr/"));
			manager.loadRepository(new URI("http://update.goko.fr/"), monitor);
		} catch (ProvisionException | OperationCanceledException| URISyntaxException e) {
			throw new GkTechnicalException(e);
		}		
		// Query installed units 
		IProfileRegistry registry = (IProfileRegistry) getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
		IProfile profile = registry.getProfile(IProfileRegistry.SELF);		
		Collection<IInstallableUnit> lstInstalledUnits = profile.query(new UserVisibleRootQuery(), monitor).toUnmodifiableSet();
		
		// Query "groups"
		IQuery<IInstallableUnit> query =QueryUtil.createLatestQuery(QueryUtil.createMatchQuery("properties[$0] == $1 && properties[$2] != null", "org.eclipse.equinox.p2.type.group", "true", "org.eclipse.equinox.p2.type.category"));
		Collection<IInstallableUnit> lstInstallableUnit = manager.query(query, monitor).toUnmodifiableSet();
		
		if(CollectionUtils.isNotEmpty(lstInstallableUnit)){
			for (IInstallableUnit iInstallableUnit : lstInstallableUnit) {
				GkInstallableUnit gkUnit = new GkInstallableUnit(iInstallableUnit);
				// Same ID already installed ?
				for (IInstallableUnit installedUnit : lstInstalledUnits) {
					if(StringUtils.equals(gkUnit.getId(), installedUnit.getId())){
						gkUnit.setInstalled(true);
						break;
					}
				}
				
				lstUnits.add(gkUnit);
			}
		}
		
		return lstUnits;
	}
	
	/** (inheritDoc)
	 * @param listener 
	 * @see org.goko.featuremanager.service.IFeatureManager#install(java.util.List)
	 */
	@Override
	public IStatus install(List<GkInstallableUnit> units, IProgressMonitor monitor, IJobChangeListener listener) throws GkException {		
		//get the repository managers and define our repositories
	   IMetadataRepositoryManager manager = (IMetadataRepositoryManager) getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
	   	   
	   List<IInstallableUnit> p2Units = new ArrayList<IInstallableUnit>();
	   for (GkInstallableUnit gkInstallableUnit : units) {
		   if(!gkInstallableUnit.isInstalled()){
			   p2Units.add(gkInstallableUnit.getBaseUnit());
		   }
		}
	   final InstallOperation installOperation = new InstallOperation(new ProvisioningSession(getProvisioningAgent()), p2Units);		
	   LOG.info("installOperation created "+ String.valueOf(installOperation));
		
	   IStatus operationStatus = installOperation.resolveModal(monitor);
	   LOG.info("operationStatus "+ String.valueOf(operationStatus));
		
	   if (operationStatus.getSeverity() > IStatus.WARNING) {
		   LOG.error("installOperation detail :"+installOperation.getResolutionDetails());
		   
		   LOG.error("Install operation failed :"+operationStatus.toString());
		   if(operationStatus.isMultiStatus()){
			   for (IStatus status : operationStatus.getChildren()) {
				   LOG.error("   + "+status.getMessage());
			   }
		   }
		   LOG.error(new CoreException(operationStatus));
		   return operationStatus;
	   }
		
	   final ProvisioningJob provisioningJob = installOperation.getProvisioningJob(monitor);	   
	   LOG.info("ProvisioningJob created "+ String.valueOf(provisioningJob));
	   
	   provisioningJob.schedule();
	   
	   provisioningJob.addJobChangeListener(listener);
	   
	   LOG.info("Installation of feature done");
	   
	   return operationStatus;
	}

	/**
	 * @return the profileRegistry
	 */
	public IProfileRegistry getProfileRegistry() {
		return profileRegistry;
	}

	/**
	 * @param profileRegistry the profileRegistry to set
	 */
	public void setProfileRegistry(IProfileRegistry profileRegistry) {
		this.profileRegistry = profileRegistry;
	}
}
