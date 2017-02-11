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
package org.goko.controller.g2core.controller;

import org.goko.controller.tinyg.commons.ITinyGStatus;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

/**
 * TinyG Status code enumeration
 *
 * @see https://github.com/synthetos/TinyG/wiki/TinyG-Status-Codes
 * @author PsyKo
 *
 */
public enum G2CoreStatusCode implements ITinyGStatus {	
	STAT_UNKNOWN(-1,""),
	STAT_OK(0,"Function completed OK"),
	STAT_ERROR(1,"Generic error return (EPERM)"),
	STAT_EAGAIN(2,"Function would block here (call again)"),
	STAT_NOOP(3,"Function had no-operation"),
	STAT_COMPLETE(4,"Operation is complete"),
	STAT_SHUTDOWN(5,"Operation was shutdown (terminated gracefully)"),
	STAT_PANIC(6,"System panic (not graceful)"),
	STAT_EOL(7,"Function returned end-of-line"),
	STAT_EOF(8,"Function returned end-of-file"),
	STAT_FILE_NOT_OPEN(9,""),
	STAT_FILE_SIZE_EXCEEDED(10,""),
	STAT_NO_SUCH_DEVICE(11,""),
	STAT_BUFFER_EMPTY(12,""),
	STAT_BUFFER_FULL(13,""),
	STAT_BUFFER_FULL_FATAL(14,""),
	STAT_INITIALIZING(15,"Initializing - not ready for use"),
	STAT_ENTERING_BOOT_LOADER(16,"This code actually emitted from boot loader, not g2"),
	STAT_FUNCTION_IS_STUBBED(17,""),
	STAT_ALARM(18,"System alarm triggered"),
	STAT_NO_DISPLAY(19,"Suppress results display - presumably handled upstream"),

	// Internal errors and startup messages
	STAT_INTERNAL_ERROR(20,"Unrecoverable internal error"),
	STAT_INTERNAL_RANGE_ERROR(21,"Number range other than by user input"),
	STAT_FLOATING_POINT_ERROR(22,"Number conversion error"),
	STAT_DIVIDE_BY_ZERO(23,""),
	STAT_INVALID_ADDRESS(24,""),
	STAT_READ_ONLY_ADDRESS(25,""),
	STAT_INIT_FAILURE(26,""),
	STAT_ERROR_27(27,"Was ALARMED in 0.97"),
	STAT_FAILED_TO_GET_PLANNER_BUFFER(28,""),
	STAT_GENERIC_EXCEPTION_REPORT(29,"Used for test"),

	STAT_PREP_LINE_MOVE_TIME_IS_INFINITE(30,""),
	STAT_PREP_LINE_MOVE_TIME_IS_NAN(31,""),
	STAT_FLOAT_IS_INFINITE(32,""),
	STAT_FLOAT_IS_NAN(33,""),
	STAT_PERSISTENCE_ERROR(34,""),
	STAT_BAD_STATUS_REPORT_SETTING(35,""),
	STAT_FAILED_GET_PLANNER_BUFFER(36,""),

	// Assertion failures - build down from 99 until they meet the system internal errors
	STAT_BUFFER_FREE_ASSERTION_FAILURE(88,""),
	STAT_STATE_MANAGEMENT_ASSERTION_FAILURE(89,""),
	STAT_CONFIG_ASSERTION_FAILURE(90,""),
	STAT_XIO_ASSERTION_FAILURE(91,""),
	STAT_ENCODER_ASSERTION_FAILURE(92,""),
	STAT_STEPPER_ASSERTION_FAILURE(93,""),
	STAT_PLANNER_ASSERTION_FAILURE(94,""),
	STAT_CANONICAL_MACHINE_ASSERTION_FAILURE(95,""),
	STAT_CONTROLLER_ASSERTION_FAILURE(96,""),
	STAT_STACK_OVERFLOW(97,""),
	STAT_MEMORY_FAULT(98,"Generic memory corruption detected by magic numbers"),
	STAT_GENERIC_ASSERTION_FAILURE(99,"Generic assertion failure - unclassified"),

	// Application and data input errors

