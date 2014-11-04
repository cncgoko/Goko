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
package org.goko.grbl.controller.executionqueue;

import org.apache.commons.collections.MapUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken;

/**
 * Streamed queue designed to work with Grbl
 *
 * @author PsyKo
 *
 */
public class GrblGCodeExecutionToken  extends GCodeStreamedExecutionToken{


	/**
	 * Constructor
	 * @param provider the provider to execute
	 */
	public GrblGCodeExecutionToken(IGCodeProvider provider) {
		super(provider);
	}

	/**
	 * Mark th enext command as executed
	 * @return
	 * @throws GkException GkException
	 */
	public GCodeCommand markNextCommandAsConfirmed() throws GkException{
		if(MapUtils.isNotEmpty(mapSentCommandById)){
			GCodeCommand nextCommand = mapSentCommandById.get(mapSentCommandById.keySet().toArray()[0]);
			markAsExecuted(nextCommand.getId());
			return nextCommand;
		}
		return null;
	}

	/**
	 * Mark th enext command as executed
	 * @throws GkException GkException
	 */
	public GCodeCommand markNextCommandAsError() throws GkException{
		if(MapUtils.isNotEmpty(mapSentCommandById)){
			GCodeCommand nextCommand = mapSentCommandById.get(mapSentCommandById.keySet().toArray()[0]);
			markAsError(nextCommand.getId());
			return nextCommand;
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken#markAsExecuted(java.lang.Integer)
	 */
	@Override
	public void markAsExecuted(Integer idCommand) throws GkException {
		super.markAsExecuted(idCommand);
		mapSentCommandById.remove(idCommand);
	}

	@Override
	public void markAsError(Integer idCommand) throws GkException {
		super.markAsError(idCommand);
		mapSentCommandById.remove(idCommand);
	}
}
