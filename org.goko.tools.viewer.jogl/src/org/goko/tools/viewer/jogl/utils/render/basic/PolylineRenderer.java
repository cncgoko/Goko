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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

/**
 * A simple polyline render
 *
 * @author PsyKo
 *
 */
public class PolylineRenderer extends AbstractLineRenderer{
	private boolean closed;
	private Color4f color;
	private List<Point3d> points;

	public PolylineRenderer(){
		super(GL.GL_LINES, VERTICES | COLORS);
	}

	public PolylineRenderer(boolean closed, Color4f color, Point3d... pointsArray) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.color = color;
		this.closed = closed;
		if(pointsArray == null || pointsArray.length < 2){
			throw new RuntimeException("At least 2 points required");
		}
		this.points = new ArrayList<Point3d>();
		for (Point3d point3d : pointsArray) {
			points.add(point3d);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		if(closed){
			setVerticesCount(CollectionUtils.size(points) + 1);
		}else{
			setVerticesCount(CollectionUtils.size(points) );
		}
		FloatBuffer vertices = FloatBuffer.allocate(getVerticesCount() * 4);
		FloatBuffer colors = FloatBuffer.allocate(getVerticesCount() * 4);

		if(CollectionUtils.isNotEmpty(points)){
			for (Point3d p : points) {
				vertices.put(new float[]{(float)p.x, (float)p.y, (float)p.z, 1});
				colors.put(new float[]{color.x, color.y, color.z, color.w});
			}

			if(closed){
				Point3d p = points.get(0);
				vertices.put(new float[]{(float)p.x, (float)p.y, (float)p.z, 1});
				colors.put(new float[]{color.x, color.y, color.z, color.w});
			}
		}
		vertices.rewind();
		colors.rewind();
		setVerticesBuffer(vertices);
		setColorsBuffer(colors);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.SHADED_FLAT_SHADER);
	}

	/**
	 * @return the closed
	 */
	protected boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	protected void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @return the color
	 */
	protected Color4f getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	protected void setColor(Color4f color) {
		this.color = color;
	}

	/**
	 * @return the points
	 */
	protected List<Point3d> getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	protected void setPoints(List<Point3d> points) {
		this.points = points;
	}

}
