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

import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Linear motion command
 * @author PsyKo
 *
 */
public abstract class LinearMotionGCodeCommand extends GCodeMotionCommand {
/*	private BigDecimal endpointX;
	private BigDecimal endpointY;
	private BigDecimal endpointZ;
	private BigDecimal endpointA;
	private BigDecimal endpointB;
	private BigDecimal endpointC;*/

	private Tuple6b endpoint;
	/**
	 *
	 */
	protected LinearMotionGCodeCommand() {
		super();
		setType(Rs274Type.MOTION_LINEAR_COMMAND);
	}
	@Override
	public void updateContext(GCodeContext context) {
		super.updateContext(context);
		if(context.isAbsolute()){
			context.getPosition().updateAbsolute(endpoint);
//			if(endpointX != null) {
//				context.setPositionX(endpointX);
//			}
//			if(endpointY != null) {
//				context.setPositionY(endpointY);
//			}
//			if(endpointZ != null) {
//				context.setPositionZ(endpointZ);
//			}
//			if(endpointA != null) {
//				context.setPositionA(endpointA);
//			}
//			if(endpointB != null) {
//				context.setPositionB(endpointB);
//			}
//			if(endpointC != null) {
//				context.setPositionC(endpointC);
//			}
		}else{
			context.getPosition().updateRelative(endpoint);
//			if(endpointX != null) {
//				context.setPositionZ( context.getPositionX().add(endpointX));
//			}
//			if(endpointY != null) {
//				context.setPositionZ( context.getPositionY().add(endpointY));
//			}
//			if(endpointZ != null) {
//				context.setPositionZ( context.getPositionZ().add(endpointZ));
//			}
//			if(endpointA != null) {
//				context.setPositionZ( context.getPositionA().add(endpointA));
//			}
//			if(endpointB != null) {
//				context.setPositionZ( context.getPositionB().add(endpointB));
//			}
//			if(endpointC != null) {
//				context.setPositionZ( context.getPositionC().add(endpointC));
//			}
		}
	}

	/**
	 * @return the endpoint
	 */
	public Tuple6b getEndpoint() {
		return endpoint;
	}
	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(Tuple6b endpoint) {
		this.endpoint = endpoint;
	}

}
