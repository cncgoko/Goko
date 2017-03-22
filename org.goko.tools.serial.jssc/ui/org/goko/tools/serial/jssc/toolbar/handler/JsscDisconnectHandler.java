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
package org.goko.tools.serial.jssc.toolbar.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.serial.ISerialConnectionService;

/**
 * Handler used to disconnect to the Serial port
 *
 * @author PsyKo
 *
 */
public class JsscDisconnectHandler {
	/** In place connection service */
	@Inject
	ISerialConnectionService connectionService;
	/** Internal - In place connection service */
	@Inject
	IEventBroker eventBroker;


	/**
	 * Disconnect
	 * @throws GkException GkException
	 */
	@Execute
	public void execute() throws GkException{
		try{
			connectionService.disconnect();
		}finally{
			eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
		}
	}

	/**
	 * Check if the disconnect action can be executed
	 * @return true or false
	 * @throws GkException GkException
	 */
	@CanExecute
	public boolean canExecute() throws GkException{
		return connectionService.isConnected();
	}
}
