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
package org.goko.core.common.applicative.logging;

import java.util.Date;

public class ApplicativeLogEvent {
	/**
	 * An error message (Value 1).
	 */
	public static final int	LOG_ERROR	= 1;
	/**
	 * A warning message (Value 2).
	 */
	public static final int	LOG_WARNING	= 2;
	/**
	 * An informational message (Value 3).
	 */
	public static final int	LOG_INFO	= 3;
	/**
	 * A debugging message (Value 4).
	 */
	public static final int	LOG_DEBUG	= 4;

	/**
	 * The severity of the message
	 */
	private int severity;
	/**
	 * The message itself
	 */
	private String message;
	/**
	 * The source of the message
	 */
	private String source;
	/**
	 * The date of the message
	 */
	private Date date;
	/**
	 * Constructor
	 * @param severity the severity
	 * @param message the message
	 * @param source the source
	 */
	public ApplicativeLogEvent(int severity, String message, String source) {
		super();
		this.severity = severity;
		this.message = message;
		this.source = source;
		this.date = new Date();
	}
	/**
	 * @return the severity
	 */
	public int getSeverity() {
		return severity;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
