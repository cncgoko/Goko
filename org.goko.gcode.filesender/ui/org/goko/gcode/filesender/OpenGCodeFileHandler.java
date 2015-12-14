package org.goko.gcode.filesender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;

public class OpenGCodeFileHandler {
	@Inject
	private IGCodeService gCodeService;
	
	@Execute
	public void executeOpenFile(Shell shell) throws GkException{
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open GCode file...");

		String filePath = dialog.open(); 
		if(StringUtils.isNotBlank(filePath)){
			File gcodeFileInput = new File(filePath);		
			IGCodeProvider gcodeFile = null;
			try {
				gcodeFile = gCodeService.parse(new FileInputStream(gcodeFileInput));
				gcodeFile.setCode(gcodeFileInput.getName());
				gCodeService.addGCodeProvider(gcodeFile);				
			} catch (FileNotFoundException e) {
				throw new GkTechnicalException(e);
			}
		}
	}
}
