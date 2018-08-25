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

package org.goko.tools.viewer.jogl.utils.render.basic;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;

import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;

import com.jogamp.opengl.GL;

public class PointRenderer extends PolylineRenderer{
	private Tuple6b position;
	private double size;

	public PointRenderer(Tuple6b position, double size, Color4f color) {
		super();
		this.position = position;
		this.size = size;
		this.setClosed(false);
		this.setColor(color);
		this.setRenderPrimitive(GL.GL_LINES);
		generatePoints();
	}

	private void generatePoints() {
		double hSize = size / 2;
		Point3d tmpPos = position.toPoint3d(JoglUtils.JOGL_UNIT);
		List<Point3d> lstPoints = new ArrayList<Point3d>();
		lstPoints.add( new Point3d(tmpPos.x + hSize, tmpPos.y, tmpPos.z) );
		lstPoints.add( new Point3d(tmpPos.x - hSize, tmpPos.y, tmpPos.z) );
		lstPoints.add( new Point3d(tmpPos.x, tmpPos.y + hSize, tmpPos.z) );
		lstPoints.add( new Point3d(tmpPos.x, tmpPos.y - hSize, tmpPos.z) );
		lstPoints.add( new Point3d(tmpPos.x, tmpPos.y, tmpPos.z + hSize) );
		lstPoints.add( new Point3d(tmpPos.x, tmpPos.y, tmpPos.z - hSize) );
		setPoints(lstPoints);
	}


}
