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
package org.goko.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GkLog {
	protected Logger logger;

	private GkLog(Logger logger){
		this.logger = logger;
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}
	/**
	 * Error logging
	 * @param e the exception to log
	 */
	public void error(Exception e) {
		logger.error("Error occured :", e);		
	}

	public static GkLog getLogger(Class<?> clazz) {
		return new GkLog(LoggerFactory.getLogger(clazz));
	}
}
