package org.goko.serial.jssc.service;

public class JsscSerialConnection {
	private String  portName;
	private Integer baudrate;
	private Integer databits;
	private Integer stopbits;
	private Integer parity 	;
	
	
	public JsscSerialConnection(String portName, Integer baudrate, Integer databits, Integer stopbits, Integer parity) {
		super();
		this.portName = portName;
		this.baudrate = baudrate;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;
	}
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
	public Integer getBaudrate() {
		return baudrate;
	}
	/**
	 * @param baudrate the baudrate to set
	 */
	public void setBaudrate(Integer baudrate) {
		this.baudrate = baudrate;
	}
	/**
	 * @return the databits
	 */
	public Integer getDatabits() {
		return databits;
	}
	/**
	 * @param databits the databits to set
	 */
	public void setDatabits(Integer databits) {
		this.databits = databits;
	}
	/**
	 * @return the stopbits
	 */
	public Integer getStopbits() {
		return stopbits;
	}
	/**
	 * @param stopbits the stopbits to set
	 */
	public void setStopbits(Integer stopbits) {
		this.stopbits = stopbits;
	}
	/**
	 * @return the parity
	 */
	public Integer getParity() {
		return parity;
	}
	/**
	 * @param parity the parity to set
	 */
	public void setParity(Integer parity) {
		this.parity = parity;
	}
	
	
}
