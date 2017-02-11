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
package org.goko.controller.g2core.handlers;

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
import org.goko.common.dialog.GkDialog;
import org.goko.controller.g2core.configuration.G2CoreCommunicationPage;
import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.configuration.G2CoreConfigurationAxisMainPage;
import org.goko.controller.g2core.configuration.G2CoreConfigurationAxisPage;
import org.goko.controller.g2core.configuration.G2CoreConfigurationDevicePage;
import org.goko.controller.g2core.configuration.G2CoreConfigurationMotorMappingPage;
import org.goko.controller.g2core.configuration.G2CoreConfigurationMotorPage;
import org.goko.controller.g2core.configuration.G2CoreGCodeDefaultPage;
import org.goko.controller.g2core.configuration.G2CoreMachiningParametersPage;
import org.goko.controller.g2core.configuration.G2CorePwmChannelPage;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.g2core.controller.G2CoreControllerService;
import org.goko.controller.g2core.controller.IG2CoreControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;

/**
 * Handler for displaying the TinyG configuration panel
 * @author PsyKo
 *
 */
public class G2CoreConfigurationOpenHandler {
	private static final GkLog LOG = GkLog.getLogger(G2CoreConfigurationOpenHandler.class);
	
	@CanExecute
	public boolean canExecute(IG2CoreControllerService controllerService, IConnectionService connectionService){
		try {
			return  connectionService.isConnected()
					&& StringUtils.equals(controllerService.getServiceId(), G2CoreControllerService.SERVICE_ID);
		} catch (GkException e) {
			LOG.error(e);
			return false;
		}
	}
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, IG2CoreControllerService service, IEclipseContext context) throws GkException {		
			//	continuer l'ui de conf G2Core
		PreferenceManager manager = new PreferenceManager('/');
		G2CoreConfiguration cfg = service.getConfiguration();		
		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.1.device", 		new G2CoreConfigurationDevicePage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.2.communication", 	new G2CoreCommunicationPage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.3.machining" , 	new G2CoreMachiningParametersPage(cfg)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.4.gcode" , 		new G2CoreGCodeDefaultPage(cfg)));

		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.5.motorMapping" , 	new G2CoreConfigurationMotorMappingPage(cfg)));
		manager.addTo("org.goko.controller.g2core.5.motorMapping", new PreferenceNode("org.goko.controller.g2core.5.a.motor1" , 		new G2CoreConfigurationMotorPage(cfg, "Motor 1", G2Core.Configuration.Groups.MOTOR_1)));
		manager.addTo("org.goko.controller.g2core.5.motorMapping", new PreferenceNode("org.goko.controller.g2core.5.b.motor2" , 		new G2CoreConfigurationMotorPage(cfg, "Motor 2", G2Core.Configuration.Groups.MOTOR_2)));
		manager.addTo("org.goko.controller.g2core.5.motorMapping", new PreferenceNode("org.goko.controller.g2core.5.c.motor3" , 		new G2CoreConfigurationMotorPage(cfg, "Motor 3", G2Core.Configuration.Groups.MOTOR_3)));
		manager.addTo("org.goko.controller.g2core.5.motorMapping", new PreferenceNode("org.goko.controller.g2core.5.d.motor4" , 		new G2CoreConfigurationMotorPage(cfg, "Motor 4", G2Core.Configuration.Groups.MOTOR_4)));
		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.6.axis" , 	new G2CoreConfigurationAxisMainPage(cfg)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.a.xaxis" , 		new G2CoreConfigurationAxisPage(cfg, "X Axis", G2Core.Configuration.Groups.X_AXIS)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.b.yaxis" , 		new G2CoreConfigurationAxisPage(cfg, "Y Axis", G2Core.Configuration.Groups.Y_AXIS)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.c.zaxis" , 		new G2CoreConfigurationAxisPage(cfg, "Z Axis", G2Core.Configuration.Groups.Z_AXIS)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.d.aaxis" , 		new G2CoreConfigurationAxisPage(cfg, "A Axis", G2Core.Configuration.Groups.A_AXIS)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.e.baxis" , 		new G2CoreConfigurationAxisPage(cfg, "B Axis", G2Core.Configuration.Groups.B_AXIS)));
		manager.addTo("org.goko.controller.g2core.6.axis", new PreferenceNode("org.goko.controller.g2core.6.f.caxis" , 		new G2CoreConfigurationAxisPage(cfg, "C Axis", G2Core.Configuration.Groups.C_AXIS)));

		manager.addToRoot(new PreferenceNode("org.goko.controller.g2core.7.pwm1" , 	new G2CorePwmChannelPage(cfg, "PWM Channel", G2Core.Configuration.Groups.PWM_CHANNEL_1)));

		
		PreferenceDialog dialog = new PreferenceDialog(shell, manager);
		dialog.create();		
		dialog.getTreeViewer().expandAll();
		int result = dialog.open();
		
		if(result == Dialog.OK){
			try{				
				service.applyConfiguration(cfg);
			}catch(GkFunctionalException e){
				LOG.log(e);
				GkDialog.openDialog(shell, e);				
			}
		}
		
	}

}
