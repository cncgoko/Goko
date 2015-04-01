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
package org.goko.core.gcode.bean.commands;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;

/**
 * A Setting command (G17,G18,G19,G20,G21,G90, G91...)
 * @author PsyKo
 *
 */
public class SettingCommand extends CommentCommand{
	/** Type of motion command */
	private EnumGCodeCommandMotionType motionType;
	/** Motion mode */
	private EnumGCodeCommandMotionMode motionMode;
	/** Explicit mode. This command redefines a value for the motion mode.
	 *  If false, then the value is inherited from previous command*/
	private boolean explicitMotionMode;
	/** The feed rate of the motion */
	private BigDecimal feedrate;
	/** Explicit mode. This command redefines a value for the feed rate
	*  If false, then the value is inherited from previous command*/
	private boolean explicitFeedrate;
	/** The unit */
	private EnumGCodeCommandUnit unit;
	/** Explicit mode. This command redefines a value for the unit
	*  If false, then the value is inherited from previous command*/
	private boolean explicitUnit;
	/** Absolute mode */
	private EnumGCodeCommandDistanceMode distanceMode;
	/** Explicit mode. This command redefines a value for the distance mode
	 *  If false, then the value is inherited from previous command*/
	private boolean explicitDistanceMode;
	/** Specify the tool number */
	private Integer toolNumber;
	/** Explicit mode. This command redefines a value for the tool number
	 *  If false, then the value is inherited from previous command*/
	private boolean explicitToolNumber;
	/** The current plane */
	private EnumGCodeCommandPlane plane;
	/** Explicit plane selection */
	private boolean explicitPlane;
	/** The current coordinate system */
	private EnumCoordinateSystem coordinateSystem;
	/** Explicit coordinate system selection */
	private boolean explicitcoordinateSystem;
	/** The spindle speed */
	private BigDecimal spindleSpeed;
	/** Explicit mode for spindle speed*/
	private boolean explicitSplindleSpeed;
	/**
	 * Constructor
	 */
	public SettingCommand() {
		super();
		setType(EnumGCodeCommandType.SETTING);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#accept(org.goko.core.gcode.bean.IGCodeCommandVisitor)
	 */
	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		visitor.visit(this);
	}

	/** {@inheritDoc}
	 * @see org.goko.core.gcode.bean.GCodeCommand#updateContext(org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public void updateContext(GCodeContext context) {
		super.updateContext(context);
		context.setDistanceMode(distanceMode);
		context.setFeedrate(feedrate);
		if(motionMode != EnumGCodeCommandMotionMode.PROBE){
			context.setMotionMode(motionMode);
		}
		context.setMotionType(motionType);
		context.setUnit(unit);
		context.setPlane(plane);
		context.setCoordinateSystem(coordinateSystem);
	}

	@Override
	public void initFromContext(GCodeContext context) {
		super.initFromContext(context);
		setDistanceMode(context.getDistanceMode());
		setFeedrate(context.getFeedrate());
		setMotionMode(context.getMotionMode());
		setMotionType(context.getMotionType());
		setUnit(context.getUnit());
		setPlane(context.getPlane());
		setCoordinateSystem(context.getCoordinateSystem());
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
	 * Returns <code>true</code> if the Feedrate is explicitly specified by the command, <code>false</code> otherwise
	 * @return <code>true</code> if the Feedrate is explicitly specified by the command, <code>false</code> otherwise
	 */
	public boolean isExplicitFeedrate() {
		return explicitFeedrate;
	}
	/**
	 * Returns <code>true</code> if the Feedrate is inherited from a previous command, <code>false</code> otherwise
	 * @return <code>true</code> if the Feedrate is inherited from a previous command, <code>false</code> otherwise
	 */
	public boolean isInheritedFeedrate() {
		return !explicitFeedrate;
	}
	/**
	 * @param explicitFeedrate the explicitFeedrate to set
	 */
	public void setExplicitFeedrate(boolean explicitFeedrate) {
		this.explicitFeedrate = explicitFeedrate;
	}

