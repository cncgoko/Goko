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
public enum TinyGStatusCode implements ITinyGStatus {	
	TG_UNKNOWN(-1,""),
	TG_CONFIG_ASSERTION_FAILURE(90,""),
	TG_XIO_ASSERTION_FAILURE(91,""),
	TG_ENCODER_ASSERTION_FAILURE(92,""),
	TG_STEPPER_ASSERTION_FAILURE(93,""),
	TG_PLANNER_ASSERTION_FAILURE(94,""),
	TG_CANONICAL_MACHINE_ASSERTION_FAILURE(95,""),
	TG_CONTROLLER_ASSERTION_FAILURE(96,""),
	TG_STACK_OVERFLOW(97,""),
	TG_MEMORY_FAULT(98,"generic memory corruption detected"),
	TG_GENERIC_ASSERTION_FAILURE(99,"unclassified assertion failure"),
	TG_UNRECOGNIZED_NAME(100,"parser didn't recognize the command"),
	TG_MALFORMED_COMMAND_INPUT(101,"malformed line to parser"),
	TG_BAD_NUMBER_FORMAT(102,"number format error"),
	TG_INPUT_EXCEEDS_MAX_LENGTH(103,"input string too long"),
	TG_INPUT_VALUE_TOO_SMALL(104,"value is under minimum"),
	TG_INPUT_VALUE_TOO_LARGE(105,"value is over maximum"),
	TG_INPUT_VALUE_RANGE_ERROR(106,"value is out-of-range"),
	TG_INPUT_VALUE_UNSUPPORTED(107,"value is not supported"),
	TG_JSON_SYNTAX_ERROR(108,"JSON input string is not well formed"),
	TG_JSON_TOO_MANY_PAIRS(109,"JSON input string has too many pairs"),
	TG_JSON_TOO_LONG(110,"JSON output exceeds buffer size"),
	TG_CONFIG_NOT_TAKEN(111,"config value not taken while in machining cycle"),
	TG_COMMAND_NOT_ACCEPTED(112,"command cannot be accepted at this time"),
	TG_GCODE_GENERIC_INPUT_ERROR(130,"generic error for gcode input"),
	TG_GCODE_COMMAND_UNSUPPORTED(131,"G command is not supported"),
	TG_MCODE_COMMAND_UNSUPPORTED(132,"M command is not supported"),
	TG_GCODE_MODAL_GROUP_VIOLATION(133,"gcode modal group error"),
	TG_GCODE_AXIS_IS_MISSING(134,"requires at least one axis present"),
	TG_GCODE_AXIS_CANNOT_BE_PRESENT(135,"error if G80 has axis words"),
	TG_GCODE_AXIS_IS_INVALID(136,"axis specified that�s illegal for command"),
	TG_GCODE_AXIS_IS_NOT_CONFIGURED(137,"WARNING: attempt to program an axis that is disabled"),
	TG_GCODE_AXIS_NUMBER_IS_MISSING(138,"axis word is missing its value"),
	TG_GCODE_AXIS_NUMBER_IS_INVALID(139,"axis word value is illegal"),
	TG_GCODE_ACTIVE_PLANE_IS_MISSING(140,"active plane is not programmed"),
	TG_GCODE_ACTIVE_PLANE_IS_INVALID(141,"active plane selected not valid for this command"),
	TG_GCODE_FEEDRATE_NOT_SPECIFIED(142,"move has no feedrate"),
	TG_GCODE_INVERSE_TIME_MODE_CANNOT_BE_USED(143,"G38.2 and some canned cycles cannot accept inverse time mode"),
	TG_GCODE_ROTARY_AXIS_CANNOT_BE_USED(144,"G38.2 and some other commands cannot have rotary axes"),
	TG_GCODE_G53_WITHOUT_G0_OR_G1(145,"G0 or G1 must be active for G53"),
	TG_REQUESTED_VELOCITY_EXCEEDS_LIMITS(146,""),
	TG_CUTTER_COMPENSATION_CANNOT_BE_ENABLED(147,""),
	TG_PROGRAMMED_POINT_SAME_AS_CURRENT_POINT(148,""),
	TG_SPINDLE_SPEED_BELOW_MINIMUM(149,""),
	TG_SPINDLE_SPEED_MAX_EXCEEDED(150,""),
	TG_S_WORD_IS_MISSING(151,""),
	TG_S_WORD_IS_INVALID(152,""),
	TG_SPINDLE_MUST_BE_OFF(153,""),
	TG_SPINDLE_MUST_BE_TURNING(154,"some canned cycles require spindle to be turning when called"),
	TG_ARC_SPECIFICATION_ERROR(155,"generic arc specification error"),
	TG_ARC_AXIS_MISSING_FOR_SELECTED_PLANE(156,"arc missing axis (axes) required by selected plane"),
	TG_ARC_OFFSETS_MISSING_FOR_SELECTED_PLANE(157,"one or both offsets are not specified"),
	TG_ARC_RADIUS_OUT_OF_TOLERANCE(158,"WARNING - radius arc is too large - accuracy in question"),
	TG_ARC_ENDPOINT_IS_STARTING_POINT(159,""),
	TG_P_WORD_IS_MISSING(160,"P must be present for dwells and other functions"),
	TG_P_WORD_IS_INVALID(161,"generic P value error"),
	TG_P_WORD_IS_ZERO(162,""),
	TG_P_WORD_IS_NEGATIVE(163,"dwells require positive P values"),
	TG_P_WORD_IS_NOT_AN_INTEGER(164,"G10s and other commands require integer P numbers"),
	TG_P_WORD_IS_NOT_VALID_TOOL_NUMBER(165,""),
	TG_D_WORD_IS_MISSING(166,""),
	TG_D_WORD_IS_INVALID(167,""),
	TG_E_WORD_IS_MISSING(168,""),
	TG_E_WORD_IS_INVALID(169,""),
	TG_H_WORD_IS_MISSING(170,""),
	TG_H_WORD_IS_INVALID(171,""),
	TG_L_WORD_IS_MISSING(172,""),
	TG_L_WORD_IS_INVALID(173,""),
	TG_Q_WORD_IS_MISSING(174,""),
	TG_Q_WORD_IS_INVALID(175,""),
	TG_R_WORD_IS_MISSING(176,""),
	TG_R_WORD_IS_INVALID(177,""),
	TG_T_WORD_IS_MISSING(178,""),
	TG_T_WORD_IS_INVALID(179,""),
	TG_GENERIC_ERROR(200,""),
	TG_MINIMUM_LENGTH_MOVE(201,"move is less than minimum length", TinyGStatusCode.SEVERITY_WARNING),
	TG_MINIMUM_TIME_MOVE(202,"move is less than minimum time", TinyGStatusCode.SEVERITY_WARNING),
	TG_MACHINE_ALARMED(203,"machine is alarmed. Command not processed"),
	TG_LIMIT_SWITCH_HIT(204,"limit switch was hit causing shutdown"),
	TG_PLANNER_FAILED_TO_CONVERGE(205,"planner can throw this exception"),
	TG_SOFT_LIMIT_EXCEEDED(220,"soft limit error - axis unspecified"),
	TG_SOFT_LIMIT_EXCEEDED_XMIN(221,"soft limit error - X minimum"),
	TG_SOFT_LIMIT_EXCEEDED_XMAX(222,"soft limit error - X maximum"),
	TG_SOFT_LIMIT_EXCEEDED_YMIN(223,"soft limit error - Y minimum"),
	TG_SOFT_LIMIT_EXCEEDED_YMAX(224,"soft limit error - Y maximum"),
	TG_SOFT_LIMIT_EXCEEDED_ZMIN(225,"soft limit error - Z minimum"),
	TG_SOFT_LIMIT_EXCEEDED_ZMAX(226,"soft limit error - Z maximum"),
	TG_SOFT_LIMIT_EXCEEDED_AMIN(227,"soft limit error - A minimum"),
	TG_SOFT_LIMIT_EXCEEDED_AMAX(228,"soft limit error - A maximum"),
	TG_SOFT_LIMIT_EXCEEDED_BMIN(229,"soft limit error - B minimum"),
	TG_SOFT_LIMIT_EXCEEDED_BMAX(230,"soft limit error - B maximum"),
	TG_SOFT_LIMIT_EXCEEDED_CMIN(231,"soft limit error - C minimum"),
	TG_SOFT_LIMIT_EXCEEDED_CMAX(232,"soft limit error - C maximum"),
	TG_HOMING_CYCLE_FAILED_240(240,"homing cycle did not complete"),
	TG_HOMING_ERROR_BAD_OR_NO_AXIS(241,""),
	TG_HOMING_ERROR_ZERO_SEARCH_VELOCITY(242,""),
	TG_HOMING_ERROR_ZERO_LATCH_VELOCITY(243,""),
	TG_HOMING_ERROR_TRAVEL_MIN_MAX_IDENTICAL(244,""),
	TG_HOMING_ERROR_NEGATIVE_LATCH_BACKOFF(245,""),
	TG_HOMING_ERROR_SWITCH_MISCONFIGURATION(246,""),
	TG_PROBE_CYCLE_FAILED(250,"probing cycle did not complete"),
	TG_PROBE_ENDPOINT_IS_STARTING_POINT(251,""),
	TG_JOGGING_CYCLE_FAILED(252,"jogging cycle did not complete"),
	TG_OK(0,"universal OK code", TinyGStatusCode.SEVERITY_INFO),
	TG_ERROR(1,"generic error return"),
	TG_EAGAIN(2,"function would block here"),
	TG_NOOP(3,"function had no-operation"),
	TG_COMPLETE(4,"operation is complete"),
	TG_TERMINATE(5,"operation terminated (gracefully)"),
	TG_RESET(6,"operation was hard reset (sig kill)"),
	TG_EOL(7,"returned end-of-line"),
	TG_EOF(8,"returned end-of-file"),
	TG_FILE_NOT_OPEN(9,""),
	TG_FILE_SIZE_EXCEEDED(10,""),
	TG_NO_SUCH_DEVICE(11,""),
	TG_BUFFER_EMPTY(12,""),
	TG_BUFFER_FULL(13,""),
	TG_BUFFER_FULL_FATAL(14,""),
	TG_INITIALIZING(15,"initializing - not ready for use"),
	TG_ENTERING_BOOT_LOADER(16,"emitted by boot loader, not TinyG"),
	TG_FUNCTION_IS_STUBBED(17,""),
	TG_INTERNAL_ERROR(20,"unrecoverable internal error"),
	TG_INTERNAL_RANGE_ERROR(21,""),
	TG_FLOATING_POINT_ERROR(22,"number conversion error"),
	TG_DIVIDE_BY_ZERO(23,""),
	TG_INVALID_ADDRESS(24,""),
	TG_READ_ONLY_ADDRESS(25,""),
	TG_INIT_FAIL(26,""),
	TG_ALARMED(27,""),
	TG_FAILED_TO_GET_PLANNER_BUFFER(28,""),
	TG_GENERIC_EXCEPTION_REPORT(29,"used for test"),
	TG_PREP_LINE_MOVE_TIME_IS_INFINITE(30,""),
	TG_PREP_LINE_MOVE_TIME_IS_NAN(31,""),
	TG_FLOAT_IS_INFINITE(32,""),
	TG_FLOAT_IS_NAN(33,""),
	TG_PERSISTENCE_ERROR(34,""),
	TG_BAD_STATUS_REPORT_SETTING(35,"");

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
	TinyGStatusCode(int value, String label){
		this.value = value;
		this.label = label;
		this.severity = SEVERITY_ERROR;
	}

	/**
	 * Constructor
	 * @param value la valeur enti�re de l'erreur
	 * @param label le libell� de l'erreur
	 */
	TinyGStatusCode(int value, String label, int severity){
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


