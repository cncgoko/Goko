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

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.bean.Tuple6b;

public class CircleCenterFinderResult {
	/** The center of the circle */
	private Tuple6b center;
	/** the radius of the circle */
	private BigDecimalQuantity<Length> radius;
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
	public BigDecimalQuantity<Length> getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(BigDecimalQuantity<Length> radius) {
		this.radius = radius;
	}
	public BigDecimalQuantity<Length> getDiameter() {		
		return this.radius.multiply(2);
	}
	
	
}
