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
package org.goko.controller.grbl.v11;

import org.goko.controller.grbl.commons.IGrblStatus;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;

/**
 * TinyG Status code enumeration
 *
 * @see https://github.com/synthetos/TinyG/wiki/TinyG-Status-Codes
 * @author PsyKo
 *
 */
public enum GrblGStatusCode implements IGrblStatus {
	UNKNOWN_ERROR(-1,"Unknown error occured"),
	EXPECTED_LETTER(1,"G-code words consist of a letter and a value. Letter was not found."),
	BAD_NUMBER_FORMAT(2,"Missing the expected G-code word value or numeric value format is not valid."),
	INVALID_STATEMENT(3,"Grbl '$' system command was not recognized or supported."),
	NEGATIVE_VALUE(4,"Negative value received for an expected positive value."),
	SETTING_DISABLED(5,"Homing cycle failure. Homing is not enabled via settings."),
	MINIMUM_STEP(6,"Minimum step pulse time must be greater than 3usec."),
	EEPROM_READ_FAIL(7,"Using defaults,An EEPROM read failed. Auto-restoring affected EEPROM to default values."),
	NOT_IDLE(8,"Grbl '$' command cannot be used unless Grbl is IDLE. Ensures smooth operation during a job."),
	GCODE_LOCK(9,"G-code commands are locked out during alarm or jog state."),
	HOMING_DISABLED(10,"Soft limits cannot be enabled without homing also enabled."),
	LINE_OVERFLOW(11,"Max characters per line exceeded. Received command line was not executed."),
	STEP_RATE(12,"Grbl '$' setting value cause the step rate to exceed the maximum supported."),
	CHECK_DOOR(13,"Safety door detected as opened and door state initiated."),
	LINE_LENGTH_EXCEEDED(14,"Build info or startup line exceeded EEPROM line length limit. Line not stored."),
	TRAVEL_EXCEEDED(15,"Jog target exceeds machine travel. Jog command has been ignored."),
	INVALID_JOG(16,"Jog command has no '=' or contains prohibited g-code."),
	LASER_DISABLED(17,"Laser mode requires PWM output."),
	UNSUPPORTED_COMMAND(20,"Unsupported or invalid g-code command found in block."),
	MODAL_GROUP_VIOLATION(21,"More than one g-code command from same modal group found in block."),
	UNDEFINED_FEED_RATE(22,"Feed rate has not yet been set or is undefined."),
	INVALID_GCODE_23(23,"G-code command in block requires an integer value."),
	INVALID_GCODE_24(24,"More than one g-code command that requires axis words found in block."),
	INVALID_GCODE_25(25,"Repeated g-code word found in block."),
	INVALID_GCODE_26(26,"No axis words found in block for g-code command or current modal state which requires them."),
	INVALID_GCODE_27(27,"Line number value is invalid."),
	INVALID_GCODE_28(28,"G-code command is missing a required value word."),
	INVALID_GCODE_29(29,"G59.x work coordinate systems are not supported."),
	INVALID_GCODE_30(30,"G53 only allowed with G0 and G1 motion modes."),
	INVALID_GCODE_31(31,"Axis words found in block when no command or current modal state uses them."),
	INVALID_GCODE_32(32,"G2 and G3 arcs require at least one in-plane axis word."),
	INVALID_GCODE_33(33,"Motion command target is invalid."),
	INVALID_GCODE_34(34,"Arc radius value is invalid."),
	INVALID_GCODE_35(35,"G2 and G3 arcs require at least one in-plane offset word."),
	INVALID_GCODE_36(36,"Unused value words found in block."),
	INVALID_GCODE_37(37,"G43.1 dynamic tool length offset is not assigned to configured tool length axis."),
	INVALID_GCODE_38(38,"Tool number greater than max supported value.");

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
	GrblGStatusCode(int value, String label){
		this.value = value;
		this.label = label;
		this.severity = SEVERITY_ERROR;
	}

	/**
	 * Constructor
	 * @param value la valeur enti�re de l'erreur
	 * @param label le libell� de l'erreur
	 */
	GrblGStatusCode(int value, String label, int severity){
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
	public static IGrblStatus getEnum(int value) throws GkException{
		for (IGrblStatus statusCode : values()) {
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
	 */
	public static IGrblStatus findEnum(int value){
		for (IGrblStatus statusCode : values()) {
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


