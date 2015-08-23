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
package org.goko.controller.tinyg.handlers;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Shell;
import org.goko.controller.tinyg.configuration.TinyGConfigurationAxisMainPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationAxisPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationCommunicationPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationDevicePage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationGCodeDefaultPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationMotionPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationMotorMappingPage;
import org.goko.controller.tinyg.configuration.TinyGConfigurationMotorPage;
import org.goko.controller.tinyg.controller.ITinygControllerService;
import org.goko.controller.tinyg.controller.TinyGControllerService;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerService;
import org.goko.core.log.GkLog;

/**
 * Handler for displaying the TinyG configuration panel
 * @author PsyKo
 *
 */
public class TinyGConfigurationOpenHandler {
	private static final GkLog LOG = GkLog.getLogger(TinyGConfigurationOpenHandler.class);
	@CanExecute
	public boolean canExecute(IControllerService controllerService, IConnectionService connectionService){
		try {
			return  connectionService.isConnected()
					&& StringUtils.equals(controllerService.getServiceId(), TinyGControllerService.SERVICE_ID);
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
	}
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, ITinygControllerService service, IEclipseContext context) throws GkException {		
		PreferenceManager manager = new PreferenceManager('/');
		TinyGConfiguration cfg = service.getConfiguration();		
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.1.device", 		new TinyGConfigurationDevicePage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.2.communication", 	new TinyGConfigurationCommunicationPage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.3.defaultgcode" , 	new TinyGConfigurationGCodeDefaultPage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.4.motion" , 		new TinyGConfigurationMotionPage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.5.motorMapping" , 	new TinyGConfigurationMotorMappingPage(cfg)));
		manager.addTo("org.goko.controller.tinyg.5.motorMapping", new PreferenceNode("org.goko.controller.tinyg.5.a.motor1" , 		new TinyGConfigurationMotorPage(cfg, "Motor 1", TinyGConfiguration.MOTOR_1_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.5.motorMapping", new PreferenceNode("org.goko.controller.tinyg.5.b.motor2" , 		new TinyGConfigurationMotorPage(cfg, "Motor 2", TinyGConfiguration.MOTOR_2_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.5.motorMapping", new PreferenceNode("org.goko.controller.tinyg.5.c.motor3" , 		new TinyGConfigurationMotorPage(cfg, "Motor 3", TinyGConfiguration.MOTOR_3_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.5.motorMapping", new PreferenceNode("org.goko.controller.tinyg.5.d.motor4" , 		new TinyGConfigurationMotorPage(cfg, "Motor 4", TinyGConfiguration.MOTOR_4_SETTINGS)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.tinyg.6.axis" , 	new TinyGConfigurationAxisMainPage(cfg)));
		manager.addTo("org.goko.controller.tinyg.6.axis", new PreferenceNode("org.goko.controller.tinyg.6.a.xaxis" , 		new TinyGConfigurationAxisPage(cfg, "X Axis", TinyGConfiguration.X_AXIS_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.6.axis", new PreferenceNode("org.goko.controller.tinyg.6.b.yaxis" , 		new TinyGConfigurationAxisPage(cfg, "Y Axis", TinyGConfiguration.Y_AXIS_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.6.axis", new PreferenceNode("org.goko.controller.tinyg.6.c.zaxis" , 		new TinyGConfigurationAxisPage(cfg, "Z Axis", TinyGConfiguration.Z_AXIS_SETTINGS)));
		manager.addTo("org.goko.controller.tinyg.6.axis", new PreferenceNode("org.goko.controller.tinyg.6.d.aaxis" , 		new TinyGConfigurationAxisPage(cfg, "A Axis", TinyGConfiguration.A_AXIS_SETTINGS)));
		
		PreferenceDialog dialog = new PreferenceDialog(shell, manager);		
		int result = dialog.open();
		
		if(result == Dialog.OK){
			//le apply n'est pas géré. Voir comment il est géré dans les GkFieldEditorPreferencePage
			service.updateConfiguration(cfg);
		}
		
	}

}
