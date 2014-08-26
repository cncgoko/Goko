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
package org.goko.base.execution.time.service;

import org.goko.base.execution.time.service.calculators.ArcMotionCalculator;
import org.goko.base.execution.time.service.calculators.LinearMotionCalculator;
import org.goko.core.execution.IGCodeCommandExecutionTimeCalculator;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;

public class TimeCalculatorFactory {

	public IGCodeCommandExecutionTimeCalculator  getCalculator(GCodeContext context, GCodeCommand command){

		if(context.getMotionType() == EnumGCodeCommandMotionType.LINEAR){
			return new LinearMotionCalculator();
		}else if(context.getMotionType() == EnumGCodeCommandMotionType.ARC){
			return new ArcMotionCalculator();

		}
		return null;
	}
}
