package org.goko.common.handlers;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
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
	public boolean canExecute(){
		return true;
	}

	@Execute
	public void saveProject(Shell shell, IWorkspaceService workspaceService) throws GkException{
		GkProject project = workspaceService.getProject();
		String filePath = project.getFilepath();

		if(StringUtils.isEmpty(filePath)){
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);
			dialog.setText("Open Goko project...");
			dialog.setFilterNames(new String[]{"Goko projects (*.goko) "});
			dialog.setFilterExtensions(new String[]{"*.goko"});
			filePath = dialog.open();
		}

		if(StringUtils.isNotEmpty(filePath)){
			workspaceService.saveProject(new File(filePath));
		}
	}
}
