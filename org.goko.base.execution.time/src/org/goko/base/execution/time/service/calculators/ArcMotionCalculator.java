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
package org.goko.base.execution.time.service.calculators;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeCommandExecutionTimeCalculator;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;

/**
 * Computes the time required for a linear motion
 *
 * @author PsyKo
 *
 */
public class ArcMotionCalculator implements IGCodeCommandExecutionTimeCalculator<ArcMotionCommand>{

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeCommandExecutionTimeCalculator#evaluateExecutionTime(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public double evaluateExecutionTime(ArcMotionCommand command, final GCodeContext context) throws GkException {

		Point3d start 	= command.getAbsoluteStartCoordinate().toPoint3d(); 
		Point3d center 	= command.getAbsoluteCenterCoordinate().toPoint3d();
		Point3d end 	= command.getAbsoluteEndCoordinate().toPoint3d();

		Vector3d v1 = new Vector3d(start.x - center.x, start.y - center.y, start.z - center.z);
		Vector3d v2 = new Vector3d(end.x - center.x, end.y - center.y, end.z - center.z);

		double smallestAngle = StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x);
		double angle = smallestAngle ;
		// If smallestAngle < 0 then it is a counterclockwise angle.
		if(smallestAngle < 0){
			if(command.isClockwise()){ // The angle is CCW but the command is CCW
				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // In OpenGl when rotating, CW rotation = negative angle
			}else{
				angle = Math.abs(smallestAngle); // In OpenGl when rotating, CCW rotation = positive angle
			}
		}else{
			if(command.isClockwise()){ // The angle is CW and we have a CW command
				angle = - Math.abs(smallestAngle); // In OpenGl when rotating, CW rotation = negative angle
			}else{ // The angle is CW but we want the CCW command
				angle =  2*Math.PI - smallestAngle;
			}
		}

		double feedrate = 0;
		if(command.getFeedrate() != null){
			feedrate = command.getFeedrate().doubleValue();
		}else{
			return 0;
		}
		if(feedrate == 0){
			return 0;
		}
		return ((Math.abs(angle) * v1.length()) / feedrate) * 60;
	}
}
