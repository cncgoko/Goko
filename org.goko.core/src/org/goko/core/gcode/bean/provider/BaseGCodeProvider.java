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
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;

/**
 * Basic command provider for a single command
 *
 * @author PsyKo
 *
 */
public class BaseGCodeProvider implements IGCodeProvider{
	/** The commands */
	private List<GCodeCommand> lstCommands;

	/**
	 * @param lstCommands
	 */
	public BaseGCodeProvider(List<GCodeCommand> lstCommands) {
		super();
		this.lstCommands = Collections.synchronizedList(new ArrayList<GCodeCommand>());
		Collections.copy(this.lstCommands, lstCommands);
	}



	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getGCodeCommands()
	 */
	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return lstCommands;
	}

	public synchronized boolean hasNext(){
		return !CollectionUtils.isEmpty(this.lstCommands);
	}

	public synchronized GCodeCommand unstackNextCommand() throws GkException{
		if(CollectionUtils.isNotEmpty(lstCommands)){
			return lstCommands.remove(0);
		}
		throw new GkTechnicalException("No more command available...");
	}
}
