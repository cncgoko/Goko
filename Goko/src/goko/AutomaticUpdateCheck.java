/**
 * 
 */
package goko;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
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
	IProvisioningAgent agent;
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
		if(GokoPreference.getInstance().isCheckUpdateAtStart()){
			LOG.info("Checking update at startup...");
			final GokoUpdateCheckRunnable updateCheck = new GokoUpdateCheckRunnable();
			updateCheck.update(agent, monitor, sync, context.get(IWorkbench.class), true);
		}
	}
}
