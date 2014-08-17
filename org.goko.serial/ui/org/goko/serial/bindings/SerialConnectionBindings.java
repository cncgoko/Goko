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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;



/**
 * Serial connection part bindings
 *
 * @author PsyKo
 *
 */
public class SerialConnectionBindings extends AbstractModelObject {
	private String connectionStatus;
	private boolean connected;
	private boolean connectionAllowed;
	private boolean disconnectionAllowed;
	private Image connectionImage;

	private List<LabeledValue<Integer>> choiceBaudrate;
	private List<LabeledValue<String>> choiceSerialPort;

	private LabeledValue<Integer> baudrate;
	private LabeledValue<String> serialPort;

	public SerialConnectionBindings(){
		connected = false;
		connectionAllowed = false;
		disconnectionAllowed = false;
		connectionImage = ResourceManager.getPluginImage("org.goko.serial", "icons/bullet_red.png");
		connectionStatus = "Disconnected";

		choiceBaudrate = new ArrayList<LabeledValue<Integer>>();
		choiceBaudrate.add(new LabeledValue<Integer>(9600	,"9600"));
		choiceBaudrate.add(new LabeledValue<Integer>(19200	,"19200"));
		choiceBaudrate.add(new LabeledValue<Integer>(38400	,"38400"));
		choiceBaudrate.add(new LabeledValue<Integer>(57600	,"57600"));
		choiceBaudrate.add(new LabeledValue<Integer>(115200	,"115200"));
		choiceBaudrate.add(new LabeledValue<Integer>(230400	,"230400"));

		baudrate = new LabeledValue<Integer>(115200	,"115200");

		choiceSerialPort = new ArrayList<LabeledValue<String>>();
	}

	public void refreshSerialPortList(){

	}

	/**
	 * @return the connectionAllowed
	 */
	public boolean isConnectionAllowed() {
		return connectionAllowed;
	}
	public boolean getConnectionAllowed() {
		return isConnectionAllowed();
	}

	/**
	 * @param connectionAllowed the connectionAllowed to set
	 */
	public void setConnectionAllowed(boolean connectionAllowed) {
		firePropertyChange("connectionAllowed", this.connectionAllowed, this.connectionAllowed = connectionAllowed);
	}

	/**
	 * @return the disconnectionAllowed
	 */
	public boolean isDisconnectionAllowed() {
		return disconnectionAllowed;
	}
	public boolean getDisconnectionAllowed() {
		return isDisconnectionAllowed();
	}

	/**
	 * @param disconnectionAllowed the disconnectionAllowed to set
	 */
	public void setDisconnectionAllowed(boolean disconnectionAllowed) {
		firePropertyChange("disconnectionAllowed", this.disconnectionAllowed, this.disconnectionAllowed = disconnectionAllowed);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}
	/**
	 * Shortcut for bindings
	 * @return the connected
	 */
	public boolean getConnected() {
		return isConnected();
	}
	/**
	 * @param connected the connected to set
	 */
	public void setConnected(boolean connected) {
		firePropertyChange("connected", this.connected, this.connected = connected);
		setDisconnectionAllowed(connected);
		setConnectionAllowed(!connected);
		if(connected){
			setConnectionImage( ResourceManager.getPluginImage("org.goko.serial", "icons/bullet_green.png"));
			setConnectionStatus( "Connected" );
		}else{
			setConnectionImage( ResourceManager.getPluginImage("org.goko.serial", "icons/bullet_red.png"));
			setConnectionStatus( "Disconnected" );
		}
	}
	/**
	 * @return the connectionImage
	 */
	public Image getConnectionImage() {
		return connectionImage;
	}
	/**
	 * @param connectionImage the connectionImage to set
	 */
	public void setConnectionImage(Image connectionImage) {
		firePropertyChange("connectionImage", this.connectionImage, this.connectionImage = connectionImage);
	}
	/**
	 * @return the connectionStatus
	 */
	public String getConnectionStatus() {
		return connectionStatus;
	}
	/**
	 * @param connectionStatus the connectionStatus to set
	 */
	public void setConnectionStatus(String connectionStatus) {
		firePropertyChange("connectionStatus", this.connectionStatus, this.connectionStatus = connectionStatus);
	}
	/**
	 * @return the choiceBaudrate
	 */
	public List<LabeledValue<Integer>> getChoiceBaudrate() {
		return choiceBaudrate;
	}
	/**
	 * @param choiceBaudrate the choiceBaudrate to set
	 */
	public void setChoiceBaudrate(List<LabeledValue<Integer>> choiceBaudrate) {
		firePropertyChange("choiceBaudrate", this.choiceBaudrate, this.choiceBaudrate = choiceBaudrate);
	}
	/**
	 * @return the baudrate
	 */
	public LabeledValue<Integer> getBaudrate() {
		return baudrate;
	}
	/**
	 * @param baudrate the baudrate to set
	 */
	public void setBaudrate(LabeledValue<Integer> baudrate) {
		firePropertyChange("baudrate", this.baudrate, this.baudrate = baudrate);
	}

	/**
	 * @return the choiceSerialPort
	 */
	public List<LabeledValue<String>> getChoiceSerialPort() {
		return choiceSerialPort;
	}

	/**
	 * @param choiceSerialPort the choiceSerialPort to set
	 */
	public void setChoiceSerialPort(List<LabeledValue<String>> choiceSerialPort) {
		firePropertyChange("choiceSerialPort", this.choiceSerialPort, this.choiceSerialPort = choiceSerialPort);
	}

	/**
	 * @return the serialPort
	 */
	public LabeledValue<String> getSerialPort() {
		return serialPort;
	}

	/**
	 * @param serialPort the serialPort to set
	 */
	public void setSerialPort(LabeledValue<String> serialPort) {
		firePropertyChange("serialPort", this.serialPort, this.serialPort = serialPort);
	}

}
