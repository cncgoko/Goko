package org.goko.gcode.rs274ngcv3.ui.workspace.handler.open;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.service.IWorkspaceService;

public class SaveHandler {
	@Inject
	private IWorkspaceService workspace;


	@Execute
	public void executeSaveProject(Shell shell) throws GkException{
		workspace.saveProject();
	}
}
