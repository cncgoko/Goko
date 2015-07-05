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
package org.goko.serial.jssc.toolbar.control;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.log.GkLog;
import org.goko.serial.jssc.service.IJsscSerialConnectionService;
import org.goko.serial.jssc.service.JsscSerialConnection;


public class JsscSerialPortConnectionState implements IConnectionListener {
	private static final GkLog LOG = GkLog.getLogger(JsscSerialPortConnectionState.class);
	@Inject
	private IJsscSerialConnectionService jsscService;

	private Label lblConnection;
	private Label lblConnectionState;

	/**
	 * Create the combo
	 * @param parent the parent composite
	 * @throws GkException GkException
	 */
	@PostConstruct
	public void createGui(Composite parent) throws GkException {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		lblConnection = new Label(parent, SWT.NONE);
		lblConnection.setImage(ResourceManager.getPluginImage("org.goko.serial.jssc", "resources/icons/network-status-offline.png"));

		lblConnectionState = new Label(parent, SWT.NONE);
		lblConnectionState.setText("Disconnected");
		lblConnectionState.setToolTipText("");
		GridData gd_lblConnectionState = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblConnectionState.widthHint = 200;
		lblConnectionState.setLayoutData(gd_lblConnectionState);
		jsscService.addConnectionListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionListener#onConnectionEvent(org.goko.core.connection.EnumConnectionEvent)
	 */
	@Override
	public void onConnectionEvent(final EnumConnectionEvent event) throws GkException {				
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				if(event == EnumConnectionEvent.CONNECTED){					
					try {
						JsscSerialConnection currentConnection = jsscService.getCurrentConnectionInformation();					
						lblConnection.setImage(ResourceManager.getPluginImage("org.goko.serial.jssc", "resources/icons/network-status.png"));					
						lblConnectionState.setText("Connected "+currentConnection.getPortName()+" @ "+currentConnection.getBaudrate()+" baud" );
						lblConnectionState.pack();
					} catch (GkException e) {						
						LOG.error(e);
					}
				}else if(event == EnumConnectionEvent.DISCONNECTED){
					lblConnection.setImage(ResourceManager.getPluginImage("org.goko.serial.jssc", "resources/icons/network-status-offline.png"));
					lblConnectionState.setText("Disconnected");
				}
			}
		});
	}
}
