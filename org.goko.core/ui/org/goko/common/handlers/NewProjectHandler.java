package org.goko.common.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * Handler for new project creation
 * @author Psyko
 */
public class NewProjectHandler {
	@Inject
	private IWorkspaceService workspaceService;
	
	@CanExecute
	public boolean canExecute(){
		return true;
	}

	@Execute
	public void saveProject(IWorkspaceService workspaceService, Shell shell) throws GkException{
		boolean keepGoing = checkUnsavedModification(shell);

		if(keepGoing){
			workspaceService.createNewProject();
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
