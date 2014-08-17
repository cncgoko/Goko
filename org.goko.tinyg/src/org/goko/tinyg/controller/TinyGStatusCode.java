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
package org.goko.tinyg.controller;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

/**
 * TinyG Status code enumeration
 *
 * @see https://github.com/synthetos/TinyG/wiki/TinyG-Status-Codes
 * @author PsyKo
 *
 */
public enum TinyGStatusCode {
	TG_OK						(	0	,	"OK"),
	TG_ERROR					(	1	,	"generic error return (EPERM)"),
	TG_EAGAIN					(	2	,	"function would block here (call again)"	),
	TG_NOOP						(	3	,	"function had no-operation"	),
	TG_COMPLETE					(	4	,	"operation is complete"	),
	TG_TERMINATE				(	5	,	"operation terminated (gracefully)"	),
	TG_RESET					(	6	,	"operation was hard reset (sig kill)"	),
	TG_EOL						(	7	,	"function returned end-of-line or end-of-message"	),
	TG_EOF						(	8	,	"function returned end-of-file"	),
	TG_FILE_NOT_OPEN			(	9	,	""	),
	TG_FILE_SIZE_EXCEEDED		(	10	,	""	),
	TG_NO_SUCH_DEVICE			(	11	,	""	),
	TG_BUFFER_EMPTY				(	12	,	""	),
	TG_BUFFER_FULL				(	13	,	""	),
	TG_BUFFER_FULL_FATAL		(	14	,	""	),
	TG_INITIALIZING				(	15	,	"initializing - not ready for use"	),
	TG_ENTERING_BOOT_LOADER		(	16	,	"entering boot loader from application"	),
	TG_INTERNAL_ERROR			(	20	,	"unrecoverable internal error"	),
	TG_INTERNAL_RANGE_ERROR		(	21	,	"number range error other than by user input"	),
	TG_FLOATING_POINT_ERROR		(	22	,	"number conversion error"	),
	TG_DIVIDE_BY_ZERO			(	23	,	""	),
	TG_INVALID_ADDRESS			(	24	,	""	),
	TG_READ_ONLY_ADDRESS		(	25	,	""	),
	TG_INIT_FAIL				(	26	,	"Initialization failure"	),
	TG_SHUTDOWN					(	27	,	"System alarmed and went into shutdown"	),
	TG_MEMORY_FAULT				(	28	,	"Memory fault or corruption detected"	),
	TG_UNRECOGNIZED_COMMAND		(	40	,	"parser didn't recognize the command"	),
	TG_EXPECTED_COMMAND_LETTER	(	41	,	"malformed line to parser"	),
	TG_BAD_NUMBER_FORMAT		(	42	,	"number format error"	),
	TG_INPUT_EXCEEDS_MAX_LENGTH	(	43	,	"input string is too long"	),
	TG_INPUT_VALUE_TOO_SMALL	(	44	,	"value is under minimum for this parameter"	),
	TG_INPUT_VALUE_TOO_LARGE	(	45	,	"value is over maximum for this parameter"	),
	TG_INPUT_VALUE_RANGE_ERROR	(	46	,	"input error: value is out-of-range for this parameter"	),
	TG_INPUT_VALUE_UNSUPPORTED	(	47	,	"input error: value is not supported for this parameter"	),
	TG_JSON_SYNTAX_ERROR		(	48	,	"JSON string is not well formed"	),
	TG_JSON_TOO_MANY_PAIRS		(	49	,	"JSON string or has too many name:value pairs"	),
	TG_JSON_TOO_LONG			(	50	,	"JSON output string too long for output buffer"	),
	TG_NO_BUFFER_SPACE			(	51	,	"Buffer pool is full and cannot perform this operation"	),
	TG_MINIMUM_LENGTH_MOVE_ERROR(	60	,	"move is below minimum length or zero"	),
	TG_MINIMUM_TIME_MOVE_ERROR	(	61	,	"move is below minimum time or zero"	),
	TG_GCODE_BLOCK_SKIPPED		(	62	,	"block was skipped - usually because it was is too short"	),
	TG_GCODE_INPUT_ERROR		(	63	,	"general error for gcode input"	),
	TG_GCODE_FEEDRATE_ERROR		(	64	,	"no feedrate specified"	),
	TG_GCODE_AXIS_WORD_MISSING	(	65	,	"command requires at least one axis present"	),
	TG_MODAL_GROUP_VIOLATION	(	66	,	"gcode modal group error"	),
	TG_HOMING_CYCLE_FAILED		(	67	,	"homing cycle did not complete"	),
	TG_MAX_TRAVEL_EXCEEDED		(	68	,	""	),
	TG_MAX_SPINDLE_SPEED_EXCEEDED(	69	,	""	),
	TG_ARC_SPECIFICATION_ERROR	(	70	,	""	);

	/** The integer value of the status */
	private int value;
	/** The label of the status */
	private String label;

	/**
	 * Constructor
	 * @param value la valeur entière de l'erreur
	 * @param label le libellé de l'erreur
	 */
	TinyGStatusCode(int value, String label){
		this.value = value;
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Return the enum value with the given integer value
	 * @param value the value to search
 	 * @return  TinyGStatusCode
	 * @throws GkException GkException
	 */
	public static TinyGStatusCode getEnum(int value) throws GkException{
		for (TinyGStatusCode statusCode : values()) {
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
	public static TinyGStatusCode findEnum(int value) throws GkException{
		for (TinyGStatusCode statusCode : values()) {
			if(statusCode.getValue() == value){
				return statusCode;
			}
		}
		return null;
	}
}


