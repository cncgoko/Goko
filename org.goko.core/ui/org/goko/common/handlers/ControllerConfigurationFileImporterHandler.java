/**
 * 
 */
package org.goko.common.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.log.GkLog;

/**
 * Handler to import controller configuration
 * 
 * @author PsyKo
 */
public class ControllerConfigurationFileImporterHandler {
	private static final GkLog LOG = GkLog.getLogger(ControllerConfigurationFileImporterHandler.class);
		
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IControllerConfigurationFileImporter importer) throws GkException {
		FileDialog dialog = new FileDialog(shell);
		dialog.setText("Open configuration file...");
		dialog.setFilterExtensions(new String[]{"*."+importer.getFileExtension()});
		String filepath = dialog.open();
		if(StringUtils.isBlank(filepath)){
			return;
		}
		
		File file = new File(filepath);		
		try {
			importer.importFrom(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			LOG.error(e);
			throw new GkTechnicalException(e);
		}
	}

	@CanExecute
	public boolean canExecute(IControllerConfigurationFileImporter importer) throws GkException{
		return importer.canImport();
	}
}
