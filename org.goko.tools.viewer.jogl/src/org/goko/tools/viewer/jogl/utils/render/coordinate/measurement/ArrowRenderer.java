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

package org.goko.tools.viewer.jogl.utils.render.coordinate.measurement;

import java.nio.FloatBuffer;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

public class ArrowRenderer extends AbstractVboJoglRenderer{
	private Point3d position;
	private Vector3d direction;
	private Vector3d base;
	private Color4f color;
	private float directionScale;
	private float baseScale;

	public ArrowRenderer(Point3d position, Vector3d direction, Vector3d base, Color4f color) {
		this(position, direction, base, color, 2, 0.5f);
	}

	public ArrowRenderer(Point3d position, Vector3d direction, Vector3d base,Color4f color, float directionScale, float baseScale) {
		super(GL.GL_TRIANGLES, VERTICES | COLORS);
		this.position  = new Point3d(position);
		this.direction = new Vector3d(direction);
		this.direction.normalize();
		this.base  	   = new Vector3d(base);
		this.base.normalize();
		this.directionScale = directionScale;
		this.baseScale 		= baseScale;
		this.color = color;
	}
	/**
	 * @throws GkException
	 */
	@Override
	protected void buildGeometry() throws GkException {
		setVerticesCount(3);
		FloatBuffer vertices = FloatBuffer.allocate(getVerticesCount() * 4);
		FloatBuffer colors 	 = FloatBuffer.allocate(getVerticesCount() * 4);

		// Head of the arrow
		vertices.put(new float[]{(float) position.x,(float) position.y,(float) position.z,1});

		vertices.put(new float[]{(float) (position.x - directionScale * direction.x - baseScale * base.x),
								 (float) (position.y - directionScale * direction.y - baseScale * base.y),
								 (float) (position.z - directionScale * direction.z - baseScale * base.z),
								 1});
		vertices.put(new float[]{(float) (position.x - directionScale * direction.x + baseScale * base.x),
								 (float) (position.y - directionScale * direction.y + baseScale * base.y),
								 (float) (position.z - directionScale * direction.z + baseScale * base.z),
								 1});

		colors.put(new float[]{color.x,color.y,color.z,color.w});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		colors.put(new float[]{color.x,color.y,color.z,color.w});

		vertices.rewind();
		colors.rewind();
		setVerticesBuffer(vertices);
		setColorsBuffer(colors);
	}

	/**
	 * @param gl
	 * @return
	 * @throws GkException
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.SHADED_FLAT_SHADER);
	}

}
