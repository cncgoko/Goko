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
package org.goko.serial.jssc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Runnable used to asynchronously notify listeners of received serial events and data
 *
 * @author PsyKo
 *
 */
public class JsscSerialEventListener implements Runnable{
	/** Xon char  */
	protected static final byte XON = 0x11;
	/** Xoff char  */
	protected static final byte XOFF = 0x13;
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JsscSerialEventListener.class);
	/** Input buffer to process */
	byte[] inputBuffer;
	/** Used serial port */
	JsscSerialConnectionService jsscService;

	/**
	 * Constructor
	 * @param service the {@link JsscSerialConnectionService}
	 * @param buffer the buffer to process
	 */
	public JsscSerialEventListener(JsscSerialConnectionService service, byte[] buffer) {
		inputBuffer = buffer;
		jsscService = service;
	}

	/** (inheritDoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<Byte> lstData = new ArrayList<Byte>();
		for (byte b : inputBuffer) {
			if(b == XOFF){
				jsscService.setXoff();
			}else if(b == XON){
				jsscService.setXon();
			}else{
				lstData.add( b );
			}
		}

		try {
			String str = StringUtils.replace(GkUtils.toString(lstData),""+'\r',"\\13");
			str = StringUtils.replace(str, ""+'\n', "\\10");
			System.out.println("Received in jssc "+ str);
			jsscService.notifyInputListeners(lstData);
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}
