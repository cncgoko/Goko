package org.goko.common.handlers;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.service.IWorkspaceService;

public class OpenProjectHandler {
	/** Key for the last opened folder */
	private static final String LAST_PROJECT_PATH = "org.goko.open.project.lastfolder";
	@Inject
	private IWorkspaceService workspaceService;


	@Execute
	public void executeOpenFile(Shell shell) throws GkException{
		boolean keepGoing = checkUnsavedModification(shell);

		if(keepGoing){
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			dialog.setText("Open Goko project...");
			dialog.setFilterNames(new String[]{"Goko projects (*.goko) "});
			dialog.setFilterExtensions(new String[]{"*.goko"});
			dialog.setFilterPath(getPersistedGCodeFolder());
			String filePath = dialog.open();

			if(StringUtils.isNotEmpty(filePath)){
				persistGCodeFolder(filePath);
				workspaceService.loadProject(new File(filePath));
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

	private void persistGCodeFolder(String filePath) {
		GokoPreference.getInstance().setValue(LAST_PROJECT_PATH, FilenameUtils.getFullPath(filePath));
	}

	private String getPersistedGCodeFolder() {
		return GokoPreference.getInstance().getString(LAST_PROJECT_PATH);
	}
}