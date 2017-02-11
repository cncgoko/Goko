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
package org.goko.controller.g2core.controller;

import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.AbstractTinyGState;
import org.goko.core.common.exception.GkException;

/**
 * Storage of the internal state of the TinyG board
 * 
 * @author PsyKo
 */
public class G2CoreState extends AbstractTinyGState{
	/** 
	 * Constructor 
	 * @throws GkException GkException 
	 */
	public G2CoreState() throws GkException {
		super();		
	}


	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.commons.AbstractTinyGState#initializeDefaultValue()
	 */
	@Override
	protected void initializeDefaultValue() throws GkException{
		super.initializeDefaultValue();
		storeValue(G2Core.AVAILABLE_BUFFER_COUNT, "G2Core Buffer", "The available space in the planner buffer", 0);
		storeValue(G2Core.MESSAGE, "Msg", "Last received message", StringUtils.EMPTY);
	}

	/**
	 * @return the availableBuffer
	 * @throws GkException GkException
	 */
	public int getAvailableBuffer() throws GkException {
		return getIntegerValue(G2Core.AVAILABLE_BUFFER_COUNT).getValue();
	}

	/**
	 * @param availableBuffer the availableBuffer to set
	 * @throws GkException GkException
	 */
	public void setAvailableBuffer(int availableBuffer) throws GkException {
		updateValue(G2Core.AVAILABLE_BUFFER_COUNT, availableBuffer);
	}


	/**
	 * Set the last received message 
	 * @param message the message
	 * @throws GkException GkException 
	 */
	public void setMessage(String message) throws GkException{
		updateValue(G2Core.MESSAGE, message);
	}
	
	/**
	 * @return the availableBuffer
	 * @throws GkException GkException
	 */
	public int getMessage() throws GkException {
		return getIntegerValue(G2Core.MESSAGE).getValue();
	}
}
