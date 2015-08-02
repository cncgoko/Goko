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
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point4d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
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
	private Quantity<Length> majorIncrement;
	private Quantity<Length> minorIncrement;
	private Color4f majorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f minorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f originColor		= new Color4f(0.8f, 0.8f, 0.8f,1f);
	private Tuple6b start;
	private Tuple6b end;
	private float opacity;
	
	public GridRenderer(){
		this(new Tuple6b(-100, -100, 0, JoglUtils.JOGL_UNIT), 
				 new Tuple6b(100, 100, 0, JoglUtils.JOGL_UNIT),
				 NumberQuantity.of(10, JoglUtils.JOGL_UNIT),
				 NumberQuantity.of(1, JoglUtils.JOGL_UNIT),
				 new Color3f(0.4f, 0.4f, 0.4f),
				 new Color3f(0.4f, 0.4f, 0.4f),
				 0.5f);
	}
			
	/**
	 * Constructor
	 */
	public GridRenderer(Tuple6b start, Tuple6b end, Quantity<Length> majorIncrement, Quantity<Length> minorIncrement, Color3f majorColor, Color3f minorColor, float opacity) {
		super(GL.GL_LINES,  COLORS | VERTICES);
		this.setLayerId(Layer.LAYER_GRIDS);		
		this.start 			= new Tuple6b();
		this.start.min(start, end);
		this.end 			= new Tuple6b();
		this.end.max(start, end);
		this.majorIncrement = majorIncrement.to(JoglUtils.JOGL_UNIT);
		this.minorIncrement = minorIncrement.to(JoglUtils.JOGL_UNIT);
		this.majorUnitColor = new Color4f(majorColor.x,majorColor.y,majorColor.z,opacity);
		this.minorUnitColor = new Color4f(minorColor.x,minorColor.y,minorColor.z,opacity);
		this.originColor.w = opacity;
		this.opacity = opacity;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	private void buildGrid2() throws GkException{
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Color4f> lstColors = new ArrayList<Color4f>();
		
		Tuple6b lclStart 			= new Tuple6b();
		lclStart.min(start, end);
		Tuple6b lclEnd 			= new Tuple6b();
		lclEnd.max(start, end);
		
		Tuple6b center = new Tuple6b(0,0,0, JoglUtils.JOGL_UNIT);
		// Determine min/max if zero is not in the desired area
		center = center.max(lclStart).min(lclEnd);
		
	//	prendre en compte les préférences dans le scene manager (objet grid reste générique)
		
		lstVertices.add(new Point4d(center.getX().doubleValue(), lclStart.getY().doubleValue(), 0,1));
		lstVertices.add(new Point4d(center.getX().doubleValue(), lclEnd.getY().doubleValue(), 0,1));
		lstVertices.add(new Point4d(lclStart.getX().doubleValue(), center.getY().doubleValue(), 0,1));
		lstVertices.add(new Point4d(lclEnd.getX().doubleValue(), center.getY().doubleValue(), 0,1));
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		
		Tuple6b deltaPlus  =  lclEnd.subtract(center).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		Tuple6b deltaMinus =  center.subtract(lclStart).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		
		// Major divisions
		int nbStepXPlusMajor = Math.abs(deltaPlus.getX().divide(majorIncrement).intValue());
		int nbStepYPlusMajor = Math.abs(deltaPlus.getY().divide(majorIncrement).intValue());
		
		int nbStepXMinusMajor = Math.abs(deltaMinus.getX().divide(majorIncrement).intValue());
		int nbStepYMinusMajor = Math.abs(deltaMinus.getY().divide(majorIncrement).intValue());
		
		for (int i = 0; i <= nbStepXPlusMajor; i++) {
			lstVertices.add(new Point4d(center.getX().doubleValue()+i*majorIncrement.doubleValue(), lclStart.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(center.getX().doubleValue()+i*majorIncrement.doubleValue(), lclEnd.getY().doubleValue() , 0,1));			
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepXMinusMajor; i++) {
			lstVertices.add(new Point4d(center.getX().doubleValue()-i*majorIncrement.doubleValue(), lclStart.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(center.getX().doubleValue()-i*majorIncrement.doubleValue(), lclEnd.getY().doubleValue() , 0,1));			
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 0; i <= nbStepYPlusMajor; i++) {
			lstVertices.add(new Point4d(lclStart.getX().doubleValue(), center.getY().doubleValue()+i*majorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(lclEnd.getX().doubleValue() ,  center.getY().doubleValue()+i*majorIncrement.doubleValue(), 0,1));
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		for (int i = 1; i <= nbStepYMinusMajor; i++) {
			lstVertices.add(new Point4d(lclStart.getX().doubleValue(), center.getY().doubleValue()-i*majorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(lclEnd.getX().doubleValue() ,  center.getY().doubleValue()-i*majorIncrement.doubleValue(), 0,1));
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		// Minor divisions
		int nbStepXPlusMinor = Math.abs(deltaPlus.getX().divide(minorIncrement).intValue());
		int nbStepYPlusMinor = Math.abs(deltaPlus.getY().divide(minorIncrement).intValue());
		
		int nbStepXMinusMinor = Math.abs(deltaMinus.getX().divide(minorIncrement).intValue());
		int nbStepYMinusMinor = Math.abs(deltaMinus.getY().divide(minorIncrement).intValue());
		
		for (int i = 0; i <= nbStepXPlusMinor; i++) {
			lstVertices.add(new Point4d(center.getX().doubleValue()+i*minorIncrement.doubleValue(), lclStart.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(center.getX().doubleValue()+i*minorIncrement.doubleValue(), lclEnd.getY().doubleValue() , 0,1));			
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 0; i <= nbStepXMinusMinor; i++) {
			lstVertices.add(new Point4d(center.getX().doubleValue()-i*minorIncrement.doubleValue(), lclStart.getY().doubleValue() , 0,1));
			lstVertices.add(new Point4d(center.getX().doubleValue()-i*minorIncrement.doubleValue(), lclEnd.getY().doubleValue() , 0,1));			
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 0; i <= nbStepYPlusMinor; i++) {
			lstVertices.add(new Point4d(lclStart.getX().doubleValue(), center.getY().doubleValue()+i*minorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(lclEnd.getX().doubleValue() ,  center.getY().doubleValue()+i*minorIncrement.doubleValue(), 0,1));
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		for (int i = 0; i <= nbStepYMinusMinor; i++) {
			lstVertices.add(new Point4d(lclStart.getX().doubleValue(), center.getY().doubleValue()-i*minorIncrement.doubleValue(), 0,1));
			lstVertices.add(new Point4d(lclEnd.getX().doubleValue() ,  center.getY().doubleValue()-i*minorIncrement.doubleValue(), 0,1));
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		setVerticesCount(CollectionUtils.size(lstVertices));
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));
	}
	

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		buildGrid2();
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

	/**
	 * @return the opacity
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * @param opacity the opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
		this.majorUnitColor.w = opacity;
		this.minorUnitColor.w = opacity;
		this.originColor.w = opacity;
	}

	/**
	 * @return the majorIncrement
	 */
	public Quantity<Length> getMajorIncrement() {
		return majorIncrement;
	}

	/**
	 * @param majorIncrement the majorIncrement to set
	 */
	public void setMajorIncrement(Quantity<Length> majorIncrement) {
		this.majorIncrement = majorIncrement;
	}

	/**
	 * @return the minorIncrement
	 */
	public Quantity<Length> getMinorIncrement() {
		return minorIncrement;
	}

	/**
	 * @param minorIncrement the minorIncrement to set
	 */
	public void setMinorIncrement(Quantity<Length> minorIncrement) {
		this.minorIncrement = minorIncrement;
	}

	/**
	 * @param majorUnitColor the majorUnitColor to set
	 */
	public void setMajorUnitColor(Color3f majorUnitColor) {
		this.majorUnitColor.x = majorUnitColor.x;
		this.majorUnitColor.y = majorUnitColor.y;
		this.majorUnitColor.z = majorUnitColor.z;
	}


	/**
	 * @param minorUnitColor the minorUnitColor to set
	 */
	public void setMinorUnitColor(Color3f minorUnitColor) {
		this.minorUnitColor.x = minorUnitColor.x;
		this.minorUnitColor.y = minorUnitColor.y;
		this.minorUnitColor.z = minorUnitColor.z;
	}

	/**
	 * @return the originColor
	 */
	public Color4f getOriginColor() {
		return originColor;
	}

	/**
	 * @param originColor the originColor to set
	 */
	public void setOriginColor(Color4f originColor) {
		this.originColor = originColor;
	}

	/**
	 * @return the start
	 */
	public Tuple6b getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Tuple6b start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Tuple6b getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Tuple6b end) {
		this.end = end;
	}
	
	
	
}
