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
package org.goko.controller.grbl.v08.configuration;

import java.util.regex.Pattern;

/**
 * Grbl setting represented by a double value
 *
 * @author PsyKo
 *
 */
public class GrblDoubleSetting extends GrblSetting<Double> {

	GrblDoubleSetting(String identifier, Double value) {
		super(identifier, value, Double.class);
	}

	/**
	 * (inheritDoc)
	 *
	 * @see org.goko.controller.grbl.v08.configuration.GrblSetting#setValueFromString(java.lang.String)
	 */
	@Override
	public void setValueFromString(String value) {
		final String digits = "(\\p{Digit}+)";
		final String hexDigits = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally
		// signed decimal integer.
		final String exp = "[eE][+-]?" + digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading
													// "whitespace"
				"[+-]?(" + // Optional sign character
				"NaN|" + // "NaN" string
				"Infinity|" + // "Infinity" string

				// A decimal floating-point string representing a finite
				// positive
				// number without a leading sign has at most five basic pieces:
				// Digits . Digits ExponentPart FloatTypeSuffix
				//
				// Since this method allows integer-only strings as input
				// in addition to strings of floating-point literals, the
				// two sub-patterns below are simplifications of the grammar
				// productions from the Java Language Specification, 2nd
				// edition, section 3.10.2.

				// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
				"(((" + digits + "(\\.)?(" + digits + "?)(" + exp + ")?)|" +

		// . Digits ExponentPart_opt FloatTypeSuffix_opt
				"(\\.(" + digits + ")(" + exp + ")?)|" +

				// Hexadecimal strings
				"((" +
				// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + hexDigits + "(\\.)?)|" +

				// 0[xX] HexDigits_opt . HexDigits BinaryExponent
				// FloatTypeSuffix_opt
				"(0[xX]" + hexDigits + "?(\\.)" + hexDigits + ")" +

				")[pP][+-]?" + digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional
																				// trailing
																				// "whitespace"

		if (Pattern.matches(fpRegex, value)) {
			setValue(Double.valueOf(value)); // Will not throw NumberFormatException
		} else {
			// Perform suitable alternative action
		}
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v08.configuration.GrblSetting#getValueAsString()
	 */
	@Override
	public String getValueAsString() {
		return String.valueOf(getValue());
	}

}
