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
package org.goko.controller.tinyg.commons.bean;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public enum EnumTinyGAxis {
	X_POSITIVE("X" ,"X",true),
	X_NEGATIVE("-X","X",false),
	Y_POSITIVE("Y" ,"Y",true),
	Y_NEGATIVE("-Y","Y",false),
	Z_POSITIVE("Z","Z",true),
	Z_NEGATIVE("-Z","Z",false),
	A_POSITIVE("A","A",true),
	A_NEGATIVE("-A","A",false),
	B_POSITIVE("B","B",true),
	B_NEGATIVE("-B","B",false),
	C_POSITIVE("C","C",true),
	C_NEGATIVE("-C","C",false);

	private String code;
	private String axisCode;
	private boolean direction;

	EnumTinyGAxis(String code, String axisLetter, boolean direction){
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
	public static EnumTinyGAxis getEnum(String code) throws GkException{
		for(EnumTinyGAxis value : EnumTinyGAxis.values()){
			if(StringUtils.equals(value.getCode(), code)){
				return value;
			}
		}
		throw new GkTechnicalException("EnumTinyGAxis with code '"+code+"' does not exist.");
	}

	/**
	 * @return the axisCode
	 */
	public String getAxisCode() {
		return axisCode;
	}
}
