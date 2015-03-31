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
package org.goko.serial.jssc.toolbar.handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.goko.core.common.exception.GkException;
import org.goko.serial.jssc.internal.JsscSerialActivator;
import org.goko.serial.jssc.service.IJsscSerialConnectionService;
import org.goko.serial.jssc.service.JsscParameter;

/**
 * Handler used to connect to the Serial port
 *
 * @author PsyKo
 *
 */
public class JsscConnectHandler {
	/** In place connection service */
	@Inject
	IJsscSerialConnectionService connectionService;
	/** Internal - In place connection service */
	@Inject
	IEventBroker eventBroker;

	/**
	 * Connect to the Serial port
	 * @throws GkException GkException
	 */
	@Execute
	public void execute() throws GkException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(JsscParameter.PORTNAME.name(), JsscSerialActivator.getPreferenceStore().getString(JsscParameter.PORTNAME.name()));
		params.put(JsscParameter.BAUDRATE.name(), JsscSerialActivator.getPreferenceStore().getInt(JsscParameter.BAUDRATE.name()));//SerialPort.BAUDRATE_115200);
		params.put(JsscParameter.PARITY.name(), JsscSerialActivator.getPreferenceStore().getInt(JsscParameter.PARITY.name()));//SerialPort.PARITY_NONE);
		params.put(JsscParameter.STOPBITS.name(), JsscSerialActivator.getPreferenceStore().getInt(JsscParameter.STOPBITS.name()));//SerialPort.STOPBITS_1);
		params.put(JsscParameter.DATABITS.name(), JsscSerialActivator.getPreferenceStore().getInt(JsscParameter.DATABITS.name()));//SerialPort.DATABITS_8);
		params.put(JsscParameter.RCSCTS.name(), JsscSerialActivator.getPreferenceStore().getString(JsscParameter.RCSCTS.name()));//SerialPort.DATABITS_8);
		params.put(JsscParameter.XONXOFF.name(), JsscSerialActivator.getPreferenceStore().getString(JsscParameter.XONXOFF.name()));//SerialPort.DATABITS_8);

		connectionService.connect(params);

		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}

	/**
	 * Check if connection to the Serial port is available
	 * @throws GkException GkException
	 */
	@CanExecute
	public boolean canExecute() throws GkException{
		return !connectionService.isConnected() && CollectionUtils.isNotEmpty(connectionService.getAvailableSerialPort());
	}

}
