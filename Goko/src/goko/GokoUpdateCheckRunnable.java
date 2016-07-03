package goko;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.core.log.LogUtils;

public class GokoUpdateCheckRunnable {
	private static final GkLog LOG = GkLog.getLogger(GokoUpdateCheckRunnable.class);
	private boolean cancelled;
	/** Nothing to update status */
	public static final IStatus NOTHING_TO_UPDATE = new Status(Status.OK, "Goko", 10000, "", null);
	/** Default update site location */
	private static final String UPDATE_SITE_URL = "http://update.goko.fr/";
	/** Developer mode update site location */
	private static final String DEV_UPDATE_SITE_URL = "http://update.goko.fr/dev/";
	
	public IStatus update(final IProvisioningAgent agent, final IProgressMonitor monitor, final UISynchronize sync, final IWorkbench workbench, boolean silent){		
		ProvisioningSession session = new ProvisioningSession(agent);
		// update the whole running profile, otherwise specify IUs
		UpdateOperation operation = new UpdateOperation(session);
		
		final SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent.getService(IArtifactRepositoryManager.SERVICE_NAME);
		
		traceUsedRepositories(metadataManager, artifactManager);
		
		addGokoDefaultRepositories(metadataManager, artifactManager);
		
		if(GokoPreference.getInstance().isDeveloperMode()){
			addGokoDeveloperRepositories(metadataManager, artifactManager);
		}
        //check if updates are available
        IStatus status = operation.resolveModal(sub.newChild(100));
        
        if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
        	LOG.info("Nothing to update");
            if(!silent){
            //	showMessage(sync, "Nothing to update");
            }
            return NOTHING_TO_UPDATE;
        }
        else {        
        	final ProvisioningJob provisioningJob = operation.getProvisioningJob(sub.newChild(100));        	
        	if (provisioningJob != null) {
        		performUpdate(provisioningJob, monitor, sync, workbench);
        	} else {
                if (operation.hasResolved()) {                	 
                    if(!silent){
                    	showError(sync, "Couldn't get provisioning job: " + operation.getResolutionResult());
                    }
                    LOG.error( LogUtils.getMessage(operation.getResolutionResult()) );       
                   
                }
                else {
                	if(!silent){
                		 showError(sync, "Couldn't resolve provisioning job");
                	}
                }
                cancelled = true;
        	}
        }
        if(GokoPreference.getInstance().isDeveloperMode()){
        	removeGokoDeveloperRepositories(metadataManager, artifactManager);
        }
		if (cancelled) {
			// reset cancelled flag
			cancelled = false;
			return Status.CANCEL_STATUS;
		}
        return Status.OK_STATUS;
	}
	
	public IStatus performUpdate(final ProvisioningJob provisioningJob, final IProgressMonitor monitor, final UISynchronize sync, final IWorkbench workbench) {		 
		
		sync.syncExec(new Runnable() {            
            @Override
            public void run() {      
            	
                boolean performUpdate = MessageDialog.openQuestion(null,
                        "Updates available",
                        "There are updates available. Do you want to install them now?");
                if (performUpdate) {
                	provisioningJob.addJobChangeListener(new JobChangeAdapter() {
						@Override
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								sync.syncExec(new Runnable() {

									@Override
									public void run() {
										GokoPreference.getInstance().setSystemClearPersistedState(true);
										boolean restart = MessageDialog.openQuestion(null,
		                                        "Updates installed, restart?",
		                                        "Updates have been installed successfully, do you want to restart?");
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
                	//provisioningJob.run(sub.newChild(100));
                }
                else {
                	cancelled = true;
                }
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
			artifactManager.addRepository(new URI(DEV_UPDATE_SITE_URL));
		} catch (URISyntaxException e) {
			LOG.error(e);
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
