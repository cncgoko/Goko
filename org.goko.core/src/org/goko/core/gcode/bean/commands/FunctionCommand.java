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
package org.goko.core.gcode.bean.commands;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;

/**
 * Defines a Function command (M word)
 *
 * @author PsyKo
 *
 */
public class FunctionCommand extends GCodeCommand {
	/** Function type */
	private EnumGCodeCommandFunctionType functionType;

	/**
	 * @param functionType
	 */
	FunctionCommand(EnumGCodeCommandFunctionType functionType) {
		super();
		this.functionType = functionType;
	}

	/**
	 * @return the functionType
	 */
	public EnumGCodeCommandFunctionType getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType the functionType to set
	 */
	public void setFunctionType(EnumGCodeCommandFunctionType functionType) {
		this.functionType = functionType;
	}

	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		throw new GkTechnicalException("Not implemented");
		//visitor.visit(this);

	}



}
