/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.bean;

import java.math.BigDecimal;

import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandDistanceMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionType;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandPlane;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandUnit;

public class GCodeContext {
	private EnumGCodeCommandMotionMode motionMode;
	private EnumGCodeCommandMotionType motionType;
	private Tuple6b offset;
	private Tuple6b position;
	private BigDecimal feedrate;
	private EnumGCodeCommandUnit unit;
	private EnumGCodeCommandDistanceMode distanceMode;
	private EnumGCodeCommandPlane plane;
	private Integer toolNumber;
	private EnumCoordinateSystem coordinateSystem;

	public GCodeContext() {
		position 	 = new Tuple6b();
		offset 		 = new Tuple6b();
		motionMode = EnumGCodeCommandMotionMode.RAPID;
		motionType = EnumGCodeCommandMotionType.LINEAR;
		offset = new Tuple6b();
		position = new Tuple6b();
		feedrate = BigDecimal.ZERO;
		unit = EnumGCodeCommandUnit.MILLIMETERS;
		distanceMode = EnumGCodeCommandDistanceMode.ABSOLUTE;
		plane = EnumGCodeCommandPlane.XY_PLANE;
		toolNumber = 0;
		coordinateSystem = EnumCoordinateSystem.G53;
	}

	public GCodeContext(GCodeContext context) {
		this.position 		= new Tuple6b(context.position);
		this.offset 		= new Tuple6b(context.offset);
		this.feedrate 		= context.feedrate;
		this.unit 			= context.unit;
		this.distanceMode	= context.distanceMode;
		this.plane 			= context.plane;
		this.motionMode 	= context.motionMode;
		this.toolNumber 	= 0;
		this.coordinateSystem = context.coordinateSystem;
		this.motionType 	= context.motionType;
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
	 * @return the position
	 */
	public Tuple6b getPosition() {
		return new Tuple6b(position);
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Tuple6b position) {
		this.position = new Tuple6b(position);
	}

	/**
	 * @return the offset
	 */
	public Tuple6b getOffset() {
		return new Tuple6b(offset);
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Tuple6b offset) {
		this.offset = new Tuple6b(offset);
	}

	/**
	 * @return the currentPlane
	 */
	public EnumGCodeCommandPlane getPlane() {
		return plane;
	}

	/**
	 * @param currentPlane the currentPlane to set
	 */
	public void setPlane(EnumGCodeCommandPlane currentPlane) {
		this.plane = currentPlane;
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


}
