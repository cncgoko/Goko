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
package org.goko.viewer.jogl.utils.render.coordinate;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.viewer.jogl.preferences.JoglViewerPreference.EnumRotaryAxisDirection;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 */
public class FourAxisRenderer extends AbstractVboJoglRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.AxisRenderer";
	public static final int ROTATION_AROUND_X = 1;
	public static final int ROTATION_AROUND_Y = 2;
	public static final int ROTATION_AROUND_Z = 4;
	private EnumRotaryAxisDirection rotationAxis;
	private int rotationArcVerticesCount;
	private float scale;
	private Color3f xColor;
	private Color3f yColor;
	private Color3f zColor;
	private Color3f aColor;
	private boolean displayRotaryAxis;

	public FourAxisRenderer() {
		this(10, EnumRotaryAxisDirection.X, new Color3f(1f,0,0), new Color3f(0,1f,0), new Color3f(0,0,1f), new Color3f(1f,1f,0));
	}

	public FourAxisRenderer(float scale, EnumRotaryAxisDirection rotationAxis, Color3f colorX, Color3f colorY, Color3f colorZ, Color3f colorA) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.rotationAxis = rotationAxis;
		this.scale = scale;
		this.xColor = colorX;
		this.yColor = colorY;
		this.zColor = colorZ;
		this.aColor = colorA;
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
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		this.rotationArcVerticesCount = 16;
		setVerticesCount(6 + rotationArcVerticesCount);
		FloatBuffer axisVertices = FloatBuffer.allocate(getVerticesCount()*4);
		axisVertices.put(new float[]{0f,0f,0.01f,1f});
		axisVertices.put(new float[]{scale,0f,0.01f,1f});
		axisVertices.put(new float[]{0f,0f,0.01f,1f});
		axisVertices.put(new float[]{0f,scale,0.01f,1f});
		axisVertices.put(new float[]{0f,0f,0f,1f});
		axisVertices.put(new float[]{0f,0f,scale,1f});


		FloatBuffer axisColors = FloatBuffer.allocate(getVerticesCount()*4);
		axisColors.put(new float[]{xColor.x,xColor.y,xColor.z,1f});
		axisColors.put(new float[]{xColor.x,xColor.y,xColor.z,1f});

		axisColors.put(new float[]{yColor.x,yColor.y,yColor.z,1f});
		axisColors.put(new float[]{yColor.x,yColor.y,yColor.z,1f});

		axisColors.put(new float[]{zColor.x,zColor.y,zColor.z,1f});
		axisColors.put(new float[]{zColor.x,zColor.y,zColor.z,1f});

		// Generate the 4th axis arc
		buildRotaryAxisArc(axisVertices, axisColors);

		axisVertices.rewind();
		setVerticesBuffer(axisVertices);
		axisColors.rewind();
		setColorsBuffer(axisColors);
	}

	private void buildRotaryAxisArc(FloatBuffer axisVertices, FloatBuffer axisColors){
		if(!displayRotaryAxis){
			for(int i = 0 ; i < this.rotationArcVerticesCount ; i++){
				axisVertices.put(new float[]{0,0,0,1});
				axisColors.put(new float[]{0,0,0,0});
			}
		}else{
			if(this.rotationAxis == EnumRotaryAxisDirection.X){
				buildRotaryAxisArcAroundX(axisVertices, axisColors);
			}else if(this.rotationAxis == EnumRotaryAxisDirection.Y){
				buildRotaryAxisArcAroundY(axisVertices, axisColors);
			}else{
				buildRotaryAxisArcAroundZ(axisVertices, axisColors);
			}
		}
	}

	private void buildRotaryAxisArcAroundX(FloatBuffer axisVertices, FloatBuffer axisColors){
		Point3f vertex = new Point3f();
		float rotationAngle = (float) (Math.PI / 2) / (this.rotationArcVerticesCount - 1);

		for(int i = 0 ; i < this.rotationArcVerticesCount ; i++){
			vertex.x = 0;
			vertex.y = (float) (scale/2 * (Math.cos(i * rotationAngle)));
			vertex.z = (float) (scale/2 * (Math.sin(i * rotationAngle)));
			axisVertices.put(new float[]{vertex.x,vertex.y,vertex.z,1f});
			axisColors.put(new float[]{aColor.x,aColor.y,aColor.z,1f});
		}
	}

	private void buildRotaryAxisArcAroundY(FloatBuffer axisVertices, FloatBuffer axisColors){
		Point3f vertex = new Point3f();
		float rotationAngle = (float) (Math.PI / 2) / (this.rotationArcVerticesCount - 1);

		for(int i = 0 ; i < this.rotationArcVerticesCount ; i++){
			vertex.x = (float) (scale/2 * (Math.cos(i * rotationAngle)));
			vertex.y = 0;
			vertex.z = (float) (scale/2 * (Math.sin(i * rotationAngle)));
			axisVertices.put(new float[]{vertex.x,vertex.y,vertex.z,1f});
			axisColors.put(new float[]{aColor.x,aColor.y,aColor.z,1f});
		}
	}

	private void buildRotaryAxisArcAroundZ(FloatBuffer axisVertices, FloatBuffer axisColors){
		Point3f vertex = new Point3f();
		float rotationAngle = (float) (Math.PI / 2) / (this.rotationArcVerticesCount - 1);

		for(int i = 0 ; i < this.rotationArcVerticesCount ; i++){
			vertex.x = (float) (scale/2 * (Math.cos(i * rotationAngle)));
			vertex.y = (float) (scale/2 * (Math.sin(i * rotationAngle)));
			vertex.z = 0;
			axisVertices.put(new float[]{vertex.x,vertex.y,vertex.z,1f});
			axisColors.put(new float[]{aColor.x,aColor.y,aColor.z,1f});
		}
	}

	/**
	 * @return the rotationAxis
	 */
	protected EnumRotaryAxisDirection getRotationAxis() {
		return rotationAxis;
	}

	/**
	 * @param rotationAxis the rotationAxis to set
	 * @throws GkException GkException
	 */
	public void setRotationAxis(EnumRotaryAxisDirection rotationAxis) throws GkException {
		this.rotationAxis = rotationAxis;
		buildGeometry();
		this.updateBufferObjects();
	}

	/**
	 * @return the displayRotaryAxis
	 */
	public boolean isDisplayRotaryAxis() {
		return displayRotaryAxis;
	}

	/**
	 * @param displayRotaryAxis the displayRotaryAxis to set
	 */
	public void setDisplayRotaryAxis(boolean displayRotaryAxis) {
		this.displayRotaryAxis = displayRotaryAxis;
	}
}