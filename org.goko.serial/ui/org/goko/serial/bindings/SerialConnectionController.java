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
package org.goko.serial.bindings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.serial.SerialConnectionService;
import org.goko.serial.bean.SerialConnectionParameter;

public class SerialConnectionController extends AbstractController<SerialConnectionBindings> implements PropertyChangeListener {
	/** Connection service */
	@Inject
	IConnectionService service;

	public SerialConnectionController(SerialConnectionBindings binding) {
		super(binding);
		binding.addPropertyChangeListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		// TODO Auto-generated method stub

	}
	/**
	 * Connect
	 */
	public void connect(){
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(SerialConnectionParameter.PORTNAME.toString(), getDataModel().getSerialPort().getValue());
			param.put(SerialConnectionParameter.BAUDRATE.toString(), getDataModel().getBaudrate().getValue()  );

			getSerialConnectionService().connect(param);

			getDataModel().setConnected(true);
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	public void disconnect(){
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param .put(SerialConnectionParameter.PORTNAME.toString(),  getDataModel().getSerialPort().getValue());
			param.put(SerialConnectionParameter.BAUDRATE.toString(),  Integer.valueOf(getDataModel().getBaudrate().getValue()));

			getSerialConnectionService().disconnect(param);

			getDataModel().setConnected(false);
		} catch (GkException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Update the list of available serial ports
	 */
	public void refreshSerialPortList(){
		try {
			List<String> lstPort = getSerialConnectionService().getAvailableSerialPorts();
			List<LabeledValue<String>> lstPortLabelledValue = new ArrayList<LabeledValue<String>>();
			for(String port : lstPort){
				lstPortLabelledValue.add( new LabeledValue<String>(port, StringUtils.defaultString(port).toUpperCase()) );
			}

			getDataModel().setChoiceSerialPort(lstPortLabelledValue);
			// Handle auto selection
			getDataModel().setSerialPort( GkUiUtils.refreshSelectedItem(lstPortLabelledValue, getDataModel().getSerialPort()) );
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the service
	 */
	public SerialConnectionService getSerialConnectionService() {
		return (SerialConnectionService) service;
	}

	/**
	 * @param service the service to set
	 */
	public void setSerialConnectionService(SerialConnectionService service) {
		this.service = service;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(StringUtils.equals( evt.getPropertyName(), "serialPort")){
			getDataModel().setConnectionAllowed(false);
			if(getDataModel().getSerialPort() != null
					&& !getDataModel().isConnected()){
				getDataModel().setConnectionAllowed(true);
			}
		}

	}

}
