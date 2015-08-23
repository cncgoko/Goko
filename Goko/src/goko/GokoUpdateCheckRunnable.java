package goko;

import java.net.URI;

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
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.goko.core.log.GkLog;
import org.goko.core.log.LogUtils;

public class GokoUpdateCheckRunnable {
	private static final GkLog LOG = GkLog.getLogger(GokoUpdateCheckRunnable.class);
	private boolean cancelled;
	
	public IStatus update(final IProvisioningAgent agent, final IProgressMonitor monitor, final UISynchronize sync, final IWorkbench workbench, boolean silent){		
		ProvisioningSession session = new ProvisioningSession(agent);
		// update the whole running profile, otherwise specify IUs
		UpdateOperation operation = new UpdateOperation(session);
		
		final SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		URI[] lstRepositories = manager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
		
		if(lstRepositories != null && lstRepositories.length > 0){
			LOG.info("Checking updates from the following repositories :");
			for (URI uri : lstRepositories) {
				LOG.info("  + "+uri.toString());	
			}
		}
        //check if updates are available
        IStatus status = operation.resolveModal(sub.newChild(100));
        
        if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
            if(!silent){
            	showMessage(sync, "Nothing to update");
            }
            return Status.CANCEL_STATUS;
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
	
	
    private void showMessage(UISynchronize sync, final String message) {
        // as the provision needs to be executed in a background thread
        // we need to ensure that the message dialog is executed in 
        // the UI thread
        sync.syncExec(new Runnable() {
            
            @Override
            public void run() {
                MessageDialog.openInformation(null, "Information", message);
            }
        });
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
}
