/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.rs274ngcv3.context;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.ICoordinateSystem;

public enum EnumCoordinateSystem implements ICoordinateSystem{
	G53(0, "G53"),
	G54(1, "G54"),
	G55(2, "G55"),
	G56(3, "G56"),
	G57(4, "G57"),
	G58(5, "G58"),
	G59(6, "G59"),
	G59_1(7, "G59.1"),
	G59_2(8, "G59.2"),
	G59_3(9, "G59.3");
	
	/** Integer denomination of the coordinate system */
	private int intValue;
	
	private String code;
	/** Constructor
	 * @param intValue the integer value of the coordinate system 
	 */
	private EnumCoordinateSystem(int intValue, String code) {
		this.intValue = intValue;
		this.code = code;
	}
	
	/**
	 * Returns the enum for the given int value 
	 * @param intValue the int value 
	 * @return EnumCoordinateSystem
	 * @throws GkTechnicalException GkTechnicalException
	 */
	public static EnumCoordinateSystem getEnum(int intValue) throws GkTechnicalException{
		for (EnumCoordinateSystem enumCS : values()) {
			if(enumCS.getIntValue() == intValue){
				return enumCS;
			}
		}
		throw new GkTechnicalException("No EnumCoordinateSystem for int value ["+intValue+"]");
	}

	/**
	 * @return the intValue
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.ICoordinateSystem#equals(org.goko.core.gcode.element.ICoordinateSystem)
	 */
	@Override
	public boolean equals(ICoordinateSystem coordinateSystem) {		
		return StringUtils.equals(code, coordinateSystem.getCode());
	}
	
}
