/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v11.internal;

import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.goko.common.dialog.GkDialog;
import org.goko.controller.grbl.v11.IGrbl11ControllerService;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;
import org.goko.controller.grbl.v11.configuration.GrblConfigurationDialog;
import org.goko.controller.grbl.v11.configuration.GrblPollingPreferencePage;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;


/**
 * Handler for displaying the Grbl configuration panel
 * @author PsyKo
 *
 */
public class GrblConfigurationOpenHandler {
	private static final GkLog LOG = GkLog.getLogger(GrblConfigurationOpenHandler.class);
	@CanExecute
	public boolean canExecute(IGrbl11ControllerService controllerService, IConnectionService connectionService){		
		try {
			return !ObjectUtils.equals(GrblMachineState.UNDEFINED, controllerService.getState());
		} catch (GkException e) {			
			e.printStackTrace();
		}
		return false;		
	}
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IGrbl11ControllerService service, IEclipseContext context) throws GkException {					
		PreferenceManager manager = new PreferenceManager('/');
		GrblConfiguration cfg = service.getConfiguration();		
		manager.addToRoot(new PreferenceNode("org.goko.controller.grbl.1.device", new GrblConfigurationDialog(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.grbl.2.polling", new GrblPollingPreferencePage(cfg)));
		
		PreferenceDialog dialog = new PreferenceDialog(shell, manager);
		
		int result = dialog.open();
		
		if(result == Dialog.OK){
			try{				
				service.setConfiguration(cfg);
			}catch(GkFunctionalException e){
				LOG.log(e);
				GkDialog.openDialog(shell, e);				
			}
		}			
		
	}

}
