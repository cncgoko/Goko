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
package org.goko.tools.viewer.jogl.utils.render.coordinate;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Color3f;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class ThreeAxisRenderer extends AbstractLineRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.AxisRenderer";
	private float scale;
	private Color3f xColor;
	private Color3f yColor;
	private Color3f zColor;

	public ThreeAxisRenderer() {
		this(10, new Color3f(1f,0,0), new Color3f(0,1f,0), new Color3f(0,0,1f));
	}

	public ThreeAxisRenderer(float scale, Color3f colorX, Color3f colorY, Color3f colorZ) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.scale = scale;
		this.xColor = colorX;
		this.yColor = colorY;
		this.zColor = colorZ;
	}
	/**
	 * (inheritDoc)
	 *
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		setVerticesCount(6);
		FloatBuffer axisVertices = FloatBuffer.allocate(getVerticesCount()*4);		
		axisVertices.put(new float[]{0f,0f,0f,1f});
		axisVertices.put(new float[]{scale,0f,0f,1f});
		axisVertices.put(new float[]{0f,0f,0f,1f});
		axisVertices.put(new float[]{0f,scale,0f,1f});
		axisVertices.put(new float[]{0f,0f,0f,1f});
		axisVertices.put(new float[]{0f,0f,scale,1f});
		axisVertices.rewind();
		setVerticesBuffer(axisVertices);

//		FloatBuffer axisColors = FloatBuffer.allocate(getVerticesCount()*4);
		FloatBuffer axisColors = FloatBuffer.allocate(6*4);
		axisColors.put(new float[]{xColor.x,xColor.y,xColor.z,1f});
		axisColors.put(new float[]{xColor.x,xColor.y,xColor.z,1f});

		axisColors.put(new float[]{yColor.x,yColor.y,yColor.z,1f});
		axisColors.put(new float[]{yColor.x,yColor.y,yColor.z,1f});

		axisColors.put(new float[]{zColor.x,zColor.y,zColor.z,1f});
		axisColors.put(new float[]{zColor.x,zColor.y,zColor.z,1f});

		axisColors.rewind();
		setColorsBuffer(axisColors);
	}

}