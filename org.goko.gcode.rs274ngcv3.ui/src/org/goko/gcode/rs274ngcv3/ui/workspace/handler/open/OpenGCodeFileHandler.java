package org.goko.gcode.rs274ngcv3.ui.workspace.handler.open;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.source.ResourceLocationGCodeSource;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.IResourceLocation;
import org.goko.core.workspace.service.IWorkspaceService;

public class OpenGCodeFileHandler {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(OpenGCodeFileHandler.class);

	private static final String LAST_GCODE_PATH = "org.goko.open.gcode.lastfolder";

	@Inject
	private IRS274NGCService gCodeService;
	@Inject
	private IWorkspaceService workspaceService;

	@Execute
	public void executeOpenFile(Shell shell) {		
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setText("Open GCode file...");
		dialog.setFilterPath(getPersistedGCodeFolder());
		String filePath = dialog.open();
		String[] fileNames = dialog.getFileNames();
		
		if(fileNames != null && fileNames.length > 0){
			persistGCodeFolder(filePath);
			final File parentFolder = new File(filePath).getParentFile();

			for (final String fileName : fileNames) {
				// Start a job for each file
				Job gcodeJob = new Job("Opening "+fileName) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						IGCodeProvider gcodeFile = null;
						File sourceFile = null;
						try {
							sourceFile = new File(parentFolder, fileName);
							IResourceLocation resource = workspaceService.addResource(sourceFile.toURI());
							ResourceLocationGCodeSource gcodeSource = new ResourceLocationGCodeSource(resource);
							gcodeFile = gCodeService.parse(gcodeSource, monitor);
							gcodeFile.setCode(fileName);
							gCodeService.addGCodeProvider(gcodeFile);							
						} catch (GkException e) {
							LOG.error(e);
							return new Status(IStatus.ERROR, "org.goko.gcode.rs274ngcv3.ui", e.getMessage());
						}
						return Status.OK_STATUS;
					};
				};

				gcodeJob.setUser(true);
				gcodeJob.schedule(100);
			}

		}
	}

	private void persistGCodeFolder(String filePath) {
		GokoPreference.getInstance().setValue(LAST_GCODE_PATH, FilenameUtils.getFullPath(filePath));
		try {
			GokoPreference.getInstance().save();
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	private String getPersistedGCodeFolder() {
		return GokoPreference.getInstance().getString(LAST_GCODE_PATH);
	}
}