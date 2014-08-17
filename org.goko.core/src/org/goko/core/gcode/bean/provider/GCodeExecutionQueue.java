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
package org.goko.core.gcode.bean.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;

/**
 * Implementation of a {@link IGCodeProvider} for execution planner
 *
 * @author PsyKo
 *
 */
public class GCodeExecutionQueue  extends EventDispatcher implements IGCodeProvider {
	/** The commands */
	private List<GCodeCommand> lstCommands;
	/**
	 * The current command index
	 */
	private int currentIndex;
	/**
	 * The count of command to send
	 */
	private int commandCount;
	/**
	 * Boolean indicating if the queue is being executed
	 */
	private boolean started;

	/**
	 * Constructor.
	 * Creates an execution queue from the given provider
	 * @param provider the provider to get command from
	 */
	public GCodeExecutionQueue(IGCodeProvider provider) {
		super();
		init(provider.getGCodeCommands());
	}

	/**
	 * Constructor
	 * @param lstCommands
	 */
	public GCodeExecutionQueue(List<GCodeCommand> lstCommands) {
		super();
		init(lstCommands);
	}

	/**
	 * Initialize this bean with the given list
	 * @param lstCommands the list of commands
	 */
	private void init(List<GCodeCommand> lstCommands){
		this.lstCommands = Collections.synchronizedList(new ArrayList<GCodeCommand>(lstCommands));
		this.currentIndex = -1;
		this.commandCount = CollectionUtils.size(this.lstCommands);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getGCodeCommands()
	 */
	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return lstCommands;
	}

	/**
	 * Determine if this execution queue has more command to execute
	 * @return <code>true</code> if there is one or more commands remaining, <code>false</code> otherwise
	 */
	public synchronized boolean hasNext(){
		return isStarted() && this.currentIndex < commandCount - 1;
	}

	/**
	 * Remove the next command from this execution queue
	 * @return the next {@link GCodeCommand} to execute
	 * @throws GkException GkException
	 */
	public synchronized GCodeCommand unstackNextCommand() throws GkException{
		if(hasNext()){
			currentIndex = currentIndex+1;
			return lstCommands.get(currentIndex);
		}
		throw new GkTechnicalException("No more command available...");
	}
	/**
	 * Return the next command from this execution queue without removing it
	 * @return the next {@link GCodeCommand} to execute
	 * @throws GkException GkException
	 */
	public synchronized GCodeCommand getNextCommand() throws GkException{
		if(hasNext()){
			return lstCommands.get(currentIndex + 1);
		}
		throw new GkTechnicalException("No more command available...");
	}

	/**
	 * Notify a state change for the given command
	 * @param command the {@link GCodeCommand}
	 * @param state the new state
	 * @throws GkException GkException
	 */
	public void setCommandState(GCodeCommand command, int state) throws GkException{
		notifyListeners(new GCodeCommandExecutionEvent(this, command, state));
	}

	public long getEstimatedExecutionTime(){
		return 0;
	}

	public long getRunningExecutionTime(){
		return 0;
	}

	/**
	 * @return the commandCount
	 */
	public int getCommandCount() {
		return commandCount;
	}

	public int getExecutedCommandCount(){
		return currentIndex;
	}


	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}
	/**
	 *  Indicates the the queue is being executed
	 */
	public void start(){
		started = true;
	}
	/**
	 * Stops the execution of the queue. No additional commands will be available
	 */
	public void stop(){
		started = false;
	}
}
