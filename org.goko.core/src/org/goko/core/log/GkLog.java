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

import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.i18n.MessageResource;
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
	
	public void warn(String msg) {
		logger.warn(msg);
	}

	public void debug(String msg) {
		logger.debug(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}
	
	public void log(GkTechnicalException exception) {
		logger.error(exception.getMessage());
	}
	
//	public void info(IStatus status){
//		info(status.getMessage());
//		if(status.getChildren() != null && status.getChildren().length > 0){
//			for (IStatus child : status.getChildren()) {
//				info(child);
//			}
//		}
//	}
//	
//	public void error(IStatus status){
//		error(status.getMessage());
//		if(status.getChildren() != null && status.getChildren().length > 0){
//			for (IStatus child : status.getChildren()) {
//				error(child);
//			}
//		}
//	}
//	
//	public void warn(IStatus status){
//		warn(status.getMessage());
//		if(status.getChildren() != null && status.getChildren().length > 0){
//			for (IStatus child : status.getChildren()) {
//				warn(child);
//			}
//		}
//	}
	
	public void log(GkFunctionalException exception) {
		logger.warn(MessageResource.getMessage(exception.getKey()), exception.getArguments());
	}
	/**
	 * Error logging
	 * @param e the exception to log
	 */
	public void error(Exception e) {
		logger.error("Error occured : "+e.getMessage(), e);		
	}

	/**
	 * Error logging
	 * @param e the exception to log
	 */
	public void error(Throwable e) {
		logger.error("Unexpected error occured :", e);		
	}
	
	public static GkLog getLogger(Class<?> clazz) {
		MessageResource.getInstance();
		return new GkLog(LoggerFactory.getLogger(clazz));
	}
}
