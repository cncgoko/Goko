/**
 * 
 */
package goko;

import java.util.Date;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.EnumUpdateCheckFrequency;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Performs an update check when the application startup process completes.
 * This class gets called by the {@link GokoLifeCycleManager}
 * 
 * @author PsyKo
 */
@Creatable
public class AutomaticUpdateCheck implements EventHandler{
	private static GkLog LOG = GkLog.getLogger(AutomaticUpdateCheck.class);	
	@Inject
	private IProvisioningAgent agent;
	@Inject
	private IProgressMonitor monitor;
	@Inject
	private UISynchronize sync;
	@Inject
	private IEclipseContext context;
		
	/** (inheritDoc)
	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		try{
			if(GokoPreference.getInstance().isCheckForUpdate()){
				EnumUpdateCheckFrequency frequency = GokoPreference.getInstance().getUpdateCheckFrequency();
				Date lastCheck = GokoPreference.getInstance().getLastUpdateCheckTimestamp();
				boolean recheck = false;
				final long msPerDay = 1000 * 60 * 60 * 24;
				if(frequency == EnumUpdateCheckFrequency.EVERY_START){
					recheck = true;
				}else if(frequency == EnumUpdateCheckFrequency.ONCE_A_DAY){
					if( (new Date().getTime() - lastCheck.getTime()) > msPerDay){
						recheck = true;	
					}					
				}else if(frequency == EnumUpdateCheckFrequency.ONCE_A_WEEK){
					if( (new Date().getTime() - lastCheck.getTime()) > 7 * msPerDay){
						recheck = true;	
					}
				}
				
				if(recheck){
					LOG.info("Checking for update...");
					GokoPreference.getInstance().setLastUpdateCheckTimestamp(new Date());
					final GokoUpdateCheckRunnable updateCheck = new GokoUpdateCheckRunnable();
					final IWorkbench workbench = context.get(IWorkbench.class);
					IStatus result = updateCheck.update(agent, monitor, sync, workbench, true);
					
					if(GokoUpdateCheckRunnable.UPDATE_AVAILABLE.equals(result)){
						sync.syncExec(new Runnable() {        	            
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
		}catch(GkException e){
			LOG.error(e);
		}
	}
}
