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
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

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
	private String id;
	private Quantity<Length> majorIncrement;
	private Quantity<Length> minorIncrement;
	private Color4f majorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f minorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f originColor		= new Color4f(0.8f, 0.8f, 0.8f,1f);
	private Tuple6b start;
	private Tuple6b end;
	private float opacity;
	private Matrix4f axisTransformMatrix; 
	private Vector4f normal; 
	
	public GridRenderer(String id){		
		this(id, new Tuple6b(-100, -100, 0, JoglUtils.JOGL_UNIT), 
				 new Tuple6b(100, 100, 0, JoglUtils.JOGL_UNIT),
				 NumberQuantity.of(10, JoglUtils.JOGL_UNIT),
				 NumberQuantity.of(1, JoglUtils.JOGL_UNIT),
				 new Color3f(0.4f, 0.4f, 0.4f),
				 new Color3f(0.4f, 0.4f, 0.4f),
				 0.5f, new Vector4f(0f,0f,1f,0f));
	}
			
	/**
	 * Constructor
	 */
	public GridRenderer(String id, Tuple6b start, Tuple6b end, Quantity<Length> majorIncrement, Quantity<Length> minorIncrement, Color3f majorColor, Color3f minorColor, float opacity, Vector4f normal) {
		super(GL.GL_LINES,  COLORS | VERTICES);
		this.id = id;
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
		this.normal = new Vector4f(normal);
		buildMatrix();
		setUseAlpha(true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	private void buildGrid() throws GkException{
		buildMatrix();		
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Color4f> lstColors = new ArrayList<Color4f>();
		
		Tuple6b lclStart6b 			= new Tuple6b();
		lclStart6b.min(start, end);		
		Tuple6b lclEnd6b 			= new Tuple6b();
		lclEnd6b.max(start, end);
		Tuple6b lclCenter6b = new Tuple6b(0,0,0, JoglUtils.JOGL_UNIT);
		
		// Determine min/max if zero is not in the desired area
		lclCenter6b = lclCenter6b.max(lclStart6b).min(lclEnd6b);
				
		
		Point3f lclStart = lclStart6b.toPoint3f();
		axisTransformMatrix.transform(lclStart);
		Point3f lclEnd   = lclEnd6b.toPoint3f();
		axisTransformMatrix.transform(lclEnd);
		Point3f lclCenter   = lclCenter6b.toPoint3f();
		axisTransformMatrix.transform(lclCenter);
		Matrix4d invAxisTransformMatrix = new Matrix4d(axisTransformMatrix);
		invAxisTransformMatrix.invert();

	//	prendre en compte les préférences dans le scene manager (objet grid reste générique)
		
		addVertice(new Point4d(lclCenter.x, lclStart.y, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
		addVertice(new Point4d(lclCenter.x, lclEnd.y, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
		addVertice(new Point4d(lclStart.x, lclCenter.y, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
		addVertice(new Point4d(lclEnd.x, lclCenter.y, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		
		Tuple6b deltaPlus  =  lclEnd6b.subtract(lclCenter6b).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		Tuple6b deltaMinus =  lclCenter6b.subtract(lclStart6b).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		
		// Major divisions
		int nbStepXPlusMajor = Math.abs(deltaPlus.getX().divide(majorIncrement).intValue());
		int nbStepYPlusMajor = Math.abs(deltaPlus.getY().divide(majorIncrement).intValue());
		int nbStepZPlusMajor = Math.abs(deltaPlus.getZ().divide(majorIncrement).intValue());
		
		int nbStepXMinusMajor = Math.abs(deltaMinus.getX().divide(majorIncrement).intValue());
		int nbStepYMinusMajor = Math.abs(deltaMinus.getY().divide(majorIncrement).intValue());
		int nbStepZMinusMajor = Math.abs(deltaMinus.getZ().divide(majorIncrement).intValue());
				
		Vector3f nbStepPlusMajor = new Vector3f(nbStepXPlusMajor, nbStepYPlusMajor, nbStepZPlusMajor);
		Vector3f nbStepMinusMajor = new Vector3f(nbStepXMinusMajor, nbStepYMinusMajor, nbStepZMinusMajor);
		axisTransformMatrix.transform(nbStepPlusMajor);
		axisTransformMatrix.transform(nbStepMinusMajor);
		
		for (int i = 1; i <= nbStepPlusMajor.x; i++) {
			addVertice(new Point4d(lclCenter.x+i*majorIncrement.doubleValue(), lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);			
			addVertice(new Point4d(lclCenter.x+i*majorIncrement.doubleValue(), lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);		
			
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepMinusMajor.x; i++) {
			addVertice(new Point4d(lclCenter.x-i*majorIncrement.doubleValue(), lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x-i*majorIncrement.doubleValue(), lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepPlusMajor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y+i*majorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y+i*majorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		for (int i = 1; i <= nbStepMinusMajor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y-i*majorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y-i*majorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		// Minor divisions
		int nbStepXPlusMinor = Math.abs(deltaPlus.getX().divide(minorIncrement).intValue());
		int nbStepYPlusMinor = Math.abs(deltaPlus.getY().divide(minorIncrement).intValue());
		int nbStepZPlusMinor = Math.abs(deltaPlus.getZ().divide(minorIncrement).intValue());
		
		int nbStepXMinusMinor = Math.abs(deltaMinus.getX().divide(minorIncrement).intValue());
		int nbStepYMinusMinor = Math.abs(deltaMinus.getY().divide(minorIncrement).intValue());
		int nbStepZMinusMinor = Math.abs(deltaMinus.getZ().divide(minorIncrement).intValue());
		
		Vector3f nbStepPlusMinor = new Vector3f(nbStepXPlusMinor, nbStepYPlusMinor, nbStepZPlusMinor);
		Vector3f nbStepMinusMinor = new Vector3f(nbStepXMinusMinor, nbStepYMinusMinor, nbStepZMinusMinor);
		
		axisTransformMatrix.transform(nbStepPlusMinor);
		axisTransformMatrix.transform(nbStepMinusMinor);
		
		for (int i = 1; i <= nbStepPlusMinor.x; i++) {
			addVertice(new Point4d(lclCenter.x+i*minorIncrement.doubleValue(), lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x+i*minorIncrement.doubleValue(), lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepMinusMinor.x; i++) {
			addVertice(new Point4d(lclCenter.x-i*minorIncrement.doubleValue(), lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x-i*minorIncrement.doubleValue(), lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepPlusMinor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y+i*minorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y+i*minorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		for (int i = 1; i <= nbStepMinusMinor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y-i*minorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y-i*minorIncrement.doubleValue(), lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		setVerticesCount(CollectionUtils.size(lstVertices));
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));		
	}
	
	private void addVertice(Point4d p, List<Point4d> buffer, Matrix4d invMatrix){
		invMatrix.transform(p);
		buffer.add(p);
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
	
	private void buildMatrix(){
		
		if(normal.dot(JoglUtils.X_AXIS) == 1){ // 
			this.axisTransformMatrix = new Matrix4f(new float[]{0.0f, 1.0f, 0.0f, 0.0f,																										
																0.0f, 0.0f, 1.0f, 0.0f,
																1.0f, 0.0f, 0.0f, 0.0f,
																0.0f, 0.0f, 0.0f, 1.0f});
		}else if(normal.dot(JoglUtils.Y_AXIS) == 1){ //
			this.axisTransformMatrix = new Matrix4f(new float[]{1.0f, 0.0f, 0.0f, 0.0f,
																0.0f, 0.0f, 1.0f, 0.0f,
																0.0f, 1.0f, 0.0f, 0.0f,
																0.0f, 0.0f, 0.0f, 1.0f});
		}else { //
			this.axisTransformMatrix = new Matrix4f(new float[]{1.0f, 0.0f, 0.0f, 0.0f,																										
																0.0f, 1.0f, 0.0f, 0.0f,
																0.0f, 0.0f, 1.0f, 0.0f,
																0.0f, 0.0f, 0.0f, 1.0f});
		}
	}

	/**
	 * @return the normal
	 */
	public Vector4f getNormal() {
		return normal;
	}

	/**
	 * @param normal the normal to set
	 */
	public void setNormal(Vector4f normal) {
		this.normal = normal;
	}
	
}
