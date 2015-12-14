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
package org.goko.core.common.exception;

/**
 * Technical exception
 * @author PsyKo
 *
 */
public class GkTechnicalException extends GkException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1125645831362340170L;
	/** The message of the exception */ 
	private String message;
	
	/**
	 * Constructor
	 * @param message the message
	 */
	public GkTechnicalException(String message){
		super();
		this.message = message;
	}
	/**
	 * Constructor
	 * @param t Throwable
	 */
	public GkTechnicalException(Throwable t) {
		super(t);		
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
}
