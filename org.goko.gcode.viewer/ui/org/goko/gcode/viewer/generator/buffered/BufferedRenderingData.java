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
package org.goko.gcode.viewer.generator.buffered;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld;

public class BufferedRenderingData {
	private AbstractGCodeGlRendererOld renderer;
	private List<Point3d> points;

	/**
	 * @param renderer
	 * @param points
	 */
	BufferedRenderingData(AbstractGCodeGlRendererOld renderer, List<Point3d> points) {
		super();
		this.renderer = renderer;
		this.points = points;
	}
	/**
	 * @return the renderer
	 */
	public AbstractGCodeGlRendererOld getRenderer() {
		return renderer;
	}
	/**
	 * @param renderer the renderer to set
	 */
	public void setRenderer(AbstractGCodeGlRendererOld renderer) {
		this.renderer = renderer;
	}
	/**
	 * @return the points
	 */
	public List<Point3d> getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(List<Point3d> points) {
		this.points = points;
	}

}
