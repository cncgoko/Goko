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
package org.goko.tools.serial.jssc.toolbar.control;

import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

class JsscSerialPortSelectorModel extends AbstractModelObject{
	private List<LabeledValue<String>> availableSerialPorts;
	private LabeledValue<String> selectedSerialPort;
	private boolean serviceConnected;
	/**
	 * @return the availableSerialPorts
	 */
	public List<LabeledValue<String>> getAvailableSerialPorts() {
		return availableSerialPorts;
	}
	/**
	 * @param availableSerialPorts the availableSerialPorts to set
	 */
	public void setAvailableSerialPorts(List<LabeledValue<String>> availableSerialPorts) {
		firePropertyChange("availableSerialPorts", this.availableSerialPorts, this.availableSerialPorts = availableSerialPorts);
	}
	/**
	 * @return the selectedSerialPorts
	 */
	public LabeledValue<String> getSelectedSerialPort() {
		return selectedSerialPort;
	}
	/**
	 * @param selectedSerialPorts the selectedSerialPorts to set
	 */
	public void setSelectedSerialPort(LabeledValue<String> selectedSerialPort) {
		firePropertyChange("selectedSerialPort", this.selectedSerialPort, this.selectedSerialPort = selectedSerialPort);
	}
	/**
	 * @return the serviceConnected
	 */
	public boolean isServiceConnected() {
		return serviceConnected;
	}
	/**
	 * @return the serviceConnected
	 */
	public boolean getServiceConnected() {
		return serviceConnected;
	}
	/**
	 * @param serviceConnected the serviceConnected to set
	 */
	public void setServiceConnected(boolean serviceConnected) {
		firePropertyChange("serviceConnected", this.serviceConnected, this.serviceConnected = serviceConnected);
	}

}
