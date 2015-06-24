package goko.handlers;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkTechnicalException;

// Require-Bundle: org.eclipse.equinox.p2.core|engine|operation|metadata.repository
// Feature: org.eclipse.equinox.p2.core.feature
//
// !!! Do not run from within IDE. Update only works in an exported product !!!
//
public class UpdateHandler {
	ProgressMonitorDialog progressDialog;
	
	@Execute
	public void execute(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync, final IWorkbench workbench, final Logger logger) {
		if(progressDialog == null){
			progressDialog = new ProgressMonitorDialog(shell);  
		}
			    
		try {
			progressDialog.run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {					
					checkForUpdates(agent, shell, sync, workbench, monitor,logger);					
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
//		Job job = new Job("Update Job") {
//			@Override
//			protected IStatus run(final IProgressMonitor monitor) {
//				return checkForUpdates(agent, shell, sync, workbench, monitor,logger);
//			}
//		};	
//		
//		job.schedule();
	}

	private IStatus checkForUpdates(final IProvisioningAgent agent, final Shell shell, final UISynchronize sync, final IWorkbench workbench, IProgressMonitor monitor, Logger logger) {

		/* 1. configure update operation */
		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);
		//configureUpdate(operation, logger);

		/* 2. Check for updates */
		IProvisioningAgent provisioningAgent = session.getProvisioningAgent();
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) provisioningAgent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		
		
		try {
			URI[] lstRepo = manager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_NON_LOCAL);
			if(lstRepo != null && lstRepo.length > 0){				
				SubMonitor subMonitor = SubMonitor.convert(monitor, "Updating repository...", lstRepo.length + 1);
				subMonitor.worked(1);
				for (URI uri : lstRepo) {
					manager.refreshRepository(uri, new NullProgressMonitor());
					subMonitor.worked(1);					
				}				
				subMonitor.done();
			}
		} catch (ProvisionException | OperationCanceledException e) {
			logger.error(e);
			return Status.CANCEL_STATUS;			
		}		

		// run check if updates are available (causing I/O)
		final IStatus status = operation.resolveModal(monitor);
		
		// Failed to find updates (inform user and exit)
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			showNoUpdateMessage(shell, sync);
			return Status.CANCEL_STATUS;
		}

		/* 3. run installation */		
		final ProvisioningJob provisioningJob = operation.getProvisioningJob(null);

		// updates cannot run from within Eclipse IDE!!!
		if (provisioningJob == null) {
			logger.error("Maybe you are trying to update from the Eclipse IDE? This won't work!!!");
			return Status.CANCEL_STATUS;
		}
		sync.syncExec(new Runnable() {

			@Override
			public void run() {
				boolean result = MessageDialog.openQuestion(shell, "Update available","An update is available. Would you like to install it now ? (Restart will be required)");
				
				if(result){
					configureProvisioningJob(provisioningJob, shell, sync, workbench);
					provisioningJob.schedule();
				}
			}
		});
		
		return Status.OK_STATUS;

	}

	private void configureProvisioningJob(ProvisioningJob provisioningJob,final Shell shell, final UISynchronize sync, final IWorkbench workbench) {

		// Register a job change listener to track
		// installation progress and notify user upon success
		provisioningJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							boolean restart = MessageDialog
									.openQuestion(shell, "Updates installed?", "Updates have been installed successfully. Do you want to restart now ?");
							if (restart) {
								workbench.restart();
							}
						}
					});

				}
				super.done(event);
			}
		});

	}

	private void showNoUpdateMessage(final Shell parent, final UISynchronize sync) {
		sync.syncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openInformation(parent, "No update","Your version of Goko is up to date.");
			}
		});
	}
}
