package org.goko.common.handlers;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * Handler for project save
 * @author Psyko
 */
public class SaveProjectHandler {

	@CanExecute
	public boolean canExecute(IWorkspaceService workspaceService) throws GkException{
		GkProject project = workspaceService.getProject();
		return project != null && project.isDirty();
	}

	@Execute
	public boolean saveProject(Shell shell, IWorkspaceService workspaceService) throws GkException{
		boolean saveDone = false;
		GkProject project = workspaceService.getProject();
		String filePath = project.getFilepath();

		if(StringUtils.isEmpty(filePath)){
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setText("Save Goko project...");
			dialog.setFilterNames(new String[]{"Goko projects (*.goko) "});
			dialog.setFilterExtensions(new String[]{"*.goko"});
			filePath = dialog.open();
		}
		final String finalFilePath = filePath;
		if(StringUtils.isNotEmpty(filePath)){
//			Job saveJob = new Job("Saving project"){
//				/** (inheritDoc)
//				 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
//				 */
//				@Override
//				protected IStatus run(IProgressMonitor monitor) {
					workspaceService.saveProject(new File(finalFilePath), new NullProgressMonitor());
//					return Status.OK_STATUS;
//				}
//			};
			saveDone = true;
		}
		return saveDone;
	}
}
