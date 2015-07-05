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

import org.goko.core.common.i18n.MessageResource;

/**
 * Goko functional exception
 * 
 * @author PsyKo
 *
 */
public class GkFunctionalException extends GkException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2648838559567768949L;
	/** Key for the exception message */
	private String key;
	/** arguments of the message */
	private String[] arguments;
		
	public GkFunctionalException(String key, String... args ){
		super();
		this.key = key;
		this.arguments = args;
	}

	@Override
	public String getLocalizedMessage() {
		return MessageResource.getMessage(key);		
	}
	
	@Override
	public String getMessage() {		
		return getLocalizedMessage();
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * @return the arguments
	 */
	public String[] getArguments() {
		return arguments;
	}


	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
}
