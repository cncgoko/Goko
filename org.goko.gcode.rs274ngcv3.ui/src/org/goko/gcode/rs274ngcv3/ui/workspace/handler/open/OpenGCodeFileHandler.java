package org.goko.gcode.rs274ngcv3.ui.workspace.handler.open;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.service.IGCodeService;

public class OpenGCodeFileHandler {
	@Inject
	private IRS274NGCService gCodeService;
	
	@Execute
	public void executeOpenFile(Shell shell) throws GkException{
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setText("Open GCode file...");

		String filePath = dialog.open(); 
		String[] fileNames = dialog.getFileNames();		
		
		if(fileNames != null && fileNames.length > 0){
			File parentFolder = new File(filePath).getParentFile();
			
			for (String fileName : fileNames) {
				IGCodeProvider gcodeFile = null;
				try {
					File sourceFile = new File(parentFolder, fileName);
					gcodeFile = gCodeService.parse(new FileInputStream(sourceFile));
					gcodeFile.setCode(fileName);
					gCodeService.addGCodeProvider(gcodeFile);				
				} catch (FileNotFoundException e) {
					throw new GkTechnicalException(e);
				}	
			}
			
		}
	}
}
