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

import org.goko.core.gcode.bean.GCodeContext;

/**
 * Linear motion command
 * @author PsyKo
 *
 */
public abstract class LinearMotionGCodeCommand extends GCodeMotionCommand {
	private BigDecimal endpointX;
	private BigDecimal endpointY;
	private BigDecimal endpointZ;
	private BigDecimal endpointA;
	private BigDecimal endpointB;
	private BigDecimal endpointC;

	@Override
	public void updateContext(GCodeContext context) {
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

}
