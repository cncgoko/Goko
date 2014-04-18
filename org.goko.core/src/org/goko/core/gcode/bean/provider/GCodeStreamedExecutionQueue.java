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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeExecutionTimeCalculator;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.log.GkLog;

/**
 * Implementation of a {@link IGCodeProvider} for execution planner on a distant controller
 *
 * @author PsyKo
 *
 */
public class GCodeStreamedExecutionQueue  extends GCodeExecutionQueue{
	private static final GkLog LOG = GkLog.getLogger(GCodeStreamedExecutionQueue.class);
	/**
	 * Execution time evaluator
	 */
	private IGCodeExecutionTimeCalculator<GCodeStreamedExecutionQueue> timeCalculator;
	/**
	 * The list of commansd awaiting acknowledgement
	 */
	private List<GCodeCommand> unacknowledgedCommands;


	/**
	 * Constructor.
	 * Creates an execution queue from the given provider
	 * @param provider the provider to get command from
	 */
	public GCodeStreamedExecutionQueue(IGCodeProvider provider) {
		super(provider);
	}

	/**
	 * Constructor
	 * @param lstCommands
	 */
	public GCodeStreamedExecutionQueue(List<GCodeCommand> lstCommands) {
		super(lstCommands);
	}

	public void confirmCommand(final GCodeCommand command) throws GkException{
		if(CollectionUtils.isNotEmpty(unacknowledgedCommands)){
			GCodeCommand pendingCommand = unacknowledgedCommands.get(0);
			if(ObjectUtils.equals(pendingCommand, command)){
				unacknowledgedCommands.remove(0);
				setCommandState(pendingCommand, GCodeCommandState.CONFIRMED);
			}else{
				LOG.debug("  /!\\  Cannot confirm GCode command "+command);
			}
		}
	}

	/**
	 * @return the timeCalculator
	 */
	public IGCodeExecutionTimeCalculator<GCodeStreamedExecutionQueue> getTimeCalculator() {
		return timeCalculator;
	}

	/**
	 * @param timeCalculator the timeCalculator to set
	 */
	public void setTimeCalculator(IGCodeExecutionTimeCalculator<GCodeStreamedExecutionQueue> timeCalculator) {
		this.timeCalculator = timeCalculator;
	}

}
