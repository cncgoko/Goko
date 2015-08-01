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
package org.goko.viewer.jogl.utils.render;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point4d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.viewer.jogl.service.JoglUtils;
import org.goko.viewer.jogl.service.Layer;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class GridRenderer extends AbstractVboJoglRenderer {
	public static final String ID = "org.goko.viewer.jogl.utils.render.GridRenderer";
	private double size = 100;
	private Quantity<Length> majorIncrement;
	private Quantity<Length> minorIncrement;
	private Point4d majorUnitColor	= new Point4d(0.4, 0.4, 0.4, 0.45);
	private Point4d minorUnitColor	= new Point4d(0.4, 0.4, 0.4, 0.15);
	private Point4d originColor		= new Point4d(0.8, 0.8, 0.8, 0.7);
	private Tuple6b start;
	private Tuple6b end;
	
	/**
	 * Constructor
	 */
	public GridRenderer(Tuple6b start, Tuple6b end, Quantity<Length> majorIncrement, Quantity<Length> minorIncrement) {
		super(GL.GL_LINES,  COLORS | VERTICES);
		this.setLayerId(Layer.LAYER_GRIDS);
		this.start 			= start.to(JoglUtils.JOGL_UNIT);
		this.end 			= end.to(JoglUtils.JOGL_UNIT);
		this.majorIncrement = majorIncrement.to(JoglUtils.JOGL_UNIT);
		this.minorIncrement = minorIncrement.to(JoglUtils.JOGL_UNIT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	private void buildGrid() throws GkException{
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Point4d> lstColors = new ArrayList<Point4d>();

		// Origin
		lstVertices.add(new Point4d(0, -size, 0,1));
		lstVertices.add(new Point4d(0,  size, 0,1));
		lstVertices.add(new Point4d(-size, 0, 0,1));
		lstVertices.add(new Point4d( size, 0, 0,1));
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		Tuple6b delta = new Tuple6b(end);
		delta = delta.subtract(start);
		int xMajor = delta.getX().divide(majorIncrement).intValue();
		int yMajor = delta.getY().divide(majorIncrement).intValue();
		
		// Main divisions
		for (int i = 0; i <= xMajor; i++) {
			lstVertices.add(new Point4d(start.getX().doubleValue()+i*majorIncrement.doubleValue(), start.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(start.getX().doubleValue()+i*majorIncrement.doubleValue(), end.getY().doubleValue() , 0,1));			
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}

		for (int i = 0; i <= yMajor; i++) {
			lstVertices.add(new Point4d(start.getX().doubleValue(), start.getY().doubleValue()+i*majorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(end.getX().doubleValue() ,  start.getY().doubleValue()+i*majorIncrement.doubleValue(), 0,1));
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		int xMinor = delta.getX().divide(minorIncrement).intValue();
		int yMinor = delta.getY().divide(minorIncrement).intValue();
		
		// Subdivisions
		for (int i = 0; i <= xMinor; i++) {
			lstVertices.add(new Point4d(start.getX().doubleValue()+i*minorIncrement.doubleValue(), start.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(start.getX().doubleValue()+i*minorIncrement.doubleValue(), end.getY().doubleValue() , 0,1));			
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}

		for (int i = 0; i <= yMinor; i++) {
			lstVertices.add(new Point4d(start.getX().doubleValue(), start.getY().doubleValue()+i*minorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(end.getX().doubleValue() ,  start.getY().doubleValue()+i*minorIncrement.doubleValue(), 0,1));
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		setVerticesCount(CollectionUtils.size(lstVertices));
		setColorsBuffer(JoglUtils.buildFloatBuffer4d(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		buildGrid();
	}
	
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.LINE_SHADER);
	}

	@Override
	protected void updateShaderData(GL3 gl) throws GkException {		
		super.updateShaderData(gl);
		gl.glLineWidth(1);
		gl.glEnable(GL3.GL_LINE_SMOOTH);
	}
	
	@Override
	protected void performRender(GL3 gl) throws GkException {		
		super.performRender(gl);
	}
}
