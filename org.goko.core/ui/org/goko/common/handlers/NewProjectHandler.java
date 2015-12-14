package org.goko.common.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * Handler for new project creation
 * @author Psyko
 */
public class NewProjectHandler {

	@CanExecute
	public boolean canExecute(){
		return true;
	}

	@Execute
	public void saveProject(IWorkspaceService workspaceService) throws GkException{

	}
}
