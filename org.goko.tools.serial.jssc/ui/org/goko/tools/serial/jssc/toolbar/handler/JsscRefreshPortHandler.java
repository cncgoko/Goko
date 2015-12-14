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
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.tools.serial.jssc.JsscUiEvent;

/**
 * Handler used to connect to the Serial port
 *
 * @author PsyKo
 *
 */
public class JsscRefreshPortHandler {
	/** In place connection service */
	@Inject
	IConnectionService connectionService;
	/** Internal - In place connection service */
	@Inject
	IEventBroker eventBroker;

	/**
	 * Connect to the Serial port
	 * @throws GkException GkException
	 */
	@Execute
	public void execute() throws GkException{
		eventBroker.send(JsscUiEvent.JSSC_REFRESH_COM_PORT_LIST, true);
	}

	/**
	 * Check if connection to the Serial port is available
	 * @throws GkException GkException
	 */
	@CanExecute
	public boolean canExecute() throws GkException{
		boolean bool = !connectionService.isConnected();
		return bool;
	}

}
