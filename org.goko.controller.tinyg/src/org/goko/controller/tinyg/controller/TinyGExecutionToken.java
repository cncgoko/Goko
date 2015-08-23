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

import org.apache.commons.collections.MapUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken;


public class TinyGExecutionToken extends GCodeStreamedExecutionToken{
	private Object lock;	
	/**
	 * Constructor
	 * @param provider the provider to execute
	 */
	public TinyGExecutionToken(IGCodeProvider provider) {
		super(provider);		
		lock = new Object();
	}

	public void markAsConfirmed(GCodeCommand command) throws GkException{
		synchronized (lock) {
			if(MapUtils.isNotEmpty(mapSentCommandById)){
				GCodeCommand nextCommand = mapSentCommandById.get(mapSentCommandById.keySet().toArray()[0]);
				//if(ObjectUtils.equals(command, nextCommand)){
					markAsExecuted(nextCommand.getId());
					updateCompleteState();
				//}
			}
		}
	}

	@Override
	public void markAsSent(Integer idCommand) throws GkException {
		synchronized (lock) {
			super.markAsSent(idCommand);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.provider.GCodeStreamedExecutionToken#markAsExecuted(java.lang.Integer)
	 */
	@Override
	public void markAsExecuted(Integer idCommand) throws GkException {
		synchronized (lock) {
			super.markAsExecuted(idCommand);
			mapSentCommandById.remove(idCommand);
			updateCompleteState();
		}
	}

}
