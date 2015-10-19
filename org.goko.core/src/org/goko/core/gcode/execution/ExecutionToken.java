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
package org.goko.core.gcode.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IExecutionMonitorService;

/**
 * Implementation of a {@link IExecutionToken} for execution planner
 *
 * @author PsyKo
 *
 */
public class ExecutionToken<T extends IExecutionState> implements IExecutionToken<T> {
	/** Id of the GCodeProvider */
	private Integer id;
	/** the list of commands */
	protected Map<Integer, GCodeLine> lstGCodeLine;
	/** The map of state by line Id */
	protected Map<Integer, T> mapExecutionStateById;
	/** The map of lines id by state */
	protected Map<T, List<Integer>> mapLineByExecutionState;
	/** The ordered list of commands */
	protected List<Integer> commands;
	/** The current command index */
	protected int currentIndex;
	/** Completed state  */
	protected boolean complete;
	/** The monitor service */
	protected IExecutionMonitorService<T, IExecutionToken<T>> monitorService;
	/** Paused state */
	protected boolean paused;
	
	/**
	 * Constructor
	 * @param provider the provider to build this execution token from
	 */
	public ExecutionToken(IGCodeProvider provider, T initState) {		
		this.mapExecutionStateById 	= new HashMap<Integer, T>();		
		this.mapLineByExecutionState = new HashMap<T, List<Integer>>();
		this.mapLineByExecutionState.put(initState, new ArrayList<Integer>());
		
		this.currentIndex = -1;
		this.lstGCodeLine = new HashMap<Integer, GCodeLine>();
		this.commands = new ArrayList<Integer>();

		if(CollectionUtils.isNotEmpty(provider.getLines())){
			for (GCodeLine gCodeLine : provider.getLines()) {
				this.lstGCodeLine.put(gCodeLine.getId(), gCodeLine);
				this.commands.add(gCodeLine.getId());
				this.mapLineByExecutionState.get(initState).add(gCodeLine.getId());
				this.mapExecutionStateById.put(gCodeLine.getId(), initState);
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
	 * @see org.goko.core.gcode.execution.IExecutionToken#setLineState(java.lang.Integer, org.goko.core.gcode.execution.IExecutionState)
	 */
	@Override
	public void setLineState(Integer idLine, T state) throws GkException {
		if(!mapExecutionStateById.containsKey(idLine)){
			throw new GkTechnicalException("GCodeLine ["+idLine+"] not found in execution token");	
		}
		
		if(!mapLineByExecutionState.containsKey(state)){
			mapLineByExecutionState.put(state, new ArrayList<Integer>());	
		}
		// remove from previous state list
		T oldState = findLineState(idLine);
		if(oldState != null){
			mapLineByExecutionState.get(oldState).remove(idLine);	
		}
		// Add to new state list
		mapLineByExecutionState.get(state).add(idLine);
		mapExecutionStateById.put(idLine, state);
		monitorService.notifyCommandStateChanged(this, idLine);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken#getLineState(java.lang.Integer)
	 */
	@Override
	public T getLineState(Integer idLine) throws GkException {
		if(mapExecutionStateById.containsKey(idLine)){
			return mapExecutionStateById.get(idLine);
		}
		throw new GkTechnicalException("GCodeLine ["+idLine+"] not found in execution token");
	}
	
	/**
	 * Equivalent to getLineState except it doesn't throws Exception
	 * @param idLine the id of the line 
	 * @return a state
	 * @throws GkException GkException
	 */
	public T findLineState(Integer idLine) throws GkException {
		if(mapExecutionStateById.containsKey(idLine)){
			return mapExecutionStateById.get(idLine);
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken#getNextCommand()
	 */
	@Override
	public GCodeLine getNextLine() throws GkException {
		return lstGCodeLine.get(commands.get(currentIndex + 1));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken#takeNextCommand()
	 */
	@Override
	public GCodeLine takeNextLine() throws GkException {
		currentIndex = currentIndex + 1;
		Integer nextId = commands.get(currentIndex);
		return lstGCodeLine.get(nextId);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken#hasMoreLine()
	 */
	@Override
	public boolean hasMoreLine() throws GkException {
		return getLineCount() > currentIndex + 1;
	}

	/**
	 * Returns the number of lines
	 * @return the number of lines
	 */
	public int getLineCount() {		
		return CollectionUtils.size(lstGCodeLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken#beginExecution()
	 */
	@Override
	public void beginExecution() throws GkException {
		if(isMonitorService()){
			getMonitorService().notifyExecutionStart(this);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken.execution.IGCodeExecutionToken#endExecution()
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
	public IExecutionMonitorService<T, IExecutionToken<T>> getMonitorService() {
		return monitorService;
	}

	/**
	 * @param monitorService the monitorService to set
	 */
	public void setMonitorService(IExecutionMonitorService<T, IExecutionToken<T>> monitorService) {
		this.monitorService = monitorService;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.gcode.execution.IExecutionToken#setExecutionPaused(boolean)
	 */
	@Override
	public void setExecutionPaused(boolean paused) throws GkException {
		this.paused = paused;
		if (paused) {
			pauseExecution();
		}
		synchronized (this) {
			notify();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() {		
		return new ArrayList<GCodeLine>(lstGCodeLine.values());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken#isExecutionPaused()
	 */
	@Override
	public boolean isExecutionPaused() throws GkException {		
		return paused;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutionToken#getLineByState(org.goko.core.gcode.execution.IExecutionState)
	 */
	@Override
	public List<GCodeLine> getLineByState(T state) throws GkException {
		List<GCodeLine> result = new ArrayList<GCodeLine>();
		List<Integer> lstId = mapLineByExecutionState.get(state);
		for (Integer idLine : lstId) {
			result.add(getLine(idLine));
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) {
		// TODO Auto-generated method stub
		return null;
	}

}
