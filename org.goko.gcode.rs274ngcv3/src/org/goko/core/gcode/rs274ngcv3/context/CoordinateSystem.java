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
import org.goko.core.gcode.element.ICoordinateSystem;

public class CoordinateSystem implements ICoordinateSystem{
	public static final CoordinateSystem G53 = new CoordinateSystem(0, "G53");
	public static final CoordinateSystem G54 = new CoordinateSystem(1, "G54");
	public static final CoordinateSystem G55 = new CoordinateSystem(2, "G55");
	public static final CoordinateSystem G56 = new CoordinateSystem(3, "G56");
	public static final CoordinateSystem G57 = new CoordinateSystem(4, "G57");
	public static final CoordinateSystem G58 = new CoordinateSystem(5, "G58");
	public static final CoordinateSystem G59 = new CoordinateSystem(6, "G59");
	public static final CoordinateSystem G59_1 = new CoordinateSystem(7, "G59.1");
	public static final CoordinateSystem G59_2 = new CoordinateSystem(8, "G59.2");
	public static final CoordinateSystem G59_3 = new CoordinateSystem(9, "G59.3");
	
	/** Integer denomination of the coordinate system */
	private int intValue;
	
	private String code;
	/** Constructor
	 * @param intValue the integer value of the coordinate system 
	 */
	private CoordinateSystem(int intValue, String code) {
		this.intValue = intValue;
		this.code = code;
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

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoordinateSystem other = (CoordinateSystem) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
