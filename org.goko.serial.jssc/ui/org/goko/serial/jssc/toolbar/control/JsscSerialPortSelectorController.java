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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionListener;
import org.goko.serial.jssc.internal.JsscSerialActivator;
import org.goko.serial.jssc.service.IJsscSerialConnectionService;
import org.goko.serial.jssc.service.JsscParameter;

public class JsscSerialPortSelectorController extends AbstractController<JsscSerialPortSelectorModel> implements IConnectionListener {
	@Inject
	private IJsscSerialConnectionService jsscService;
	@Inject
	private IEventBroker eventBroker;
	public JsscSerialPortSelectorController() {
		super(new JsscSerialPortSelectorModel());
	}

	@Override
	public void initialize() throws GkException {
		refreshSerialPortList();
		String port = JsscSerialActivator.getPreferenceStore().getString(JsscParameter.PORTNAME.name());
		LabeledValue<String> preferredPort = GkUiUtils.getLabelledValueByKey(port, getDataModel().getAvailableSerialPorts());
		if(preferredPort != null){
			getDataModel().setSelectedSerialPort(preferredPort);
		}

		jsscService.addConnectionListener(this);
	}

	public void onSerialPortChange() {
		if(getDataModel().getSelectedSerialPort() != null){
			JsscSerialActivator.getPreferenceStore().setValue(JsscParameter.PORTNAME.name(), getDataModel().getSelectedSerialPort().getValue());
		}else{
			JsscSerialActivator.getPreferenceStore().setValue(JsscParameter.PORTNAME.name(), StringUtils.EMPTY);
		}

		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}

	public void refreshSerialPortList() {
		try {
			List<LabeledValue<String>> lstPorts = createInput();
			getDataModel().setAvailableSerialPorts(lstPorts);
			if(lstPorts.size() == 1){
				getDataModel().setSelectedSerialPort( lstPorts.get(0));
			}
			onSerialPortChange();
		} catch (GkException e) {
			notifyException(e);
		}
	}

	private List<LabeledValue<String>> createInput() throws GkException {
		List<LabeledValue<String>> lstValues = new ArrayList<LabeledValue<String>>();
		for (String port : jsscService.getAvailableSerialPort()) {
			lstValues.add( new LabeledValue<String>(port,  port));
		}
		return lstValues;
	}

	@Override
	public void onConnectionEvent(EnumConnectionEvent event) throws GkException {
		getDataModel().setServiceConnected( jsscService.isConnected() );
	}

}
