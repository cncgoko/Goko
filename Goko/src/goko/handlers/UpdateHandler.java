package goko.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.goko.core.log.GkLog;

import goko.GokoUpdateCheckRunnable;

public class UpdateHandler {
	private static GkLog LOG = GkLog.getLogger(UpdateHandler.class);
	boolean cancelled = false;
	
	@Execute
	public void execute(final IProvisioningAgent agent, final UISynchronize sync, final IWorkbench workbench) {
		final GokoUpdateCheckRunnable updateCheck = new GokoUpdateCheckRunnable();
		
        Job updateJob = new Job("Checking for updates"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {				 
				return updateCheck.update(agent, monitor, sync, workbench, false);
			}        	
        };
        
        updateJob.addJobChangeListener(new JobChangeAdapter(){        	
        	/** (inheritDoc)
        	 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
        	 */
        	@Override
        	public void done(IJobChangeEvent event) {        		
        		super.done(event);
        		IStatus result = event.getResult();
        		if(result != null){
        			if(result.equals(GokoUpdateCheckRunnable.NOTHING_TO_UPDATE)){
	        			// Asynchronous execution required to allow the job progress to close
	        	        sync.asyncExec(new Runnable() {        	            
	        	            /** (inheritDoc) @see java.lang.Runnable#run() */
	        	            @Override
	        	            public void run() {
	        	                MessageDialog.openInformation(null, "Information", "Nothing to update");
	        	            }
	        	        });
        			}else if(result.equals(GokoUpdateCheckRunnable.UPDATE_AVAILABLE)){
        				sync.asyncExec(new Runnable() {        	            
	        	            /** (inheritDoc) @see java.lang.Runnable#run() */
	        	            @Override
	        	            public void run() {
		        				boolean performUpdate = MessageDialog.openQuestion(null,
		                                "Updates available",
		                                "There are updates available. Do you want to install them now?");
		        				if(performUpdate){
				        			// Asynchronous execution required to allow the job progress to close
			        				Job applyUpdateJob = new Job("Checking for updates"){
			        					@Override
			        					protected IStatus run(IProgressMonitor monitor) {				 
			        						return updateCheck.performUpdate(monitor, sync, workbench);
			        					}        	
			        		        };      
			        		        applyUpdateJob.setUser(true);
			        		        applyUpdateJob.schedule();
		        				}
	        	            }
	        	        });
        			}
        		}
        	}
        });
        updateJob.setUser(true);
        updateJob.schedule();

	}
    
}