package org.goko.gcode.rs274ngcv3.ui.workspace.handler.open;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceService;

public class OpenGCodeProjectHandler {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(OpenGCodeProjectHandler.class);

	@Inject
	private IWorkspaceService workspaceService;


	@Execute
	public void executeOpenFile(Shell shell) throws GkException{
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open Goko project...");
		dialog.setFilterNames(new String[]{"Goko projects (*.goko) "});
		dialog.setFilterExtensions(new String[]{"*.goko"});
		String filePath = dialog.open();

		if(StringUtils.isNotEmpty(filePath)){
			workspaceService.loadProject(new File(filePath));
		}
	}
}