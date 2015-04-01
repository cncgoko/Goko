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

import java.util.Map;

import jssc.SerialPort;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

public class JsscUtils {

	protected static void verifyParameterMap(Map<String, Object> param) throws GkException{
		if(MapUtils.isEmpty(param)){
			throw new GkFunctionalException("Error : no parameters found for Jssc Connection");
		}
	}

	protected static String getParameterPortName(Map<String, Object> param) throws GkException{
		String portName = StringUtils.EMPTY;
		portName = getParameter(param, JsscParameter.PORTNAME.name(), String.class, true);
		return portName;
	}

	protected static Integer getParameterBaudrate(Map<String, Object> param) throws GkException{
		int baudrate = SerialPort.BAUDRATE_115200;
		Integer baudrateParam = getParameter(param, JsscParameter.BAUDRATE.name(), Integer.class, false);
		if(baudrateParam != null){
			baudrate = baudrateParam.intValue();
		}
		return baudrate;
	}

	protected static Integer getParameterDatabits(Map<String, Object> param) throws GkException{
		int databits = SerialPort.DATABITS_8;
		Integer databitsParam = getParameter(param, JsscParameter.DATABITS.name(), Integer.class, false);
		if(databitsParam != null){
			databits = databitsParam.intValue();
		}
		return databits;
	}

	protected static Integer getParameterStopbits(Map<String, Object> param) throws GkException{
		int stopbits = SerialPort.STOPBITS_1;
		Integer stopbitsParam = getParameter(param, JsscParameter.STOPBITS.name(), Integer.class, false);
		if(stopbitsParam != null){
			stopbits = stopbitsParam.intValue();
		}
		return stopbits;
	}

	protected static Integer getParameterParity(Map<String, Object> param) throws GkException{
		int parity = SerialPort.PARITY_NONE;
		Integer parityParam = getParameter(param, JsscParameter.PARITY.name(), Integer.class, false);
		if(parityParam != null){
			parity = parityParam.intValue();
		}
		return parity;
	}

	@SuppressWarnings("unchecked")
	protected static <T> T getParameter(Map<String, Object> param, String name, Class<T> clazz, boolean required) throws GkException{
		verifyParameterMap(param);
		T result = null;
		if(param.containsKey(name)){
			Object obj = param.get(name);
			if(obj != null){
				if( clazz.isAssignableFrom(obj.getClass())){
					result = (T)obj;
				}
			}else if(required){
				throw new GkFunctionalException("Unable to find parameter ["+name+"] in JSSC Connection parameter map");
			}
		}
		return result;
	}

	public static Integer getParameterFlowControl(Map<String, Object> parameters) throws GkException {
		int flowControl = 0;
		String xonXoffParam = getParameter(parameters, JsscParameter.XONXOFF.name(), String.class, false);
		String rcsCtsParam = getParameter(parameters, JsscParameter.RCSCTS.name(), String.class, false);
		if(xonXoffParam != null && Boolean.valueOf(xonXoffParam)){
			flowControl |= SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
		}
		if(rcsCtsParam != null && Boolean.valueOf(rcsCtsParam)){
			flowControl |= SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
		}
		return flowControl;
	}
}
