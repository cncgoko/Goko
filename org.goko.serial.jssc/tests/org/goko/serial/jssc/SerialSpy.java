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
package org.goko.serial.jssc;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.goko.core.common.GkUtils;

// COM6  <-->  InSerialPort  <--> outSerialPort <--> COM1 <--> COM2 <--> Goko
public class SerialSpy {
	SerialPort inSerialPort;
	String inPortName = "COM6";
	Listener inLlistener;

	SerialPort outSerialPort;
	String outPortName = "COM1";
	Listener outListner;

	public SerialSpy() {
		this.inSerialPort = new SerialPort(inPortName);
		this.outSerialPort = new SerialPort(outPortName);

		this.inLlistener = new Listener(inSerialPort,outSerialPort,false);
		this.outListner = new Listener(outSerialPort, inSerialPort, true);

		try {
			this.inSerialPort.openPort();
			this.inSerialPort.setParams(SerialPort.BAUDRATE_9600,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			this.inSerialPort.addEventListener(inLlistener, SerialPort.MASK_RXCHAR | SerialPort.MASK_ERR);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.outSerialPort.openPort();
			this.outSerialPort.setParams(SerialPort.BAUDRATE_9600,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			this.outSerialPort.addEventListener(outListner, SerialPort.MASK_RXCHAR  | SerialPort.MASK_ERR);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SerialSpy spy = new SerialSpy();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true){
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Thread.yield();
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Listener implements SerialPortEventListener {
	SerialPort outputPort;
	SerialPort inputPort;
	boolean trace;

	public Listener(SerialPort inSerialPort, SerialPort outSerialPort, boolean trace ) {
		this.outputPort = inSerialPort;
		this.inputPort = outSerialPort;
		this.trace = trace;
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		if(serialPortEvent.isRXCHAR()){
			int dataAvailableCount = serialPortEvent.getEventValue();
			try {
				byte buffer[] = inputPort.readBytes();//readBytes(dataAvailableCount);
				if(buffer != null && buffer.length > 0){
					// Process the call
					if(trace){
						System.out.print(GkUtils.toString(buffer));
					}
					outputPort.writeBytes(buffer);
				}
			} catch (SerialPortException e) {
				System.err.println(e);
			}
		}else if(serialPortEvent.isERR()){
			System.err.println("err");
		}
	}

}