	// Generic data input errors
	STAT_UNRECOGNIZED_NAME(100,"Parser didn't recognize the name"),
	STAT_INVALID_OR_MALFORMED_COMMAND(101,"Malformed line to parser"),
	STAT_BAD_NUMBER_FORMAT(102,"Number format error"),
	STAT_UNSUPPORTED_TYPE(103,"An otherwise valid JSON type is not supported"),
	STAT_PARAMETER_IS_READ_ONLY(104,"Input error: parameter cannot be set"),
	STAT_PARAMETER_CANNOT_BE_READ(105,"Input error: parameter cannot be returned"),
	STAT_COMMAND_NOT_ACCEPTED(106,"Input error: command cannot be accepted at this time"),
	STAT_INPUT_EXCEEDS_MAX_LENGTH(107,"Input error: input string is too long"),
	STAT_INPUT_LESS_THAN_MIN_VALUE(108,"Input error: value is under minimum"),
	STAT_INPUT_EXCEEDS_MAX_VALUE(109,"Input error: value is over maximum"),
	STAT_INPUT_VALUE_RANGE_ERROR(110,"Input error: value is out-of-range"),

	STAT_JSON_SYNTAX_ERROR(111,"JSON input string is not well formed"),
	STAT_JSON_TOO_MANY_PAIRS(112,"JSON input string has too many JSON pairs"),
	STAT_JSON_OUTPUT_TOO_LONG(113,"JSON output exceeds buffer size"),
	STAT_NESTED_TXT_CONTAINER(114,"JSON 'txt' fields cannot be nested"),
	STAT_MAX_DEPTH_EXCEEDED(115,"JSON exceeded maximum nesting depth"),
	STAT_VALUE_TYPE_ERROR(116,"JSON value does not agree with variable type"),

	// Gcode errors and warnings (Most originate from NIST - by concept, not number)
	STAT_GCODE_GENERIC_INPUT_ERROR(130,"Generic error for gcode input"),
	STAT_GCODE_COMMAND_UNSUPPORTED(131,"G command is not supported"),
	STAT_MCODE_COMMAND_UNSUPPORTED(132,"M command is not supported"),
	STAT_GCODE_MODAL_GROUP_VIOLATION(133,"Gcode modal group error"),
	STAT_GCODE_AXIS_IS_MISSING(134,"Command requires at least one axis present"),
	STAT_GCODE_AXIS_CANNOT_BE_PRESENT(135,"Error if G80 has axis words"),
	STAT_GCODE_AXIS_IS_INVALID(136,"An axis is specified that is illegal for the command"),
	STAT_GCODE_AXIS_IS_NOT_CONFIGURED(137,"WARNING: attempt to program an axis that is disabled"),
	STAT_GCODE_AXIS_NUMBER_IS_MISSING(138,"Axis word is missing its value"),
	STAT_GCODE_AXIS_NUMBER_IS_INVALID(139,"Axis word value is illegal"),

	STAT_GCODE_ACTIVE_PLANE_IS_MISSING(140,"Active plane is not programmed"),
	STAT_GCODE_ACTIVE_PLANE_IS_INVALID(141,"Active plane selected is not valid for this command"),
	STAT_GCODE_FEEDRATE_NOT_SPECIFIED(142,"Move has no feedrate"),
	STAT_GCODE_INVERSE_TIME_MODE_CANNOT_BE_USED(143,"G38.2 and some canned cycles cannot accept inverse time mode"),
	STAT_GCODE_ROTARY_AXIS_CANNOT_BE_USED(144,"G38.2 and some other commands cannot have rotary axes"),
	STAT_GCODE_G53_WITHOUT_G0_OR_G1(145,"G0 or G1 must be active for G53"),
	STAT_REQUESTED_VELOCITY_EXCEEDS_LIMITS(146,""),
	STAT_CUTTER_COMPENSATION_CANNOT_BE_ENABLED(147,""),
	STAT_PROGRAMMED_POINT_SAME_AS_CURRENT_POINT(148,""),
	STAT_SPINDLE_SPEED_BELOW_MINIMUM(149,""),

