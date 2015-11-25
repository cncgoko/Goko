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
package org.goko.controller.tinyg.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;


public class TinyGExecutionToken extends ExecutionToken<ExecutionState>{
	private Object lock;	
	/**
	 * Constructor
	 * @param provider the provider to execute
	 * @throws GkException 
	 */
	public TinyGExecutionToken(IGCodeProvider provider) throws GkException {
		super(provider, ExecutionState.NONE);		
		lock = new Object();
	}

	public void markAsConfirmed(GCodeLine command) throws GkException{
		synchronized (lock) {
			List<GCodeLine> lstSentLine = getLineByState(ExecutionState.SENT);
			GCodeLine gCodeLine = lstSentLine.get(0);
			markAsExecuted(gCodeLine.getId());
		}
	}

	public void markAsSent(Integer idCommand) throws GkException {
		synchronized (lock) {
			super.setLineState(idCommand, ExecutionState.SENT);
		}
	}

	public void markAsExecuted(Integer idCommand) throws GkException {
		synchronized (lock) {
			super.setLineState(idCommand, ExecutionState.EXECUTED);
			updateCompleteState();
		}
	}
	
	protected void updateCompleteState() throws GkException{
		if(CollectionUtils.isNotEmpty(getLineByState(ExecutionState.NONE))){
			int nbErrors 	= CollectionUtils.size(getLineByState(ExecutionState.ERROR));
			int nbExecuted  = CollectionUtils.size(getLineByState(ExecutionState.EXECUTED));
			
			if(nbErrors + nbExecuted == getLineCount()){
				setComplete(true);
			}
		}
	}
	
	plus utilisé. 
	Utiliser un ExecutionToken standard

}
