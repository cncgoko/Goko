package goko.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.goko.core.log.GkLog;

import goko.GokoUpdateCheckRunnable;

public class UpdateHandler {
	private static GkLog LOG = GkLog.getLogger(UpdateHandler.class);
	boolean cancelled = false;
	
	@PostContextCreate
	public void checkUpdateOnStart(final IProvisioningAgent agent, final UISynchronize sync, final IWorkbench workbench) {
		final GokoUpdateCheckRunnable updateCheck = new GokoUpdateCheckRunnable();
		// update using a progress monitor
        IRunnableWithProgress runnable = new IRunnableWithProgress() {            
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {                
                updateCheck.update(agent, monitor, sync, workbench, true);
            }
        };
        
        try {
            new ProgressMonitorDialog(null).run(true, true, runnable);
        } catch (InvocationTargetException | InterruptedException e) {
            LOG.error(e);
        }
        
	}
	
	@Execute
	public void execute(final IProvisioningAgent agent, final UISynchronize sync, final IWorkbench workbench) {
		final GokoUpdateCheckRunnable updateCheck = new GokoUpdateCheckRunnable();
		// update using a progress monitor
        IRunnableWithProgress runnable = new IRunnableWithProgress() {            
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {                
                updateCheck.update(agent, monitor, sync, workbench, false);
            }
        };
        
        try {
            new ProgressMonitorDialog(null).run(true, true, runnable);
        } catch (InvocationTargetException | InterruptedException e) {
            LOG.error(e);
        }
	}
    
}