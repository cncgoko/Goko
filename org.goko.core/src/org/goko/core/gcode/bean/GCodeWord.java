/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.bean;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class GCodeWord {
	private String stringValue;

	/**
	 * @param value
	 */
	public GCodeWord(String value) {
		super();
		this.stringValue = value;
	}


	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GCodeWord other = (GCodeWord) obj;
		if (stringValue == null) {
			if (other.stringValue != null) {
				return false;
			}
		} else if (!stringValue.equals(other.stringValue)) {
			return false;
		}
		return true;
	}


	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}


	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}


	/**
	 * @return the letter
	 */
	public String getLetter() {
		return StringUtils.substring(stringValue,0,1);
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return new BigDecimal(StringUtils.substring(stringValue,1));
	}

	@Override
	public String toString() {
		return getStringValue();
	}
}
