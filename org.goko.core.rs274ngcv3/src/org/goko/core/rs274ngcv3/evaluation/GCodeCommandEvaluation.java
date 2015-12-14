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

package org.goko.core.rs274ngcv3.evaluation;

import java.math.BigDecimal;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandPlane;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;

public class GCodeCommandEvaluation{
	private EnumGCodeCommandMotionMode motionMode;
	private EnumGCodeCommandMotionType motionType;
	private BigDecimal feedrate;
	private EnumGCodeCommandUnit unit;
	private EnumGCodeCommandDistanceMode distanceMode;
	private EnumGCodeCommandPlane plane;
	private Integer toolNumber;
	private EnumCoordinateSystem coordinateSystem;
	private Tuple6b start;
	private Tuple6b end;
	private GCodeCommand gCodeCommand;

	public GCodeCommandEvaluation(GCodeCommand gCodeCommand) {
		this.gCodeCommand = gCodeCommand;
	}
	/**
	 * @return the start
	 */
	public Tuple6b getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Tuple6b start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public Tuple6b getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(Tuple6b end) {
		this.end = end;
	}
	/**
	 * @return the motionMode
	 */
	public EnumGCodeCommandMotionMode getMotionMode() {
		return motionMode;
	}
	/**
	 * @param motionMode the motionMode to set
	 */
	public void setMotionMode(EnumGCodeCommandMotionMode motionMode) {
		this.motionMode = motionMode;
	}
	/**
	 * @return the motionType
	 */
	public EnumGCodeCommandMotionType getMotionType() {
		return motionType;
	}
	/**
	 * @param motionType the motionType to set
	 */
	public void setMotionType(EnumGCodeCommandMotionType motionType) {
		this.motionType = motionType;
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
	/**
	 * @return the unit
	 */
	public EnumGCodeCommandUnit getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(EnumGCodeCommandUnit unit) {
		this.unit = unit;
	}
	/**
	 * @return the distanceMode
	 */
	public EnumGCodeCommandDistanceMode getDistanceMode() {
		return distanceMode;
	}
	/**
	 * @param distanceMode the distanceMode to set
	 */
	public void setDistanceMode(EnumGCodeCommandDistanceMode distanceMode) {
		this.distanceMode = distanceMode;
	}
	/**
	 * @return the plane
	 */
	public EnumGCodeCommandPlane getPlane() {
		return plane;
	}
	/**
	 * @param plane the plane to set
	 */
	public void setPlane(EnumGCodeCommandPlane plane) {
		this.plane = plane;
	}
	/**
	 * @return the toolNumber
	 */
	public Integer getToolNumber() {
		return toolNumber;
	}
	/**
	 * @param toolNumber the toolNumber to set
	 */
	public void setToolNumber(Integer toolNumber) {
		this.toolNumber = toolNumber;
	}
	/**
	 * @return the coordinateSystem
	 */
	public EnumCoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}
	/**
	 * @param coordinateSystem the coordinateSystem to set
	 */
	public void setCoordinateSystem(EnumCoordinateSystem coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}
	/**
	 * @return the gCodeCommand
	 */
	public GCodeCommand getGCodeCommand() {
		return gCodeCommand;
	}
	/**
	 * @param gCodeCommand the gCodeCommand to set
	 */
	public void setGCodeCommand(GCodeCommand gCodeCommand) {
		this.gCodeCommand = gCodeCommand;
	}

}
