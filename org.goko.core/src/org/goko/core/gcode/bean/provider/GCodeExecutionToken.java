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
package org.goko.core.gcode.bean.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.execution.IGCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;

/**
 * Implementation of a {@link IGCodeProvider} for execution planner
 *
 * @author PsyKo
 *
 */
public class GCodeExecutionToken  extends EventDispatcher implements IGCodeExecutionToken {
	/** Id of the GCodeProvider */
	private Integer id;
	/** The name of this provider */
	private String name;
	/** The bounds of this provider */
	private BoundingTuple6b bounds;
	/** the list of commands */
	protected List<GCodeCommand> lstCommands;
	/** The map of commands by Id */
	protected Map<Integer, GCodeCommand> mapCommandById;
	/** The map of executed commands */
	protected List<Integer> mapExecutedCommandById;
	/** The map of errors commands */
	protected List<Integer> mapErrorsCommandById;
	/** The map of state by command id*/
	protected Map<Integer, Integer> mapStateByIdCommand;
	/** The list of commands */
	protected List<Integer> commands;
	/** The current command index */
	protected int currentIndex;
	/** Completed state  */
	protected boolean complete;
	/** The monitor service */
	protected IGCodeExecutionMonitorService monitorService;
	/** Paused state */
	protected boolean paused;
	/**
	 * Constructor
	 * @param provider the provider to build this execution token from
	 */
	public GCodeExecutionToken(IGCodeProvider provider) {
		this.name = provider.getName();
		this.mapCommandById 		= new HashMap<Integer, GCodeCommand>();
		this.mapStateByIdCommand 	= new HashMap<Integer, Integer>();
		this.mapExecutedCommandById = new ArrayList<Integer>();
		this.mapErrorsCommandById 	= new ArrayList<Integer>();
		this.bounds = provider.getBounds();
		this.currentIndex = -1;
		this.lstCommands = new ArrayList<GCodeCommand>(provider.getGCodeCommands());
		this.commands = new ArrayList<Integer>();

		if(CollectionUtils.isNotEmpty(provider.getGCodeCommands())){
			for (GCodeCommand gCodeCommand : provider.getGCodeCommands()) {
				this.commands.add(gCodeCommand.getId());
				this.mapCommandById.put(gCodeCommand.getId(), gCodeCommand);
			}
		}
	}

	/**
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getGCodeCommands()
	 */
	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return lstCommands;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getBounds()
	 */
	@Override
	public BoundingTuple6b getBounds() {
		return bounds;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#getCommandCount()
	 */
	@Override
	public int getCommandCount() throws GkException {
		return commands.size();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#markAsExecuted(java.lang.Integer)
	 */
	@Override
	public void markAsExecuted(Integer idCommand) throws GkException {
		GCodeCommand command = mapCommandById.get(idCommand);
		mapExecutedCommandById.add(command.getId());
		mapStateByIdCommand.put(command.getId(), GCodeCommandState.EXECUTED);
		notifyListeners(new GCodeCommandExecutionEvent(this, command, GCodeCommandState.EXECUTED));
		monitorService.notifyCommandStateChanged(this, idCommand);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#getExecutedCommandCount()
	 */
	@Override
	public int getExecutedCommandCount() throws GkException {
		return mapExecutedCommandById.size();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#markAsError(java.lang.Integer)
	 */
	@Override
	public void markAsError(Integer idCommand) throws GkException {
		GCodeCommand command = mapCommandById.get(idCommand);
		mapErrorsCommandById.add(command.getId());
		mapStateByIdCommand.put(command.getId(), GCodeCommandState.ERROR);
		notifyListeners(new GCodeCommandExecutionEvent(this, command, GCodeCommandState.ERROR));
		monitorService.notifyCommandStateChanged(this, idCommand);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#getErrorCommandCount()
	 */
	@Override
	public int getErrorCommandCount() throws GkException {
		return mapErrorsCommandById.size();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#getCommandState(java.lang.Integer)
	 */
	@Override
	public GCodeCommandState getCommandState(Integer idCommand) throws GkException {
		if(mapStateByIdCommand.containsKey(idCommand)){
			return new GCodeCommandState(mapStateByIdCommand.get(idCommand));
		}
		return new GCodeCommandState(GCodeCommandState.NONE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#getNextCommand()
	 */
	@Override
	public GCodeCommand getNextCommand() throws GkException {
		return mapCommandById.get(commands.get(currentIndex + 1));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#takeNextCommand()
	 */
	@Override
	public GCodeCommand takeNextCommand() throws GkException {
		currentIndex = currentIndex + 1;
		Integer nextId = commands.get(currentIndex);
		return mapCommandById.get(nextId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#hasMoreCommand()
	 */
	@Override
	public boolean hasMoreCommand() throws GkException {
		return getCommandCount() > currentIndex + 1;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#beginExecution()
	 */
	@Override
	public void beginExecution() throws GkException {
		if(isMonitorService()){
			getMonitorService().notifyExecutionStart(this);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.execution.IGCodeExecutionToken#endExecution()
	 */
	@Override
	public void endExecution() throws GkException {
		if(isMonitorService()){
			if(isComplete()){
				getMonitorService().notifyExecutionComplete(this);				
			}else{
				getMonitorService().notifyExecutionCanceled(this);
			}
		}
	}
		
	/**
	 * Notify the execution's pause of this token
	 * @throws GkException GkException
	 */
	protected void pauseExecution() throws GkException {
		if(isMonitorService()){			
			getMonitorService().notifyExecutionPause(this);			
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getCommandById(java.lang.Integer)
	 */
	@Override
	public GCodeCommand getCommandById(Integer id) throws GkException {
		return mapCommandById.get(id);
	}

	/**
	 * @return the complete
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
		synchronized (this) {
			notify();
		}
	}

	public boolean isMonitorService() {
		return monitorService != null;
	}
	/**
	 * @return the monitorService
	 */
	public IGCodeExecutionMonitorService getMonitorService() {
		return monitorService;
	}

	/**
	 * @param monitorService the monitorService to set
	 */
	public void setMonitorService(IGCodeExecutionMonitorService monitorService) {
		this.monitorService = monitorService;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 * @throws GkException GkException 
	 */
	public void setPaused(boolean paused) throws GkException {
		this.paused = paused;
		if(paused){
			pauseExecution();
		}
		synchronized (this) {
			notify();
		}
	}



}
