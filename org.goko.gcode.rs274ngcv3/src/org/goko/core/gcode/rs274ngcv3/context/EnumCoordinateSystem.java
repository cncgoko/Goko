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

import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.ICoordinateSystem;

public enum EnumCoordinateSystem implements ICoordinateSystem{
	G53(0),
	G54(1),
	G55(2),
	G56(3),
	G57(4),
	G58(5),
	G59(6),
	G59_1(7),
	G59_2(8),
	G59_3(9);
	
	/** Integer denomination of the coordinate system */
	int intValue;

	/** Constructor
	 * @param intValue the integer value of the coordinate system 
	 */
	private EnumCoordinateSystem(int intValue) {
		this.intValue = intValue;
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
}
