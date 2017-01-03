package goko;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.core.log.LogUtils;

public class GokoUpdateCheckRunnable {
	private static final GkLog LOG = GkLog.getLogger(GokoUpdateCheckRunnable.class);
	private boolean cancelled;
	/** Nothing to update status */
	public static final IStatus NOTHING_TO_UPDATE = new Status(Status.OK, "Goko", 10000, "", null);
	/** Update available status */ 
	public static final IStatus UPDATE_AVAILABLE = new Status(Status.OK, "Goko", 10001, "", null);
	/** Default update site location */
	private static final String UPDATE_SITE_URL = "http://update.goko.fr/";
	/** Developer mode update site location */
	private static final String DEV_UPDATE_SITE_URL = "http://update.goko.fr/dev/";
	/** The update operation */
	private UpdateOperation operation;
	
	public IStatus update(final IProvisioningAgent agent, final IProgressMonitor monitor, final UISynchronize sync, final IWorkbench workbench, boolean silent){		
		if(GokoPreference.getInstance().isDevEnvironment()){
			// Don't perform update check in development environment
			LOG.info("Update check disabled in dev environment");			
			return NOTHING_TO_UPDATE;
		}		
		ProvisioningSession session = new ProvisioningSession(agent);
		
		// update the whole running profile, otherwise specify IUs
		operation = new UpdateOperation(session);
		
		final SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent.getService(IArtifactRepositoryManager.SERVICE_NAME);
		
		if(GokoPreference.getInstance().isDeveloperMode()){			
			LOG.info("********** DEV MODE UPDATE **********");
			removeDistantRepositories(metadataManager, artifactManager);			
			addGokoDeveloperRepositories(metadataManager, artifactManager);
		}else{
			addGokoDefaultRepositories(metadataManager, artifactManager);	
		}
		
		refreshUsedRepositories(metadataManager, artifactManager, monitor);
		
		traceUsedRepositories(metadataManager, artifactManager);
		
        //check if updates are available
        IStatus status = operation.resolveModal(sub.newChild(100));
        IStatus finalStatus = status; 
        if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
        	LOG.info("Nothing to update");
            finalStatus = NOTHING_TO_UPDATE;
        }
        else {        
        	finalStatus = UPDATE_AVAILABLE;
        }
        if(GokoPreference.getInstance().isDeveloperMode()){
        	removeGokoDeveloperRepositories(metadataManager, artifactManager);
        }
		if (cancelled) {
			// reset cancelled flag
			cancelled = false;
			finalStatus = Status.CANCEL_STATUS;
		}
        return finalStatus;
	}
	
	/**
	 * Retrieve the provisioning job for update
	 * @param monitor progress monitor
	 * @param sync ui sync
	 * @return ProvisioningJob
	 */
	protected ProvisioningJob getProvisioningJob(final IProgressMonitor monitor, final UISynchronize sync){
		final SubMonitor sub = SubMonitor.convert(monitor, "Retrieving provisioning job...", 10);
		final ProvisioningJob provisioningJob = operation.getProvisioningJob(sub);        	
    	if (provisioningJob == null) {
            if (operation.hasResolved()) {
                showError(sync, "Couldn't get provisioning job: " + operation.getResolutionResult());
                LOG.error( LogUtils.getMessage(operation.getResolutionResult()) );      
            } else {            	
            	showError(sync, "Couldn't resolve provisioning job");            	
            }
            cancelled = true;
    	}
    	
    	return provisioningJob;
	}
	
	public IStatus performUpdate(final IProgressMonitor monitor, final UISynchronize sync, final IWorkbench workbench) {		 
		final ProvisioningJob provisioningJob = getProvisioningJob(monitor, sync);		
		sync.syncExec(new Runnable() {            
            @Override
            public void run() {      
            	                
            	provisioningJob.addJobChangeListener(new JobChangeAdapter() {
					@Override
					public void done(IJobChangeEvent event) {
						if (event.getResult().isOK()) {
							sync.asyncExec(new Runnable() {

								@Override
								public void run() {
									GokoPreference.getInstance().setSystemClearPersistedState(true);
									boolean restart = MessageDialog.open(MessageDialog.QUESTION, null,
	                                        "Updates installed, restart?",
	                                        "Updates have been installed successfully, do you want to restart?", SWT.MODELESS);
	                                if (restart) {
	                                	workbench.restart();
	                                }
								}
							});
						}else {
							LOG.info( LogUtils.getMessage(event.getResult()));
							showError(sync, event.getResult().getMessage());
							cancelled = true;
						}
					}
            	});
            	
            	// since we switched to the UI thread for interacting with the user
            	// we need to schedule the provisioning thread, otherwise it would
            	// be executed also in the UI thread and not in a background thread
            	provisioningJob.setUser(true); 
            	provisioningJob.schedule(); 
            }
        });

		if (cancelled) {
			// reset cancelled flag
			cancelled = false;
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}
	    
    private void showError(UISynchronize sync, final String message) {
        // as the provision needs to be executed in a background thread
        // we need to ensure that the message dialog is executed in 
        // the UI thread
        sync.syncExec(new Runnable() {
            
            @Override
            public void run() {
                MessageDialog.openError(null, "Error", message);
            }
        });
    }
    
    
    /**
     * Traces the list of known repositories
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void traceUsedRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager){
		URI[] lstMetadataRepositories = metadataManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);		
		URI[] lstArtifactRepositories = artifactManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
		
		// Logging known metadata repositories
		if(lstMetadataRepositories != null && lstMetadataRepositories.length > 0){
			LOG.info("Checking updates from the following metadata repositories :");
			for (URI uri : lstMetadataRepositories) {
				LOG.info("  - "+uri.toString());				
			}			
		}
		// Logging known artifact repositories
		if(lstArtifactRepositories != null && lstArtifactRepositories.length > 0){
			LOG.info("Checking updates from the following artifact repositories :");
			for (URI uri : lstArtifactRepositories) {
				LOG.info("  - "+uri.toString());				
			}			
		}
    }
    
    /**
     * Traces the list of known repositories
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void refreshUsedRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager, IProgressMonitor monitor){
		URI[] lstMetadataRepositories = metadataManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);		
		URI[] lstArtifactRepositories = artifactManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
		
		// Logging known metadata repositories
		if(lstMetadataRepositories != null && lstMetadataRepositories.length > 0){
			LOG.info("Refreshing metadata repositories : ");
			for (URI uri : lstMetadataRepositories) {						
				LOG.info("  - "+uri.toString());
				try {
					metadataManager.loadRepository(uri, monitor);
					metadataManager.refreshRepository(uri, monitor);
				} catch (ProvisionException | OperationCanceledException e) {
					LOG.error(e);
				}
						
			}			
		}
		// Logging known artifact repositories
		if(lstArtifactRepositories != null && lstArtifactRepositories.length > 0){
			LOG.info("Refreshing artifact repositories : ");
			for (URI uri : lstArtifactRepositories) {
				LOG.info("  - "+uri.toString());
				try {
					metadataManager.loadRepository(uri, monitor);
					artifactManager.refreshRepository(uri, monitor);
				} catch (ProvisionException | OperationCanceledException e) {
					LOG.error(e);
				}
			}			
		}
    }
    
    /**
     * Adds Goko default repository if not found in the known repositories list 
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void addGokoDefaultRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager){
    	URI[] lstMetadataRepositories = metadataManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);		
		URI[] lstArtifactRepositories = artifactManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
		
		boolean metadataRepositoryFound = false;
		boolean artifactRepositoryFound = false;
		// Looking through known metadata repositories
		if(lstMetadataRepositories != null && lstMetadataRepositories.length > 0){			
			for (URI uri : lstMetadataRepositories) {
				if(StringUtils.contains(uri.toString(), UPDATE_SITE_URL)){
					metadataRepositoryFound = true;
					break;
				}				
			}			
		}
		// Looking through known artifact repositories
		if(lstArtifactRepositories != null && lstArtifactRepositories.length > 0){			
			for (URI uri : lstArtifactRepositories) {
				if(StringUtils.contains(uri.toString(), UPDATE_SITE_URL)){
					artifactRepositoryFound = true;
					break;
				}				
			}			
		}

		if(!metadataRepositoryFound){
			LOG.info("Adding Goko default repository ("+UPDATE_SITE_URL+") to metadata repositories.");
			try {
				metadataManager.addRepository(new URI(UPDATE_SITE_URL));
			} catch (URISyntaxException e) {
				LOG.error(e);
			}
		}
		
		if(!artifactRepositoryFound){
			LOG.info("Adding Goko default repository ("+UPDATE_SITE_URL+") to artifact repositories.");
			try {
				artifactManager.addRepository(new URI(UPDATE_SITE_URL));
			} catch (URISyntaxException e) {
				LOG.error(e);
			}
		}
    }
    
    /**
     * Adds Goko developer's mode repository  
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void addGokoDeveloperRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager){
		try {
			metadataManager.addRepository(new URI(DEV_UPDATE_SITE_URL));
			metadataManager.addRepository(new URI("file:///G:/Git/Goko/org.goko.build.product/target/repository"));
			LOG.info("Adding Goko dev repository ("+DEV_UPDATE_SITE_URL+") to metadata repositories.");
			
			artifactManager.addRepository(new URI(DEV_UPDATE_SITE_URL));
			artifactManager.addRepository(new URI("file:///G:/Git/Goko/org.goko.build.product/target/repository"));
			LOG.info("Adding Goko dev repository ("+DEV_UPDATE_SITE_URL+") to artifact repositories.");
			
		} catch (URISyntaxException e) {
			LOG.error(e);
		}
    }
    
    /**
     * Removes any update site that is not "dev"
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void removeDistantRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager){
		URI[] lstMetadataRepositories = metadataManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_NON_LOCAL);		
		URI[] lstArtifactRepositories = artifactManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_NON_LOCAL);
		
		// Logging known metadata repositories
		if(lstMetadataRepositories != null && lstMetadataRepositories.length > 0){			
			LOG.info("Removing distant updates site from the following metadata repositories :");
			for (URI uri : lstMetadataRepositories) {
				metadataManager.removeRepository(uri);
				
				LOG.info("  - Removing  "+uri.toString());				
			}			
		}
		// Logging known artifact repositories
		if(lstArtifactRepositories != null && lstArtifactRepositories.length > 0){
			LOG.info("Removing distant updates site from the following artifact repositories :");
			for (URI uri : lstArtifactRepositories) {
				artifactManager.removeRepository(uri);
				LOG.info("  - Removing  "+uri.toString());				
			}			
		}
    }
    
    /**
     * Removes Goko developer's mode repository  
     * @param metadataManager the used IMetadataRepositoryManager
     * @param artifactManager the used IArtifactRepositoryManager
     */
    private void removeGokoDeveloperRepositories(IMetadataRepositoryManager metadataManager, IArtifactRepositoryManager artifactManager){
		try {
			metadataManager.removeRepository(new URI(DEV_UPDATE_SITE_URL));
			artifactManager.removeRepository(new URI(DEV_UPDATE_SITE_URL));
		} catch (URISyntaxException e) {
			LOG.error(e);
		}
    }
}
