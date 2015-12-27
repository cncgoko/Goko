/**
 *
 */
package goko;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.internal.TargetBoardTracker;

import goko.dialog.GokoProgressDialog;

/**
 * Life cycle manager
 *
 * @author PsyKo
 *
 */
public class GokoLifeCycleManager {

	/**
	 * Post context creation method
	 * @param eventBroker the event broker
	 * @param context the eclipse context
	 * @throws GkException GkException
	 */
	@PostContextCreate
	public void postContextCreate(IEventBroker eventBroker, final IEclipseContext context) throws GkException {

		final GokoProgressDialog dialog = ContextInjectionFactory.make(GokoProgressDialog.class, context);
		dialog.open();
		dialog.getShell().setVisible(false);
		Job.getJobManager().setProgressProvider(new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return dialog.addJob(job);
			}
		});
		// Create target board tracker
		TargetBoardTracker tracker = ContextInjectionFactory.make(TargetBoardTracker.class, context);
		tracker.checkTargetBoardDefined(context);
		// Create auto update check
		AutomaticUpdateCheck updater = ContextInjectionFactory.make(AutomaticUpdateCheck.class, context);
		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, updater);	}

	/**
	 * Sets the JFace dialog default image to the icon of the main shell
	 * @param shell the active shell
	 */
	@Inject
	@Optional
	public void updateIcons(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		if(shell != null){
			Dialog.setDefaultImage(shell.getImage());
		}
	}
}
