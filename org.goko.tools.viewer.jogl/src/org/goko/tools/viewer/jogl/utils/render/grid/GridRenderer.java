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
package org.goko.tools.viewer.jogl.utils.render.grid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.service.Layer;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class GridRenderer extends AbstractVboJoglRenderer implements IGCodeContextListener<GCodeContext>, IGridRenderer {
	private static final GkLog LOG = GkLog.getLogger(GridRenderer.class);
	private Length majorIncrement;
	private Length minorIncrement;	
	private Matrix4f axisTransformMatrix; 
	private Vector3f normal;
	private Vector3f horizontalVector;
	private Length horizontalMinimal;
	private Length horizontalMaximal;
	private Color4f horizontalColor;
	private Vector3f verticalVector;
	private Length verticalMinimal;
	private Length verticalMaximal;
	private Color4f verticalColor;
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	 
	private Color4f majorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f minorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f originColor		= new Color4f(0.8f, 0.8f, 0.8f,1f);
	private float majorOpacity;
	private float minorOpacity;
	private float axisOpacity;
	
	public GridRenderer(IGCodeContextProvider<GCodeContext> gcodeContextProvider){		
		this(new Vector3f(0f,0f,1f),
				Length.valueOf(10, JoglUtils.JOGL_UNIT), Length.valueOf(1, JoglUtils.JOGL_UNIT),
				new Vector3f(1f,0f,0f), Length.valueOf(-100, JoglUtils.JOGL_UNIT), Length.valueOf(100, JoglUtils.JOGL_UNIT),
				new Vector3f(0f,1f,0f), Length.valueOf(-100, JoglUtils.JOGL_UNIT), Length.valueOf(100, JoglUtils.JOGL_UNIT),
				new Color3f(0.4f, 0.4f, 0.4f), new Color3f(0.4f, 0.4f, 0.4f), 0.5f, 0.5f, 0.5f);

		this.gcodeContextProvider = gcodeContextProvider;
		if(this.gcodeContextProvider != null){
			gcodeContextProvider.addObserver(this);
		}
	}
				
	/**	
	 * @param normal
	 * @param majorIncrement
	 * @param minorIncrement
	 * @param horizontalVector
	 * @param horizontalMinimal
	 * @param horizontalMaximal
	 * @param verticalVector
	 * @param verticalMinimal
	 * @param verticalMaximal
	 * @param majorUnitColor
	 * @param minorUnitColor
	 * @param originColor
	 * @param majorOpacity
	 * @param minorOpacity
	 * @param axisOpacity
	 */
	public GridRenderer(Vector3f normal, Length majorIncrement, Length minorIncrement, 
			Vector3f horizontalVector, Length horizontalMinimal, Length horizontalMaximal,
			Vector3f verticalVector, Length verticalMinimal, Length verticalMaximal,
			Color3f majorUnitColor, Color3f minorUnitColor, 
			float majorOpacity, float minorOpacity, 
			float axisOpacity) {		
		super(GL.GL_LINES,  COLORS | VERTICES);
		this.setLayerId(Layer.LAYER_GRIDS);	
		this.majorIncrement = majorIncrement;
		this.minorIncrement = minorIncrement;
		this.normal = new Vector3f(normal);
		this.horizontalVector = horizontalVector;
		this.horizontalMinimal = horizontalMinimal;
		this.horizontalMaximal = horizontalMaximal;
		this.verticalVector = verticalVector;
		this.verticalMinimal = verticalMinimal;
		this.verticalMaximal = verticalMaximal;
		this.majorUnitColor = new Color4f(majorUnitColor.x,majorUnitColor.y,majorUnitColor.z,majorOpacity);
		this.minorUnitColor = new Color4f(minorUnitColor.x,minorUnitColor.y,minorUnitColor.z,minorOpacity);
		this.majorOpacity = majorOpacity;
		this.minorOpacity = minorOpacity;
		this.axisOpacity = axisOpacity;
		buildMatrix();
		setUseAlpha(true);
	}

//	/**
//	 * Constructor
//	 */
//	public GridRenderer(Tuple6b start, Tuple6b end, Length majorIncrement, Length minorIncrement, Color3f majorColor, Color3f minorColor, float minorOpacity, float majorOpacity, float axisOpacity, Vector4f normal) {
//		super(GL.GL_LINES,  COLORS | VERTICES);
//		this.setLayerId(Layer.LAYER_GRIDS);		
//		this.majorIncrement = majorIncrement.to(JoglUtils.JOGL_UNIT);
//		this.minorIncrement = minorIncrement.to(JoglUtils.JOGL_UNIT);
//		this.majorUnitColor = new Color4f(majorColor.x,majorColor.y,majorColor.z,majorOpacity);
//		this.minorUnitColor = new Color4f(minorColor.x,minorColor.y,minorColor.z,minorOpacity);
//		this.originColor.w = axisOpacity;
//		this.minorOpacity = minorOpacity;
//		this.majorOpacity = majorOpacity;
//		this.axisOpacity = axisOpacity;
//		this.normal = new Vector3f(normal.x, normal.y, normal.z);
//		buildMatrix();
//		setUseAlpha(true);
//	}

	private void buildGrid() throws GkException{
		buildMatrix();		
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Color4f> lstColors   = new ArrayList<Color4f>();
	
		Matrix4d invAxisTransformMatrix = new Matrix4d(axisTransformMatrix);
		invAxisTransformMatrix.invert();
		
		double hMin = horizontalMinimal.doubleValue(JoglUtils.JOGL_UNIT);
		double hMax = horizontalMaximal.doubleValue(JoglUtils.JOGL_UNIT);
		
		double vMin = verticalMinimal.doubleValue(JoglUtils.JOGL_UNIT);
		double vMax = verticalMaximal.doubleValue(JoglUtils.JOGL_UNIT);
		
		int hStart = horizontalMinimal.divide(majorIncrement).setScale(0, RoundingMode.DOWN).intValue();
		int hEnd   = horizontalMaximal.divide(majorIncrement).setScale(0, RoundingMode.DOWN).intValue();

		int nbMinorPerMajor = (int) majorIncrement.divide(minorIncrement).floatValue();
		double dMajorIncrement = majorIncrement.doubleValue(JoglUtils.JOGL_UNIT);
		double dMinorIncrement = minorIncrement.doubleValue(JoglUtils.JOGL_UNIT);
		
		// Draw origin		
		Point4d originHorizontalMin = new Point4d( hMin, 0, 0 , 1);
		Point4d originHorizontalMax = new Point4d( hMax, 0, 0 , 1);
		Point4d originVerticalMin   = new Point4d( 0, vMin, 0 , 1);
		Point4d originVerticalMax   = new Point4d( 0, vMax, 0 , 1);
		
		addVertice(originHorizontalMin, lstVertices, invAxisTransformMatrix);
		addVertice(originHorizontalMax, lstVertices, invAxisTransformMatrix);
		addVertice(originVerticalMin, lstVertices, invAxisTransformMatrix);
		addVertice(originVerticalMax, lstVertices, invAxisTransformMatrix);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		lstColors.add(originColor);
		
		// Let's build vertical major rulers
		for (int h = hStart; h <= hEnd; h++) {
			double horizontal = dMajorIncrement * h;
			Point4d p1 = new Point4d( horizontal, vMin, 0 , 1);
			Point4d p2 = new Point4d( horizontal, vMax, 0 , 1);
			addVertice(p1, lstVertices, invAxisTransformMatrix);
			addVertice(p2, lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
			for(int s = 1; s < nbMinorPerMajor && h < hEnd; s++){
				Point4d s1 = new Point4d( horizontal + dMinorIncrement * s, vMin, 0 , 1);
				Point4d s2 = new Point4d( horizontal + dMinorIncrement * s, vMax, 0 , 1);
				addVertice(s1, lstVertices, invAxisTransformMatrix);
				addVertice(s2, lstVertices, invAxisTransformMatrix);
				lstColors.add(minorUnitColor);
				lstColors.add(minorUnitColor);	
			}
		}
		
		int vStart = verticalMinimal.divide(majorIncrement).setScale(0, RoundingMode.DOWN).intValue();
		int vEnd   = verticalMaximal.divide(majorIncrement).setScale(0, RoundingMode.DOWN).intValue();
		// Let's build horizontal major rulers
		for (int v = vStart; v <= vEnd; v++) {
			double vertical = dMajorIncrement * v;
			Point4d p1 = new Point4d( hMin, vertical, 0 , 1);
			Point4d p2 = new Point4d( hMax, vertical, 0 , 1);
			addVertice(p1, lstVertices, invAxisTransformMatrix);
			addVertice(p2, lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
			for(int s = 1; s < nbMinorPerMajor  && v < vEnd; s++){
				Point4d s1 = new Point4d( hMin, vertical + dMinorIncrement * s, 0 , 1);
				Point4d s2 = new Point4d( hMax, vertical + dMinorIncrement * s, 0 , 1);
				addVertice(s1, lstVertices, invAxisTransformMatrix);
				addVertice(s2, lstVertices, invAxisTransformMatrix);
				lstColors.add(minorUnitColor);
				lstColors.add(minorUnitColor);	
			}
		}
		
		if(horizontalColor != null){
			Point4d p1 = new Point4d( hMin, 0, 0 , 1);
			Point4d p2 = new Point4d( hMax, 0, 0 , 1);
			addVertice(p1, lstVertices, invAxisTransformMatrix);
			addVertice(p2, lstVertices, invAxisTransformMatrix);
			lstColors.add(horizontalColor);
			lstColors.add(horizontalColor);
		}
		
		if(verticalColor != null){
			Point4d p1 = new Point4d( 0, vMin, 0 , 1);
			Point4d p2 = new Point4d( 0, vMax, 0 , 1);
			addVertice(p1, lstVertices, invAxisTransformMatrix);
			addVertice(p2, lstVertices, invAxisTransformMatrix);
			lstColors.add(verticalColor);
			lstColors.add(verticalColor);
		}
		setVerticesCount(CollectionUtils.size(lstVertices));
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));	
	}
		
	private void addVertice(Point4d p, List<Point4d> buffer, Matrix4d invMatrix){
		if(invMatrix != null){
			invMatrix.transform(p);
		}
		buffer.add(p);
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		buildGrid();
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
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
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.event.IGCodeContextListener#onGCodeContextEvent(org.goko.core.gcode.element.IGCodeContext)
	 */
	@Override
	public void onGCodeContextEvent(GCodeContext context) {
		try {
			updateGeometry();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getMinorOpacity()
	 */
	@Override
	public float getMinorOpacity() {
		return minorOpacity;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMinorOpacity(float)
	 */
	@Override
	public void setMinorOpacity(float opacity) {
		this.minorOpacity = opacity;		
		this.minorUnitColor.w = opacity;		
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getMajorOpacity()
	 */
	@Override
	public float getMajorOpacity() {
		return majorOpacity;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMajorOpacity(float)
	 */
	@Override
	public void setMajorOpacity(float opacity) {
		this.majorOpacity = opacity;
		this.majorUnitColor.w = opacity;
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getAxisOpacity()
	 */
	@Override
	public float getAxisOpacity() {
		return axisOpacity;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setAxisOpacity(float)
	 */
	@Override
	public void setAxisOpacity(float opacity) {
		this.axisOpacity = opacity;
		this.originColor.w = opacity;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getMajorIncrement()
	 */
	@Override
	public Length getMajorIncrement() {
		return majorIncrement;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMajorIncrement(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setMajorIncrement(Length majorIncrement) {
		this.majorIncrement = majorIncrement;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getMinorIncrement()
	 */
	@Override
	public Length getMinorIncrement() {
		return minorIncrement;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMinorIncrement(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setMinorIncrement(Length minorIncrement) {
		this.minorIncrement = minorIncrement;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMajorUnitColor(javax.vecmath.Color3f)
	 */
	@Override
	public void setMajorUnitColor(Color3f majorUnitColor) {
		this.majorUnitColor.x = majorUnitColor.x;
		this.majorUnitColor.y = majorUnitColor.y;
		this.majorUnitColor.z = majorUnitColor.z;
	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setMinorUnitColor(javax.vecmath.Color3f)
	 */
	@Override
	public void setMinorUnitColor(Color3f minorUnitColor) {
		this.minorUnitColor.x = minorUnitColor.x;
		this.minorUnitColor.y = minorUnitColor.y;
		this.minorUnitColor.z = minorUnitColor.z;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getOriginColor()
	 */
	@Override
	public Color4f getOriginColor() {
		return originColor;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setOriginColor(javax.vecmath.Color4f)
	 */
	@Override
	public void setOriginColor(Color4f originColor) {
		this.originColor = originColor;
	}

	private void buildMatrix(){
		this.normal.normalize();
		this.horizontalVector.normalize();
		this.verticalVector.normalize();
		//this.verticalVector = new Vector3f();		
		//this.verticalVector.cross(normal, horizontalVector);
		this.axisTransformMatrix = new Matrix4f(new float[]{horizontalVector.x, horizontalVector.y, horizontalVector.z, 0.0f,																										
															verticalVector.x, verticalVector.y, verticalVector.z, 0.0f,
														    normal.x, normal.y, normal.z, 0.0f,
															0.0f, 0.0f, 0.0f, 1.0f});		
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getNormal()
	 */
	@Override
	public Vector3f getNormal() {
		return normal;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setNormal(javax.vecmath.Vector4f)
	 */
	@Override
	public void setNormal(Vector3f normal) {
		this.normal = normal;
		buildMatrix();
	}

	/**
	 * @return the majorUnitColor
	 */
	public Color4f getMajorUnitColor() {
		return majorUnitColor;
	}

	/**
	 * @param majorUnitColor the majorUnitColor to set
	 */
	public void setMajorUnitColor(Color4f majorUnitColor) {
		this.majorUnitColor = majorUnitColor;
	}

	/**
	 * @return the minorUnitColor
	 */
	public Color4f getMinorUnitColor() {
		return minorUnitColor;
	}

	/**
	 * @param minorUnitColor the minorUnitColor to set
	 */
	public void setMinorUnitColor(Color4f minorUnitColor) {
		this.minorUnitColor = minorUnitColor;
	}

	/**
	 * @return the axisTransformMatrix
	 */
	public Matrix4f getAxisTransformMatrix() {
		return axisTransformMatrix;
	}

	/**
	 * @param axisTransformMatrix the axisTransformMatrix to set
	 */
	public void setAxisTransformMatrix(Matrix4f axisTransformMatrix) {
		this.axisTransformMatrix = axisTransformMatrix;
	}

	/**
	 * @return the horizontalVector
	 */
	public Vector3f getHorizontalVector() {
		return horizontalVector;
	}

	/**
	 * @param horizontalVector the horizontalVector to set
	 */
	public void setHorizontalVector(Vector3f horizontalVector) {
		this.horizontalVector = horizontalVector;
		buildMatrix();
	}

	/**
	 * @return the verticalVector
	 */
	public Vector3f getVerticalVector() {
		return verticalVector;
	}

	/**
	 * @param verticalVector the verticalVector to set
	 */
	public void setVerticalVector(Vector3f verticalVector) {
		this.verticalVector = verticalVector;
		buildMatrix();
	}

	/**
	 * @return the horizontalMinimal
	 */
	public Length getHorizontalMinimal() {
		return horizontalMinimal;
	}

	/**
	 * @param horizontalMinimal the horizontalMinimal to set
	 */
	public void setHorizontalMinimal(Length horizontalMinimal) {
		this.horizontalMinimal = horizontalMinimal;
	}

	/**
	 * @return the horizontalMaximal
	 */
	public Length getHorizontalMaximal() {
		return horizontalMaximal;
	}

	/**
	 * @param horizontalMaximal the horizontalMaximal to set
	 */
	public void setHorizontalMaximal(Length horizontalMaximal) {
		this.horizontalMaximal = horizontalMaximal;
	}

	/**
	 * @return the verticalMinimal
	 */
	public Length getVerticalMinimal() {
		return verticalMinimal;
	}

	/**
	 * @param verticalMinimal the verticalMinimal to set
	 */
	public void setVerticalMinimal(Length verticalMinimal) {
		this.verticalMinimal = verticalMinimal;
	}

	/**
	 * @return the verticalMaximal
	 */
	public Length getVerticalMaximal() {
		return verticalMaximal;
	}

	/**
	 * @param verticalMaximal the verticalMaximal to set
	 */
	public void setVerticalMaximal(Length verticalMaximal) {
		this.verticalMaximal = verticalMaximal;
	}

	/**
	 * @return the horizontalColor
	 */
	public Color4f getHorizontalColor() {
		return horizontalColor;
	}

	/**
	 * @param horizontalColor the horizontalColor to set
	 */
	public void setHorizontalColor(Color4f horizontalColor) {
		this.horizontalColor = horizontalColor;
	}

	/**
	 * @return the verticalColor
	 */
	public Color4f getVerticalColor() {
		return verticalColor;
	}

	/**
	 * @param verticalColor the verticalColor to set
	 */
	public void setVerticalColor(Color4f verticalColor) {
		this.verticalColor = verticalColor;
	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setWorldBounds(org.goko.core.math.Tuple6b, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void setWorldBounds(Tuple6b workMin, Tuple6b workMax) {
		Vector3f workMinJogl = new Vector3f(workMin.min(workMax).toVector3d(JoglUtils.JOGL_UNIT));
		Vector3f workMaxJogl = new Vector3f(workMax.max(workMin).toVector3d(JoglUtils.JOGL_UNIT));
		Matrix4f invMatrix = new Matrix4f(getAxisTransformMatrix());
		invMatrix.invert();
		invMatrix.transform(workMinJogl);		
		invMatrix.transform(workMaxJogl);
		setHorizontalMinimal( Length.valueOf(BigDecimal.valueOf(workMinJogl.dot(getHorizontalVector())), JoglUtils.JOGL_UNIT));
		setHorizontalMaximal( Length.valueOf(BigDecimal.valueOf(workMaxJogl.dot(getHorizontalVector())), JoglUtils.JOGL_UNIT));
		setVerticalMinimal(   Length.valueOf(BigDecimal.valueOf(workMinJogl.dot(getVerticalVector())), JoglUtils.JOGL_UNIT));
		setVerticalMaximal(   Length.valueOf(BigDecimal.valueOf(workMaxJogl.dot(getVerticalVector())), JoglUtils.JOGL_UNIT));		
	}
}
