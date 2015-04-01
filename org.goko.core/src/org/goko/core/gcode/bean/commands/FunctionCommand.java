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
package org.goko.core.gcode.bean.commands;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;

/**
 * Defines a Function command (M word)
 *
 * @author PsyKo
 *
 */
public class FunctionCommand extends SettingCommand{
	/** Function type */
	private List<EnumGCodeCommandFunctionType> lstFunctionType;

	public FunctionCommand() {
		setType(EnumGCodeCommandType.FUNCTION);
		lstFunctionType = new ArrayList<EnumGCodeCommandFunctionType>();
	}

	/**
	 * @param functionType the functionType to set
	 */
	public void addFunctionType(EnumGCodeCommandFunctionType functionType) {
		this.lstFunctionType.add(functionType);
	}
	public boolean isFunctionType(EnumGCodeCommandFunctionType functionType){
		return lstFunctionType.contains(functionType);
	}
	public List<EnumGCodeCommandFunctionType> getFunctionType(){
		return new ArrayList<EnumGCodeCommandFunctionType>(lstFunctionType);
	}
	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		visitor.visit(this);
	}



}
