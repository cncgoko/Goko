/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.viewer.jogl.utils.render.basic;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class CircleRenderer extends PolylineRenderer{
	/** The center of the circle*/
	private Point3d center;
	/** The radius of the circle */
	private double radius;
	/** The normal of the plan in which the circle lies*/
	private Vector3d normal;

	public CircleRenderer(Point3d center, double radius, Color4f color, Vector3d normal) {
		super();
		super.setClosed(true);
		super.setColor(color);
		this.center = center;
		this.radius = radius;
		this.normal = normal;
		Vector3d a = new Vector3d(1,0,0);
		Vector3d b = new Vector3d(1,0,0);
		if(a.dot(normal) <= 0.01){
			a = new Vector3d(0,1,0);
		}
		b.cross(normal, a);
		a.cross(b, normal);
		int nbPoints = 24;
		double angle = 2 * Math.PI / nbPoints;
		List<Point3d> lstPoint = new ArrayList<Point3d>();
		for(int i = 0; i < nbPoints; i++){
			Point3d p = new Point3d(
						center.x + radius * Math.cos(i*angle)*a.x + radius * Math.sin(i * angle) * b.x,
						center.y + radius * Math.cos(i*angle)*a.y + radius * Math.sin(i * angle) * b.y,
						center.z + radius * Math.cos(i*angle)*a.z + radius * Math.sin(i * angle) * b.z
					);
			lstPoint.add(p);
		}
		setPoints(lstPoint);
		setRenderPrimitive(GL.GL_LINE_STRIP);
	}
}
