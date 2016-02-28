/**
 * 
 */
package goko;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.internal.workbench.swt.IEventLoopAdvisor;
import org.eclipse.swt.widgets.Display;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 * @date 22 févr. 2016
 */
public class GokoEventLoopAdvisor implements IEventLoopAdvisor {
	private static final GkLog LOG = GkLog.getLogger(GokoEventLoopAdvisor.class);
	@Inject
	private IEclipseContext appContext;
	@Inject
	protected Logger logger;

	/** (inheritDoc)
	 * @see org.eclipse.e4.ui.internal.workbench.swt.IEventLoopAdvisor#eventLoopIdle(org.eclipse.swt.widgets.Display)
	 */
	@Override
	public void eventLoopIdle(Display display) {
		display.sleep();
	}

	/** (inheritDoc)
	 * @see org.eclipse.e4.ui.internal.workbench.swt.IEventLoopAdvisor#eventLoopException(java.lang.Throwable)
	 */
	@Override
	public void eventLoopException(Throwable exception) { 
		LOG.error(exception);			
		StatusReporter statusReporter = appContext.get(StatusReporter.class);
		if (statusReporter != null) {
			statusReporter.show(StatusReporter.ERROR, "Internal Error", exception);
		} else {
			if (logger != null) {
				logger.error(exception);
			}
		}		
	}

}
