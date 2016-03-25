/**
 * 
 */
package org.goko.core.workspace.addon;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.handlers.SaveProjectHandler;
import org.goko.core.workspace.service.IWorkspaceService;
import org.osgi.service.event.Event;

/**
 * @author PsyKo
 * @date 24 mars 2016
 */
public class CheckSaveOnExitAddon {
	private static final GkLog LOG = GkLog.getLogger(CheckSaveOnExitAddon.class);
	@Inject
	MApplication application;
	@Inject
	private IWorkspaceService workspaceService;
	
	@Inject
	@Optional
	private void subscribeApplicationCompleted(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		registerCloseHandler();
	}
		
	public void registerCloseHandler() {
		for (MWindow window : application.getChildren()) {

			final Shell shell = (Shell) window.getWidget();
			IWindowCloseHandler handler = new IWindowCloseHandler() {
				@Override
				public boolean close(MWindow window) {
					try {
						return checkUnsavedModification(shell);
					} catch (GkException e) {
						LOG.error(e);
					}
					return false;
				}
			};
			
			// Mostly MWindow contexts are lazily created by renderers
			// therefore it does not need to be set already at this point
			if (window.getContext() != null) {
				window.getContext().set(IWindowCloseHandler.class, handler);
			}			
		}
	}
	
	private boolean checkUnsavedModification(Shell shell) throws GkException {
		GkProject project = workspaceService.getProject();
		if(project.isDirty()){

			MessageDialog saveDialog = new MessageDialog(shell,
												"Save",
												null,
												"Current project has unsaved modification. Would you like to save them now ?",
												MessageDialog.QUESTION_WITH_CANCEL,
												new String[]{
													IDialogConstants.YES_LABEL,
													IDialogConstants.NO_LABEL,
													IDialogConstants.CANCEL_LABEL},
												0
												);
			int result = saveDialog.open();
			switch(result){
			case 0: return saveProject(shell);
			case 1: return true;
			case 2: // Cancel is the default state
			default: return false;
			}
		}

		return true;
	}
	
	/**
	 * Performs the save reusing the regular handler
	 * @param shell the shell in use
	 * @return <code>true</code> if the save was completed, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	private boolean  saveProject(Shell shell) throws GkException {
		return new SaveProjectHandler().saveProject(shell, workspaceService);
	}
}
