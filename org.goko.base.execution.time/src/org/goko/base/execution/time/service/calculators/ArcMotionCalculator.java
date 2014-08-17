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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeCommandExecutionTimeCalculator;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.rs274ngcv3.RS274;

/**
 * Computes the time required for a linear motion
 *
 * @author PsyKo
 *
 */
public class ArcMotionCalculator implements IGCodeCommandExecutionTimeCalculator{

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeCommandExecutionTimeCalculator#evaluateExecutionTime(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public double evaluateExecutionTime(final GCodeContext preContext, final GCodeContext postContext, GCodeCommand command) throws GkException {
		Tuple6b positionAfter 	= postContext.getPosition();
		Tuple6b tuple 			= new Tuple6b(preContext.getPosition());

		Double x = tuple.getX().doubleValue();
		Double y = tuple.getY().doubleValue();
		Double z = tuple.getZ().doubleValue();

		Double i = postContext.getOffset().getX().doubleValue();
		Double j = postContext.getOffset().getY().doubleValue();
		Double k = postContext.getOffset().getZ().doubleValue();

		Point3d center = new Point3d(x + i, y + j, z + k);

		Vector3d radius = new Vector3d(x - center.x, y - center.y, z - center.z);

		/* ** */
		x = positionAfter.getX().doubleValue();
		y = positionAfter.getY().doubleValue();
		z = positionAfter.getZ().doubleValue();
		Vector3d v2 = new Vector3d(x - center.x, y - center.y, z - center.z);

		double smallestAngle = StrictMath.atan2(radius.y,radius.x) - StrictMath.atan2(v2.y,v2.x);
		double angle = smallestAngle ;
		boolean isClockwise = StringUtils.equals(postContext.getMotionMode(), RS274.MOTION_MODE_ARC_CW);
		// If smallestAngle < 0 then it is a counterclockwise angle.
		if(smallestAngle < 0){
			if(isClockwise){ // The angle is CCW but the command is CCW
				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // When rotating, CW rotation = negative angle
			}else{
				angle = Math.abs(smallestAngle); // When rotating, CCW rotation = positive angle
			}
		}else{
			if(isClockwise){ // The angle is CW and we have a CW command
				angle = - Math.abs(smallestAngle); // When rotating, CW rotation = negative angle
			}else{ // The angle is CW but we want the CCW command
				angle =  2*Math.PI - smallestAngle;
			}
		}

		double feedrate = 0;
		if(postContext.getFeedrate() != null){
			feedrate = postContext.getFeedrate().doubleValue();
		}else if(preContext.getFeedrate() != null){
			feedrate = preContext.getFeedrate().doubleValue();
		}else{
			feedrate = 1000; // Arbitrary value TODO fix this
		}
		double d = ((Math.abs(angle) * radius.length()) / feedrate);
		return ((Math.abs(angle) * radius.length()) / feedrate) * 60;
	}
}
