/**
 * 
 */
package org.goko.junit.tools.connection;

import org.goko.core.connection.serial.ISerialConnection;

/**
 * @author PsyKo
 *
 */
public class EmulatedSerialConnection implements ISerialConnection {
	private String  portName;
	private int baudrate;
	private int databits;
	private int stopbits;
	private int parity;
	private int flowControl;
	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}
	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}
	/**
	 * @return the baudrate
	 */
	public int getBaudrate() {
		return baudrate;
	}
	/**
	 * @param baudrate the baudrate to set
	 */
	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}
	/**
	 * @return the databits
	 */
	public int getDatabits() {
		return databits;
	}
	/**
	 * @param databits the databits to set
	 */
	public void setDatabits(int databits) {
		this.databits = databits;
	}
	/**
	 * @return the stopbits
	 */
	public int getStopbits() {
		return stopbits;
	}
	/**
	 * @param stopbits the stopbits to set
	 */
	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}
	/**
	 * @return the parity
	 */
	public int getParity() {
		return parity;
	}
	/**
	 * @param parity the parity to set
	 */
	public void setParity(int parity) {
		this.parity = parity;
	}
	/**
	 * @return the flowControl
	 */
	public int getFlowControl() {
		return flowControl;
	}
	/**
	 * @param flowControl the flowControl to set
	 */
	public void setFlowControl(int flowControl) {
		this.flowControl = flowControl;
	}
}
