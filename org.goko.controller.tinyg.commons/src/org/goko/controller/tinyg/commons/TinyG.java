/**
 * 
 */
package org.goko.controller.tinyg.commons;

/**
 * @author Psyko
 * @date 7 janv. 2017
 */
public class TinyG {
	public static final String RESPONSE_ENVELOPE = "r";
	public static final String FOOTER = "f";
	public static final String STATUS_REPORT = "sr";
	public static final String QUEUE_REPORT = "qr";
	public static final String GCODE_COMMAND = "gc";
	public static final String LINE_REPORT = "n";
	public static final String PROBE_REPORT = "prb";
	public static final String MESSAGE_REPORT = "msg";
	public static final String ERROR_REPORT = "er";
	
	public static final String JSON_SYNTAX = "ej";
	public static final int    JSON_SYNTAX_STRICT = 1;
	
	public static final String ON = "On";
	public static final String OFF = "Off";
	
	public static final int FOOTER_STATUS_CODE_INDEX = 1;
	public static final int FOOTER_BYTES_COUNT_INDEX = 2;
	public static final int FOOTER_CHECKSUM_INDEX = 3;
	
	public static final String STATUS_REPORT_WORK_POSITION_X = "posx";
	public static final String STATUS_REPORT_WORK_POSITION_Y = "posy";
	public static final String STATUS_REPORT_WORK_POSITION_Z = "posz";
	public static final String STATUS_REPORT_WORK_POSITION_A = "posa";
	public static final String STATUS_REPORT_MACHINE_POSITION_X = "mpox";
	public static final String STATUS_REPORT_MACHINE_POSITION_Y = "mpoy";
	public static final String STATUS_REPORT_MACHINE_POSITION_Z = "mpoz";
	public static final String STATUS_REPORT_MACHINE_POSITION_A = "mpoa";
	public static final String STATUS_REPORT_UNITS = "unit";
	public static final String STATUS_REPORT_COORDINATES = "coor";
	public static final String STATUS_REPORT_MOTION_MODE = "momo";
	public static final String STATUS_REPORT_PLANE = "plan";
	public static final String STATUS_REPORT_PATH_CONTROL = "path";
	public static final String STATUS_REPORT_DISTANCE_MODE = "dist";
	public static final String STATUS_REPORT_VELOCITY = "vel";
	public static final String STATUS_REPORT_FEEDRATE = "feed";
	public static final String STATUS_REPORT_STATE = "stat";
	public static final String STATUS_REPORT_ARC_DISTANCE_MODE = "admo";
	public static final String STATUS_REPORT_TOOL = "tool";
	public static final String STATUS_REPORT_FEEDRATE_MODE = "frmo";
	public static final String STATUS_REPORT_MACHINE_POSITION = "mpo";
	public static final String STATUS_REPORT_WORK_POSITION = "pos";
}
