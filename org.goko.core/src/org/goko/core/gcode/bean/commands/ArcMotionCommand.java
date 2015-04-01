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
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Defines an arc motion command
 *
 * @author PsyKo
 *
 */
public class ArcMotionCommand extends MotionCommand {
	/** The computed center point coordinate in the absolute coordinate system in mm */
	private Tuple6b absoluteCenterCoordinate;
	/** Turn direction */
	private boolean clockwise;
	/** the radius of the arc */
	private BigDecimal radius;
	/** The number of complete turns beside the arc definition (P word)*/
	private int turnCount;
	/** The revolution axis */
	private Tuple6b revolutionAxis;
	/** I,J and K values expressing the relative offset */
	private Tuple6b ijkValues;

	/**
	 * Constructor
	 */
	public ArcMotionCommand() {
		super(EnumGCodeCommandMotionType.ARC);
		this.ijkValues = new Tuple6b();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#accept(org.goko.core.gcode.bean.IGCodeCommandVisitor)
	 */
	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		visitor.visit(this);
	}

	protected void updateCenterCoordinate(){
		absoluteCenterCoordinate = new Tuple6b(getAbsoluteStartCoordinate());
		absoluteCenterCoordinate.updateRelative(ijkValues);
	}
	/**
	 * @return the absoluteCenterCoordinate
	 */
	public Tuple6b getAbsoluteCenterCoordinate() {
		return new Tuple6b(absoluteCenterCoordinate);
	}

	/**
	 * @param absoluteCenterCoordinate the absoluteCenterCoordinate to set
	 */
	public void setAbsoluteCenterCoordinate(Tuple6b absoluteCenterCoordinate) {
		this.absoluteCenterCoordinate = new Tuple6b(absoluteCenterCoordinate);
	}

	/**
	 * @return the radius
	 */
	public BigDecimal getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(BigDecimal radius) {
		this.radius = radius;
	}

	/**
	 * @return the turnCount
	 */
	public int getTurnCount() {
		return turnCount;
	}

	/**
	 * @param turnCount the turnCount to set
	 */
	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}

	/**
	 * @return the revolutionAxis
	 */
	public Tuple6b getRevolutionAxis() {
		return new Tuple6b(revolutionAxis);
	}

	/**
	 * @param revolutionAxis the revolutionAxis to set
	 */
	public void setRevolutionAxis(Tuple6b revolutionAxis) {
		this.revolutionAxis = new Tuple6b(revolutionAxis);
	}

	public BigDecimal getI(){
		return ijkValues.getX();
	}
	public BigDecimal getJ(){
		return ijkValues.getY();
	}
	public BigDecimal getK(){
		return ijkValues.getZ();
	}


	/**
	 * @return the clockwise
	 */
	public boolean isClockwise() {
		return clockwise;
	}

	/**
	 * @param clockwise the clockwise to set
	 */
	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	/**
	 * @return the ijkValues
	 */
	public Tuple6b getIJKValues() {
		return ijkValues;
	}

	/**
	 * @param ijkValues the ijkValues to set
	 */
	public void setIJKValues(Tuple6b ijkValues) {
		this.ijkValues = ijkValues;
		updateCenterCoordinate();
	}

}
