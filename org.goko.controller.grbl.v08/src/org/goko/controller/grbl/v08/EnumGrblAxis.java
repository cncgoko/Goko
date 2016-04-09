/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.v08;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public enum EnumGrblAxis {
	X_POSITIVE("X" ,"X",true),
	X_NEGATIVE("-X","X",false),
	Y_POSITIVE("Y" ,"Y",true),
	Y_NEGATIVE("-Y","Y",false),
	Z_POSITIVE("Z","Z",true),
	Z_NEGATIVE("-Z","Z",false);

	private String code;
	private String axisCode;
	private boolean direction;

	EnumGrblAxis(String code, String axisLetter, boolean direction){
		this.code = code;
		this.axisCode = axisLetter;
		this.direction = direction;
	}

	public boolean isPositive(){
		return direction == true;
	}

	public boolean isNegative(){
		return direction == false;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Retrieve the enumeration object from its code
	 * @param code the code of the requested enum
	 * @return EnumTinyGAxis
	 * @throws GkException if the enum could not be found
	 */
	public static EnumGrblAxis getEnum(String code) throws GkException{
		for(EnumGrblAxis value : EnumGrblAxis.values()){
			if(StringUtils.equals(value.getCode(), code)){
				return value;
			}
		}
		throw new GkTechnicalException("GrblTinyGAxis with code '"+code+"' does not exist.");
	}

	/**
	 * @return the axisCode
	 */
	public String getAxisCode() {
		return axisCode;
	}
}
