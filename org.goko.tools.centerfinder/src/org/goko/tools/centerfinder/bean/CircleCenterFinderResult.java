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
package org.goko.tools.centerfinder.bean;

import javax.vecmath.Vector3d;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.tools.centerfinder.EnumPlane;

public class CircleCenterFinderResult {
	/** The center of the circle */
	private Tuple6b center;
	/** the radius of the circle */
	private Length radius;
	/** The plane in which the circle is described */
	private EnumPlane plane;
	
	/**
	 * @return the center
	 */
	public Tuple6b getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(Tuple6b center) {
		this.center = center;
	}
	/**
	 * @return the radius
	 */
	public Length getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Length radius) {
		this.radius = radius;
	}
	public Length getDiameter() {		
		return this.radius.multiply(2);
	}
	/**
	 * @return the plane
	 */
	public EnumPlane getPlane() {
		return plane;
	}
	/**
	 * @param plane the plane to set
	 */
	public void setPlane(EnumPlane plane) {
		this.plane = plane;
	}
	/**
	 * The normal to the given plane
	 * @return
	 */
	public Vector3d getNormal() {
		switch (plane) {
		case XY_PLANE: return new Vector3d(0,0,1);			
		case YZ_PLANE: return new Vector3d(1,0,0);
		case XZ_PLANE: return new Vector3d(0,1,0);
		default:return new Vector3d(0,0,1);			
		}
	}
	
	
}