	/**
	 * Returns <code>true</code> if the Unit is explicitly specified by the command, <code>false</code> otherwise
	 * @return <code>true</code> if the Unit is explicitly specified by the command, <code>false</code> otherwise
	 */
	public boolean isExplicitUnit() {
		return explicitUnit;
	}
	/**
	 * Returns <code>true</code> if the Unit is inherited from a previous command, <code>false</code> otherwise
	 * @return <code>true</code> if the Unit is inherited from a previous command, <code>false</code> otherwise
	 */
	public boolean isInheritedUnit() {
		return !explicitUnit;
	}

	/**
	 * @param explicitUnit the explicitUnit to set
	 */
	public void setExplicitUnit(boolean explicitUnit) {
		this.explicitUnit = explicitUnit;
	}

	/**
	 * Returns <code>true</code> if the Distance mode is explicitly specified by the command, <code>false</code> otherwise
	 * @return <code>true</code> if the Distance mode is explicitly specified by the command, <code>false</code> otherwise
	 */
	public boolean isExplicitDistanceMode() {
		return explicitDistanceMode;
	}

	/**
	 * Returns <code>true</code> if the Distance mode is inherited from a previous command, <code>false</code> otherwise
	 * @return <code>true</code> if the Distance mode is inherited from a previous command, <code>false</code> otherwise
	 */
	public boolean isInheritedDistanceMode() {
		return !explicitDistanceMode;
	}

	/**
	 * @param explicitDistanceMode the explicitDistanceMode to set
	 */
	public void setExplicitDistanceMode(boolean explicitDistanceMode) {
		this.explicitDistanceMode = explicitDistanceMode;
	}


	/**
	 * Returns <code>true</code> if the Motion mode is explicitly specified by the command, <code>false</code> otherwise
	 * @return <code>true</code> if the Motion mode is explicitly specified by the command, <code>false</code> otherwise
	 */
	public boolean isExplicitMotionMode() {
		return explicitMotionMode;
	}
	/**
	 * Returns <code>true</code> if the Motion mode is inherited from a previous command, <code>false</code> otherwise
	 * @return <code>true</code> if the Motion mode is inherited from a previous command, <code>false</code> otherwise
	 */
	public boolean isInheritedMotionMode() {
		return !explicitMotionMode;
	}

	/**
	 * @param explicitMotionMode the explicitMotionMode to set
	 */
	public void setExplicitMotionMode(boolean explicitMotionMode) {
		this.explicitMotionMode = explicitMotionMode;
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
	 * @return the explicitToolNumber
	 */
	public boolean isExplicitToolNumber() {
		return explicitToolNumber;
	}

	/**
	 * @param explicitToolNumber the explicitToolNumber to set
	 */
	public void setExplicitToolNumber(boolean explicitToolNumber) {
		this.explicitToolNumber = explicitToolNumber;
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
	 * @return the explicitPlane
	 */
	public boolean isExplicitPlane() {
		return explicitPlane;
	}

	/**
	 * @param explicitPlane the explicitPlane to set
	 */
	public void setExplicitPlane(boolean explicitPlane) {
		this.explicitPlane = explicitPlane;
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
	 * @return the explicitcoordinateSystem
	 */
	public boolean isExplicitcoordinateSystem() {
		return explicitcoordinateSystem;
	}

	/**
	 * @param explicitcoordinateSystem the explicitcoordinateSystem to set
	 */
	public void setExplicitcoordinateSystem(boolean explicitcoordinateSystem) {
		this.explicitcoordinateSystem = explicitcoordinateSystem;
	}

	/**
	 * @return the spindleSpeed
	 */
	public BigDecimal getSpindleSpeed() {
		return spindleSpeed;
	}

	/**
	 * @param spindleSpeed the spindleSpeed to set
	 */
	public void setSpindleSpeed(BigDecimal spindleSpeed) {
		this.spindleSpeed = spindleSpeed;
	}

	/**
	 * @return the explicitSplindleSpeed
	 */
	public boolean isExplicitSplindleSpeed() {
		return explicitSplindleSpeed;
	}

	/**
	 * @param explicitSplindleSpeed the explicitSplindleSpeed to set
	 */
	public void setExplicitSplindleSpeed(boolean explicitSplindleSpeed) {
		this.explicitSplindleSpeed = explicitSplindleSpeed;
	}

}
