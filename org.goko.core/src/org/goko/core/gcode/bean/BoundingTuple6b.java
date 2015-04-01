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

/**
 * Bounding box using Tuple6b
 *
 * @author PsyKo
 *
 */
public class BoundingTuple6b {
	private Tuple6b min;
	private Tuple6b max;
	/**
	 * @param min
	 * @param max
	 */
	public BoundingTuple6b(Tuple6b min, Tuple6b max) {
		super();
		this.min = min.min(max);
		this.max = max.max(max);
	}
	/**
	 * Add the given bound to this bound
	 * @param bound the bound to add
	 */
	public void add(BoundingTuple6b bound){
		if(bound != null){
			if(min == null){
				min = new Tuple6b(bound.min);
			}else{
				min = min.min(bound.min);
			}
			if(max == null){
				max = new Tuple6b(bound.max);
			}else{
				max = max.max(bound.max);
			}
		}
	}
	/**
	 * @return the min
	 */
	public Tuple6b getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(Tuple6b min) {
		this.min = min;
	}
	/**
	 * @return the max
	 */
	public Tuple6b getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(Tuple6b max) {
		this.max = max;
	}


}
