/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.gcode.rs274ngcv3.evaluation;

import org.goko.core.gcode.bean.GCodeContext;

public class GCodeContextUpdater extends GCodeContext {

	public GCodeContextUpdater(GCodeContext initial, GCodeContext updated) {
		super(updated);
		if(initial.getCoordinateSystem() == updated.getCoordinateSystem()){
			setCoordinateSystem(null);
		}
		if(initial.getDistanceMode() == updated.getDistanceMode()){
			setDistanceMode(null);
		}
		if(initial.getFeedrate() == updated.getFeedrate()){
			setFeedrate(null);
		}
		if(initial.getMotionMode() == updated.getMotionMode()){
			setMotionMode(null);
		}
		if(initial.getMotionType() == updated.getMotionType()){
			setMotionType(null);
		}
		if(initial.getPlane() == updated.getPlane()){
			setPlane(null);
		}
		if(initial.getToolNumber() == updated.getToolNumber()){
			setToolNumber(null);
		}
		if(initial.getUnit() == updated.getUnit()){
			setUnit(null);
		}
	}

	public boolean hasMoreUpdate(){
		return getCoordinateSystem() != null
			&& getDistanceMode() != null
			&& getFeedrate() != null
			&& getMotionMode() != null
			&& getMotionType() != null
			&& getPlane() != null
			&& getToolNumber() != null
			&& getUnit() != null;
	}
}