	STAT_SPINDLE_SPEED_MAX_EXCEEDED(150,""),
	STAT_SPINDLE_MUST_BE_OFF(151,""),
	STAT_SPINDLE_MUST_BE_TURNING(152,"Some canned cycles require spindle to be turning when called"),
	STAT_ARC_ERROR_RESERVED(153,"RESERVED"),
	STAT_ARC_HAS_IMPOSSIBLE_CENTER_POINT(154,"Trap (.05 inch/.5 mm) OR ((.0005 inch/.005mm) AND .1% of radius condition"),
	STAT_ARC_SPECIFICATION_ERROR(155,"Generic arc specification error"),
	STAT_ARC_AXIS_MISSING_FOR_SELECTED_PLANE(156,"Arc is missing axis (axes) required by selected plane"),
	STAT_ARC_OFFSETS_MISSING_FOR_SELECTED_PLANE(157,"One or both offsets are not specified"),
	STAT_ARC_RADIUS_OUT_OF_TOLERANCE(158,"WARNING - radius arc is too large - accuracy in question"),
	STAT_ARC_ENDPOINT_IS_STARTING_POINT(159,""),

	STAT_P_WORD_IS_MISSING(160,"P must be present for dwells and other functions"),
	STAT_P_WORD_IS_INVALID(161,"Generic P value error"),
	STAT_P_WORD_IS_ZERO(162,""),
	STAT_P_WORD_IS_NEGATIVE(163,"Dwells require positive P values"),
	STAT_P_WORD_IS_NOT_AN_INTEGER(164,"G10s and other commands require integer P numbers"),
	STAT_P_WORD_IS_NOT_VALID_TOOL_NUMBER(165,""),
	STAT_D_WORD_IS_MISSING(166,""),
	STAT_D_WORD_IS_INVALID(167,""),
	STAT_E_WORD_IS_MISSING(168,""),
	STAT_E_WORD_IS_INVALID(169,""),

	STAT_H_WORD_IS_MISSING(170,""),
	STAT_H_WORD_IS_INVALID(171,""),
	STAT_L_WORD_IS_MISSING(172,""),
	STAT_L_WORD_IS_INVALID(173,""),
	STAT_Q_WORD_IS_MISSING(174,""),
	STAT_Q_WORD_IS_INVALID(175,""),
	STAT_R_WORD_IS_MISSING(176,""),
	STAT_R_WORD_IS_INVALID(177,""),
	STAT_S_WORD_IS_MISSING(178,""),
	STAT_S_WORD_IS_INVALID(179,""),

	STAT_T_WORD_IS_MISSING(180,""),
	STAT_T_WORD_IS_INVALID(181,""),

	// g2core errors and warnings
	STAT_GENERIC_ERROR(200,""),
	STAT_MINIMUM_LENGTH_MOVE(201,"Move is less than minimum length"),
	STAT_MINIMUM_TIME_MOVE(202,"Move is less than minimum time"),
	STAT_LIMIT_SWITCH_HIT(203,"A limit switch was hit causing shutdown"),
	STAT_COMMAND_REJECTED_BY_ALARM(204,"Command was not processed because machine is alarmed"),
	STAT_COMMAND_REJECTED_BY_SHUTDOWN(205,"Command was not processed because machine is shutdown"),
	STAT_COMMAND_REJECTED_BY_PANIC(206,"Command was not processed because machine is paniced"),
	STAT_KILL_JOB(207,"^d received (job kill)"),
	STAT_NO_GPIO(208,"No GPIO exists for this value"),

	STAT_TEMPERATURE_CONTROL_ERROR(209,"Temperature controls err'd out"),

