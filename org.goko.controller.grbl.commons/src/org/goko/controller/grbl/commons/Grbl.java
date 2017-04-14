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
package org.goko.controller.grbl.commons;

import org.goko.core.controller.bean.DefaultControllerValues;

/**
 * Constants definition for Grbl
 * @author PsyKo
 *
 */
public abstract class Grbl implements DefaultControllerValues {
	/** Current status command */
	public static final String CURRENT_STATUS = "?";
	/** View parameters request */
	public static final String VIEW_PARAMETERS = "$#";
	/** View parser state request */
	public static final String PARSER_STATE = "$G";
	/** Response ok from Grbl */
	public static final CharSequence OK_RESPONSE = "ok";
	/** Pause command */
    public static final byte PAUSE_COMMAND = '!';
    /** Resume command */
    public static final byte RESUME_COMMAND = '~';
    /** Status command */
    public static final byte STATUS_COMMAND = '?';
    /** Reset command */
    public static final byte RESET_COMMAND = 0x18;
    /** Kill alarm command*/
    public static final String KILL_ALARM_COMMAND = "$X";
    /** Check mode enable/disable*/
    public static final String CHECK_MODE = "$C";
    /** Home sequence command*/
    public static final String HOME_COMMAND = "$H";
    /** Read configuration command */
    public static final String CONFIGURATION = "$$";
    /** Constant for Grbl used buffer value in value store */
    public static final String GRBL_AVAILABLE_RX_BUFFER = "GrblControllerAvailableRxBuffer";
    /** Constant for Grbl planner buffer value in value store */
    public static final String GRBL_AVAILABLE_PLANNER_BUFFER = "GrblControllerAvailablePlannerBuffer";
	/** Grbl buffer size */
	public static final int GRBL_BUFFER_SIZE = 120;
	/** G54 offset name */
	public static final String G54_OFFSET = "G54";
	/** G55 offset name */
	public static final String G55_OFFSET = "G55";
	/** G56 offset name */
	public static final String G56_OFFSET = "G56";
	/** G57 offset name */
	public static final String G57_OFFSET = "G57";
}
