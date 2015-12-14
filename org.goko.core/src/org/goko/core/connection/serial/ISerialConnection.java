/**
 * 
 */
package org.goko.core.connection.serial;

/**
 * @author PsyKo
 *
 */
public interface ISerialConnection {
	String getPortName();
	int getBaudrate();
	int getDatabits();
	int getStopbits();
	int getParity();
	int getFlowControl();
}
