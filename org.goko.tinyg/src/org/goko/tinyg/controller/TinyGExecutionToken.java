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
package org.goko.tinyg.controller;

import org.apache.commons.collections.MapUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken;


public class TinyGExecutionToken extends GCodeStreamedExecutionToken{

	/**
	 * Constructor
	 * @param provider the provider to execute
	 */
	public TinyGExecutionToken(IGCodeProvider provider) {
		super(provider);
	}

	public void markAsConfirmed(GCodeCommand command) throws GkException{
		if(MapUtils.isNotEmpty(mapSentCommandById)){
			GCodeCommand nextCommand = mapSentCommandById.get(mapSentCommandById.keySet().toArray()[0]);
			//if(ObjectUtils.equals(command, nextCommand)){
				super.markAsExecuted(nextCommand.getId());
			//}
		}
	}
}