	STAT_SOFT_LIMIT_EXCEEDED(220,"Soft limit error - axis unspecified"),
	STAT_SOFT_LIMIT_EXCEEDED_XMIN(221,"Soft limit error - X minimum"),
	STAT_SOFT_LIMIT_EXCEEDED_XMAX(222,"Soft limit error - X maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_YMIN(223,"Soft limit error - Y minimum"),
	STAT_SOFT_LIMIT_EXCEEDED_YMAX(224,"Soft limit error - Y maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_ZMIN(225,"Soft limit error - Z minimum"),
	STAT_SOFT_LIMIT_EXCEEDED_ZMAX(226,"Soft limit error - Z maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_AMIN(227,"Soft limit error - A minimum"),
	STAT_SOFT_LIMIT_EXCEEDED_AMAX(228,"Soft limit error - A maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_BMIN(229,"Soft limit error - B minimum"),

	STAT_SOFT_LIMIT_EXCEEDED_BMAX(220,"Soft limit error - B maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_CMIN(231,"Soft limit error - C minimum"),
	STAT_SOFT_LIMIT_EXCEEDED_CMAX(232,"Soft limit error - C maximum"),
	STAT_SOFT_LIMIT_EXCEEDED_ARC(233,"Soft limit err on arc"),

	STAT_HOMING_CYCLE_FAILED(240,"Homing cycle did not complete"),
	STAT_HOMING_ERROR_BAD_OR_NO_AXIS(241,""),
	STAT_HOMING_ERROR_ZERO_SEARCH_VELOCITY(242,""),
	STAT_HOMING_ERROR_ZERO_LATCH_VELOCITY(243,""),
	STAT_HOMING_ERROR_TRAVEL_MIN_MAX_IDENTICAL(244,""),
	STAT_HOMING_ERROR_NEGATIVE_LATCH_BACKOFF(245,""),
	STAT_HOMING_ERROR_HOMING_INPUT_MISCONFIGURED(246,""),
	STAT_HOMING_ERROR_MUST_CLEAR_SWITCHES_BEFORE_HOMING(247,""),
	STAT_ERROR_248(248,""),
	STAT_ERROR_249(249,""),

	STAT_PROBE_CYCLE_FAILED(250,"Probing cycle did not complete"),
	STAT_PROBE_TRAVEL_TOO_SMALL(251,""),
	STAT_NO_PROBE_SWITCH_CONFIGURED(252,""),
	STAT_MULTIPLE_PROBE_SWITCHES_CONFIGURED(253,""),
	STAT_PROBE_SWITCH_ON_ABC_AXIS(254,""),

	STAT_ERROR_255(255,"");

	public static final int SEVERITY_INFO = 1;
	public static final int SEVERITY_ERROR = 2;
	public static final int SEVERITY_WARNING = 4;
	
	/** The integer value of the status */
	private int value;
	/** The label of the status */
	private String label;
	/** Severity level */
	private int severity;

	/**
	 * Constructor
	 * @param value la valeur enti�re de l'erreur
	 * @param label le libell� de l'erreur
	 */
	G2CoreStatusCode(int value, String label){
		this.value = value;
		this.label = label;
		this.severity = SEVERITY_ERROR;
	}

	/**
	 * Constructor
	 * @param value la valeur enti�re de l'erreur
	 * @param label le libell� de l'erreur
	 */
	G2CoreStatusCode(int value, String label, int severity){
		this.value = value;
		this.label = label;
		this.severity = severity;
	}
	
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinyGStatus#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinyGStatus#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * Return the enum value with the given integer value
	 * @param value the value to search
 	 * @return  TinyGStatusCode
	 * @throws GkException GkException
	 */
	public static ITinyGStatus getEnum(int value) throws GkException{
		for (ITinyGStatus statusCode : values()) {
			if(statusCode.getValue() == value){
				return statusCode;
			}
		}
		throw new GkFunctionalException("TinyG Status Code with integer value "+value+" does not exist");
	}

	/**
	 * Return the enum value with the given integer value if it exists
	 * @param value the value to search
 	 * @return  TinyGStatusCode
	 * @throws GkException GkException
	 */
	public static ITinyGStatus findEnum(int value) throws GkException{
		for (ITinyGStatus statusCode : values()) {
			if(statusCode.getValue() == value){
				return statusCode;
			}
		}
		return null;
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinyGStatus#isWarning()
	 */
	@Override
	public boolean isWarning(){
		return severity == SEVERITY_WARNING;
	}
	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.ITinyGStatus#isError()
	 */
	@Override
	public boolean isError(){
		return severity == SEVERITY_ERROR;
	}
}


