/**
 *
 */
package goko;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.swt.IEventLoopAdvisor;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.internal.TargetBoardTracker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import goko.dialog.GokoProgressDialog;
import goko.splashscreen.GokoSplashscreen;

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
	public void postContextCreate(final IEventBroker eventBroker, final IEclipseContext context, IApplicationContext appContext) throws GkException {
		final GokoSplashscreen splash = new GokoSplashscreen();
		splash.open();
		String[] args = (String[]) appContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		enableDevModeIfRequired(args);
		
		IWindowCloseHandler closeHandler = new ExitHandlerManager();
		context.set(IWindowCloseHandler.class, closeHandler);
		/*
		 * Performing model update if required
		 */
		if(GokoPreference.getInstance().isSystemClearPersistedState()){
			GokoPreference.getInstance().setSystemClearPersistedState(false);
			System.getProperties().put(IWorkbench.CLEAR_PERSISTED_STATE, "true");	
		}	
		
		/* ******************************************** */
		/*            Setting event advisor             */
		/* ******************************************** */
		GokoEventLoopAdvisor advisor = ContextInjectionFactory.make(GokoEventLoopAdvisor.class, context);
		context.set(IEventLoopAdvisor.class, advisor);
		
		final GokoProgressDialog dialog = ContextInjectionFactory.make(GokoProgressDialog.class, context);
		
		// Create target board tracker
		TargetBoardTracker tracker = ContextInjectionFactory.make(TargetBoardTracker.class, context);
		tracker.checkTargetBoardDefined(context);
		context.set(TargetBoardTracker.class, tracker);
		// Create auto update check
		AutomaticUpdateCheck updater = ContextInjectionFactory.make(AutomaticUpdateCheck.class, context);
		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, updater);

		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, new EventHandler() {

			@Override
			public void handleEvent(Event event) {
				// Activate progress dialog
				dialog.open();
				dialog.getShell().setVisible(false);
				Job.getJobManager().setProgressProvider(new ProgressProvider() {
					@Override
					public IProgressMonitor createMonitor(Job job) {
						return dialog.addJob(job);
					}
				});
				// Close splashscreen
				splash.close();
			}
		});		
	}

	private void enableDevModeIfRequired(String[] args){
		GokoPreference.getInstance().setDeveloperMode(false);
		if(args != null && args.length > 0){
			for (String key : args) {
				if(StringUtils.equals(key, "-goko.devMode=true")){
					GokoPreference.getInstance().setDeveloperMode(true);
				}
			}
		}
		GokoPreference.getInstance().setDevEnvironment(false);
		if(args != null && args.length > 0){
			for (String key : args) {
				if(StringUtils.equals(key, "-goko.devEnvironment=true")){
					GokoPreference.getInstance().setDevEnvironment(true);
				}
			}
		}
	}
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

class ExitHandlerManager implements IWindowCloseHandler{
	@Inject
	@Optional
	private MWindow window;

	/** (inheritDoc)
	 * @see org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler#close(org.eclipse.e4.ui.model.application.ui.basic.MWindow)
	 */
	@Override
	public boolean close(MWindow window) {
		// Probably not the best location for this "dispoe"....
		ResourceManager.dispose();
		return true;
	}
}
