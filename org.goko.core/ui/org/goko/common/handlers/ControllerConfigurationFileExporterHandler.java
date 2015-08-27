/**
 * 
 */
package org.goko.common.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.log.GkLog;

/**
 * Handler to export controller configuration
 * 
 * @author PsyKo
 */
public class ControllerConfigurationFileExporterHandler {
	private static final GkLog LOG = GkLog.getLogger(ControllerConfigurationFileExporterHandler.class);
		
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IControllerConfigurationFileExporter exporter) throws GkException {
		FileDialog dialog = new FileDialog(shell);
		dialog.setText("Open configuration file...");
		dialog.setFilterExtensions(new String[]{"*."+exporter.getFileExtension()});
		String filepath = dialog.open();
		if(StringUtils.isBlank(filepath)){
			return;
		}	
		if(StringUtils.startsWith(exporter.getFileExtension(), ".")){
			filepath += exporter.getFileExtension();
		}else{
			filepath += "."+exporter.getFileExtension();
		}
	
		try {
			File file = new File(filepath);
			if(!file.exists()){				
				file.createNewFile();				
			}		
			exporter.exportTo(new FileOutputStream(file));
			MessageDialog.openInformation(shell, "Success", "Configuration successfully exported.");
		} catch (IOException e) {
			LOG.error(e);
			throw new GkTechnicalException(e);
		}
	}

	@CanExecute
	public boolean canExecute(IControllerConfigurationFileExporter exporter) throws GkException{
		return exporter.canExport();
	}
}
