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
package org.goko.controller.grbl.v11;

import org.goko.core.common.exception.GkTechnicalException;

/**
 * Constants definition for Grbl 1.1
 * @author PsyKo
 *
 */
public enum EnumGrblGCodeError {
	ERR_23(23,"A G or M command value in the block is not an integer. For example, G4 can't be G4.13. Some G-code commands are floating point (G92.1), but these are ignored."),
	ERR_24(24,"Two G-code commands that both require the use of the XYZ axis words were detected in the block."),
	ERR_25(25,"A G-code word was repeated in the block."),
	ERR_26(26,"A G-code command implicitly or explicitly requires XYZ axis words in the block, but none were detected."),
	ERR_27(27,"The G-code protocol mandates N line numbers to be within the range of 1-99,999. We think that's a bit silly and arbitrary. So, we increased the max number to 9,999,999. This error occurs when you send a number more than this."),
	ERR_28(28,"A G-code command was sent, but is missing some important P or L value words in the line. Without them, the command can't be executed. Check your G-code."),
	ERR_29(29,"Grbl supports six work coordinate systems G54-G59. This error happens when trying to use or configure an unsupported work coordinate system, such as G59.1, G59.2, and G59.3."),
	ERR_30(30,"The G53 G-code command requires either a G0 seek or G1 feed motion mode to be active. A different motion was active."),
	ERR_31(31,"There are unused axis words in the block and G80 motion mode cancel is active."),
	ERR_32(32,"A G2 or G3 arc was commanded but there are no XYZ axis words in the selected plane to trace the arc."),
	ERR_33(33,"The motion command has an invalid target. G2, G3, and G38.2 generates this error. For both probing and arcs traced with the radius definition, the current position cannot be the same as the target. This also errors when the arc is mathematically impossible to trace, where the current position, the target position, and the radius of the arc doesn't define a valid arc."),
	ERR_34(34,"A G2 or G3 arc, traced with the radius definition, had a mathematical error when computing the arc geometry. Try either breaking up the arc into semi-circles or quadrants, or redefine them with the arc offset definition."),
	ERR_35(35,"A G2 or G3 arc, traced with the offset definition, is missing the IJK offset word in the selected plane to trace the arc."),
	ERR_36(36,"There are unused, leftover G-code words that aren't used by any command in the block."),
	ERR_37(37,"The G43.1 dynamic tool length offset command cannot apply an offset to an axis other than its configured axis. The Grbl default axis is the Z-axis.");
	private int id;
	private String message;
	
	/**
	 * @param id
	 * @param message
	 */
	private EnumGrblGCodeError(int id, String message) {
		this.id = id;
		this.message = message;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public static EnumGrblGCodeError getById(int id) throws GkTechnicalException{
		EnumGrblGCodeError error = findById(id);
		if(error == null){
			throw new GkTechnicalException("Grbl GCode unknown error code ["+id+"]");
		}
		return error;
	}
	
	public static EnumGrblGCodeError findById(int id){
		for (EnumGrblGCodeError error : values()) {
			if(error.getId() == id){
				return error;
			}
		}		
		return null;
	}
}
