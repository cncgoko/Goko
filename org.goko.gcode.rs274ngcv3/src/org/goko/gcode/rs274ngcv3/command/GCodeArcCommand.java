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

import javax.vecmath.Point3d;

import org.goko.core.gcode.bean.GCodeContext;

/**
 * An extended GCodeCommand describing an arc command (usually G02 or G03)
 * @author PsyKo
 *
 */
public class GCodeArcCommand extends GCodeMotionCommand {
	/** The endpoint of the arc */
	private BigDecimal endpointX;
	private BigDecimal endpointY;
	private BigDecimal endpointZ;
	private BigDecimal endpointA;
	private BigDecimal endpointB;
	private BigDecimal endpointC;

	/** The I offset */
	private BigDecimal offsetI;
	/** The J offset */
	private BigDecimal offsetJ;
	/** The K offset */
	private BigDecimal offsetK;
	/** The radius */
	private BigDecimal radius;
	/** The center of the arc */
	private Point3d center;
	/** The direction of the arc*/
	private boolean clockwise;

	/**
	 * @param clockwise the direction
	 */
	public GCodeArcCommand(boolean clockwise) {
		super();
		this.clockwise = clockwise;
	}

	@Override
	public void updateContext(GCodeContext context) {
		// TODO Auto-generated method stub
		super.updateContext(context);
		if(context.isAbsolute()){
			if(endpointX != null) {
				context.setPositionX(endpointX);
			}
			if(endpointY != null) {
				context.setPositionY(endpointY);
			}
			if(endpointZ != null) {
				context.setPositionZ(endpointZ);
			}
			if(endpointA != null) {
				context.setPositionA(endpointA);
			}
			if(endpointB != null) {
				context.setPositionB(endpointB);
			}
			if(endpointC != null) {
				context.setPositionC(endpointC);
			}
		}else{
			if(endpointX != null) {
				context.setPositionZ( context.getPositionX().add(endpointX));
			}
			if(endpointY != null) {
				context.setPositionZ( context.getPositionY().add(endpointY));
			}
			if(endpointZ != null) {
				context.setPositionZ( context.getPositionZ().add(endpointZ));
			}
			if(endpointA != null) {
				context.setPositionZ( context.getPositionA().add(endpointA));
			}
			if(endpointB != null) {
				context.setPositionZ( context.getPositionB().add(endpointB));
			}
			if(endpointC != null) {
				context.setPositionZ( context.getPositionC().add(endpointC));
			}
		}
	}
	/**
	 * @return the offsetI
	 */
	public BigDecimal getOffsetI() {
		return offsetI;
	}
	public double getOffsetIDouble(){
		if(offsetI == null){
			return 0;
		}
		return offsetI.doubleValue();
	}
	/**
	 * @param offsetI the offsetI to set
	 */
	public void setOffsetI(BigDecimal offsetI) {
		this.offsetI = offsetI;
	}
	/**
	 * @return the offsetJ
	 */
	public BigDecimal getOffsetJ() {
		return offsetJ;
	}
	public double getOffsetJDouble(){
		if(offsetJ == null){
			return 0;
		}
		return offsetJ.doubleValue();
	}
	/**
	 * @param offsetJ the offsetJ to set
	 */
	public void setOffsetJ(BigDecimal offsetJ) {
		this.offsetJ = offsetJ;
	}
	/**
	 * @return the offsetK
	 */
	public BigDecimal getOffsetK() {
		return offsetK;
	}
	public double getOffsetKDouble(){
		if(offsetK == null){
			return 0;
		}
		return offsetK.doubleValue();
	}
	/**
	 * @param offsetK the offsetK to set
	 */
	public void setOffsetK(BigDecimal offsetK) {
		this.offsetK = offsetK;
	}
	/**
	 * @return the center
	 */
	public Point3d getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(Point3d center) {
		this.center = center;
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
	 * @return the endpointX
	 */
	public BigDecimal getEndpointX() {
		return endpointX;
	}

	/**
	 * @param endpointX the endpointX to set
	 */
	public void setEndpointX(BigDecimal endpointX) {
		this.endpointX = endpointX;
	}

	/**
	 * @return the endpointY
	 */
	public BigDecimal getEndpointY() {
		return endpointY;
	}

	/**
	 * @param endpointY the endpointY to set
	 */
	public void setEndpointY(BigDecimal endpointY) {
		this.endpointY = endpointY;
	}

	/**
	 * @return the endpointZ
	 */
	public BigDecimal getEndpointZ() {
		return endpointZ;
	}

	/**
	 * @param endpointZ the endpointZ to set
	 */
	public void setEndpointZ(BigDecimal endpointZ) {
		this.endpointZ = endpointZ;
	}

	/**
	 * @return the endpointA
	 */
	public BigDecimal getEndpointA() {
		return endpointA;
	}

	/**
	 * @param endpointA the endpointA to set
	 */
	public void setEndpointA(BigDecimal endpointA) {
		this.endpointA = endpointA;
	}

	/**
	 * @return the endpointB
	 */
	public BigDecimal getEndpointB() {
		return endpointB;
	}

	/**
	 * @param endpointB the endpointB to set
	 */
	public void setEndpointB(BigDecimal endpointB) {
		this.endpointB = endpointB;
	}

	/**
	 * @return the endpointC
	 */
	public BigDecimal getEndpointC() {
		return endpointC;
	}

	/**
	 * @param endpointC the endpointC to set
	 */
	public void setEndpointC(BigDecimal endpointC) {
		this.endpointC = endpointC;
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


}
