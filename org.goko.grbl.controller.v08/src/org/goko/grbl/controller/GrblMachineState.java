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
package org.goko.grbl.controller;

import org.goko.core.controller.bean.MachineState;

/**
 * Enumeration of TinyG machine states
 *
 * @author PsyKo
 *
 */
public class GrblMachineState extends MachineState{
	public static final GrblMachineState UNDEFINED 		= new GrblMachineState(-1,"Undefined");
	public static final GrblMachineState IDLE 			= new GrblMachineState(100, "Idle");

	public GrblMachineState(int code, String label){
		super(code,label);
	}

}
