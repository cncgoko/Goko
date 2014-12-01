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

import javax.vecmath.Point3d;

public class Segment {
	private Point3d start;
	private Point3d end;

	/**
	 * @param start
	 * @param end
	 */
	public Segment(Point3d start, Point3d end) {
		super();
		this.start = start;
		this.end = end;
	}
	/**
	 * @return the start
	 */
	public Point3d getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Point3d start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public Point3d getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(Point3d end) {
		this.end = end;
	}

}
