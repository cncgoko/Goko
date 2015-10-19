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
package org.goko.tools.execution.time.service.calculators;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeCommandExecutionTimeCalculator;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.math.Tuple6b;

/**
 * Computes the time required for a linear motion
 *
 * @author PsyKo
 *
 */
public class LinearMotionCalculator implements IGCodeCommandExecutionTimeCalculator<LinearMotionCommand>{

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeCommandExecutionTimeCalculator#evaluateExecutionTime(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public double evaluateExecutionTime(LinearMotionCommand command, final GCodeContext context) throws GkException {
		Tuple6b 		positionBefore 	= command.getAbsoluteStartCoordinate();
		Tuple6b 		positionAfter 	= command.getAbsoluteEndCoordinate();

		Tuple6b delta = positionBefore.subtract(positionAfter);

		double dx = (delta.getX() == null) ? 0 : Math.abs(positionBefore.getX().doubleValue() - positionAfter.getX().doubleValue());
		double dy = (delta.getY() == null) ? 0 : Math.abs(positionBefore.getY().doubleValue() - positionAfter.getY().doubleValue());
		double dz = (delta.getZ() == null) ? 0 : Math.abs(positionBefore.getZ().doubleValue() - positionAfter.getZ().doubleValue());
		double da = (delta.getA() == null) ? 0 : Math.abs(positionBefore.getA().doubleValue() - positionAfter.getA().doubleValue());
		double db = (delta.getB() == null) ? 0 : Math.abs(positionBefore.getB().doubleValue() - positionAfter.getB().doubleValue());
		double dc = (delta.getC() == null) ? 0 : Math.abs(positionBefore.getC().doubleValue() - positionAfter.getC().doubleValue());

		double max = Math.max(dx, Math.max(dy, Math.max(dz, Math.max(da, Math.max(db, dc)))));

		double feedrate = 0;
		if(command.getFeedrate() != null){
			feedrate = command.getFeedrate().doubleValue();
		}else{
			return 0;
		}
		if(feedrate == 0){
			return 0;
		}
		return (max / feedrate) * 60 ;
	}
}
