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
package org.goko.gcode.rs274ngcv3.command;

import java.math.BigDecimal;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;

/**
 * GCode command describing a motion command
 * @author PsyKo
 *
 */
public class GCodeMotionCommand extends GCodeCommand {
	/** Feed rate of the command */
	private BigDecimal feedrate;

	public GCodeMotionCommand() {
		setType(Rs274Type.MOTION_COMMAND);
	}

	@Override
	public void updateContext(GCodeContext context) {
		super.updateContext(context);
		if(feedrate != null){
			context.setFeedrate(feedrate);
		}
	}
	/**
	 * @return the feedrate
	 */
	public BigDecimal getFeedrate() {
		return feedrate;
	}

	/**
	 * @param feedrate the feedrate to set
	 */
	public void setFeedrate(BigDecimal feedrate) {
		this.feedrate = feedrate;
	}


}
