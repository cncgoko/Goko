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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkTechnicalException;

public class CoordinateSystemFactory{
	private List<CoordinateSystem> lstCoordinateSystem;
	
	/**
	 * Constructor
	 */
	public CoordinateSystemFactory() {
		lstCoordinateSystem = new ArrayList<CoordinateSystem>();
		lstCoordinateSystem.add(CoordinateSystem.G53);
		lstCoordinateSystem.add(CoordinateSystem.G54);
		lstCoordinateSystem.add(CoordinateSystem.G55);
		lstCoordinateSystem.add(CoordinateSystem.G56);
		lstCoordinateSystem.add(CoordinateSystem.G57);
		lstCoordinateSystem.add(CoordinateSystem.G58);
		lstCoordinateSystem.add(CoordinateSystem.G59);
		lstCoordinateSystem.add(CoordinateSystem.G53);
	}
	
	/**
	 * Register the given coordinate system
	 * @param coordinateSystem
	 */
	public void addCoordinateSystem(CoordinateSystem coordinateSystem){
		lstCoordinateSystem.add(coordinateSystem);
	}
	/**
	 * Returns the enum for the given int value 
	 * @param intValue the int value 
	 * @return EnumCoordinateSystem
	 * @throws GkTechnicalException GkTechnicalException
	 */
	public CoordinateSystem get(int intValue) throws GkTechnicalException{
		for (CoordinateSystem coordinateSystem : lstCoordinateSystem) {
			if(coordinateSystem.getIntValue() == intValue){
				return coordinateSystem;
			}
		}	
		throw new GkTechnicalException("No CoordinateSystem for int value ["+intValue+"]");
	}
	
	/**
	 * Returns the enum for the given code 
	 * @param code the code  
	 * @return EnumCoordinateSystem
	 * @throws GkTechnicalException GkTechnicalException
	 */
	public CoordinateSystem get(String code) throws GkTechnicalException{
		for (CoordinateSystem coordinateSystem : lstCoordinateSystem) {
			if(StringUtils.equalsIgnoreCase(coordinateSystem.getCode(), code)){
				return coordinateSystem;
			}
		}	
		throw new GkTechnicalException("No CoordinateSystem for code ["+code+"]");
	}

	/**
	 * @return the list of coordinate system
	 */
	public List<CoordinateSystem> get() {
		return lstCoordinateSystem;
	}
	
}
