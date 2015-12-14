package org.goko.common.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.common.exception.GkException;
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
	public void saveProject(IWorkspaceService workspaceService) throws GkException{
		workspaceService.saveProject();
	}
}
