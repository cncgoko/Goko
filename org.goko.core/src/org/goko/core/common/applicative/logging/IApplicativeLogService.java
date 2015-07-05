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
package org.goko.core.common.applicative.logging;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * Defines an applicative log service (use to notify error to the end user)
 * @author PsyKo
 *
 */
public interface IApplicativeLogService {
	/**
	 * Log a Log event with no source
	 * @param severity the severity
	 * @param message the message
	 */
	void log(int severity, String message);
	/**
	 * Log a complete Log event
	 * @param severity the severity
	 * @param message the message
	 * @param source the source
	 */
	void log(int severity, String message, String source);
	/**
	 * Log a complete Log event
	 * @param severity the severity
	 * @param message the message
	 * @param source the source
	 */
	void error(String message, String source);
	/**
	 * Log an warning event
	 * @param message the message
	 * @param source the source
	 */
	void warning(String message, String source);
	/**
	 * Log an info event
	 * @param message the message
	 * @param source the source
	 */
	void info(String message, String source);
	/**
	 * Registers the given listener
	 * @param listener the listener to register
	 */
	void registerApplicativeLogListener(IApplicativeLogListener listener);

	/**
	 * Unregisters the given listener
	 * @param listener the listener to unregister
	 */
	void unregisterApplicativeLogListener(IApplicativeLogListener listener);
	
	/**
	 * Return the list of all logged events so far
	 * @return a list of ApplicativeLogEvent
	 * @throws GkException GkException
	 */
	List<ApplicativeLogEvent> getEvents() throws GkException; 
	
	/**
	 * Clear the list of all logged events so far
	 * @throws GkException GkException
	 */
	void clearEvents() throws GkException; 
}
