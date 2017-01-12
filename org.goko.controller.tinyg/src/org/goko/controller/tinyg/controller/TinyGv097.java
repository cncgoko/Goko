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
package org.goko.controller.tinyg.controller;

import org.goko.core.controller.bean.DefaultControllerValues;

/**
 * Constants definition
 *
 * @author PsyKo
 *
 */
public class TinyGv097 implements DefaultControllerValues{
	/** Feed hold command */
	public static final String FEED_HOLD = "!";
	/** Cycle start command */
	public static final String CYCLE_START = "~";
	/** Queue flush command */
	public static final String QUEUE_FLUSH = "%";
    /** Reset command */
    public static final byte RESET_COMMAND = 0x18;
	protected static final String AVAILABLE_BUFFER_COUNT = "tinyg.buffer.count";
	public static final String ON = "On";
	public static final String OFF = "Off";

	public class Topic{
		public class TinyGExecutionError{
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String TOPIC		= "topic/tinyg/execution/error";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String TITLE		= "prop/tinyg/execution/error/title";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String MESSAGE		= "prop/tinyg/execution/error/message";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String ERROR		= "prop/tinyg/execution/error/error";
		}
	}

	public static final String RESPONSE_ENVELOPE = "r";
	public static final String FOOTER = "f";
	public static final String STATUS_REPORT = "sr";
	public static final String QUEUE_REPORT = "qr";
	public static final String GCODE_COMMAND = "gc";
	public static final String LINE_REPORT = "n";
	public static final String PROBE_REPORT = "prb";
	public static final String MESSAGE_REPORT = "msg";

	public static final String STATUS_REPORT_POSITION_X = "posx";
	public static final String STATUS_REPORT_POSITION_Y = "posy";
	public static final String STATUS_REPORT_POSITION_Z = "posz";
	public static final String STATUS_REPORT_POSITION_A = "posa";
	public static final String STATUS_REPORT_UNITS = "unit";
	public static final String STATUS_REPORT_COORDINATES = "coor";
	public static final String STATUS_REPORT_DISTANCE_MODE = "dist";
	public static final String STATUS_REPORT_VELOCITY = "vel";
	public static final String STATUS_REPORT_FEEDRATE = "feed";
	public static final String STATUS_REPORT_STATE = "stat";

	public static final String PROBE_REPORT_SUCCESS    = "e";
	public static final String PROBE_REPORT_POSITION_X = "x";
	public static final String PROBE_REPORT_POSITION_Y = "y";
	public static final String PROBE_REPORT_POSITION_Z = "z";
	public static final String PROBE_REPORT_POSITION_A = "a";
	public static final String PROBE_REPORT_POSITION_B = "b";
	public static final String PROBE_REPORT_POSITION_C = "c";

	public static final int FOOTER_STATUS_CODE_INDEX = 1;
	public static final int FOOTER_BYTES_COUNT_INDEX = 2;
	public static final int FOOTER_CHECKSUM_INDEX = 3;
	private static final int HASHMASK = 9999;
}
