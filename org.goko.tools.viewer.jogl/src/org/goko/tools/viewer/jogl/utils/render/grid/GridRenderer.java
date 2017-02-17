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
	private Color4f majorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f minorUnitColor	= new Color4f(0.4f, 0.4f, 0.4f,1f);
	private Color4f originColor		= new Color4f(0.8f, 0.8f, 0.8f,1f);
	private Tuple6b start;
	private Tuple6b end;
	private float majorOpacity;
	private float minorOpacity;
	private float axisOpacity;
	private Matrix4f axisTransformMatrix; 
	private Vector4f normal; 
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	 
	public GridRenderer(IGCodeContextProvider<GCodeContext> gcodeContextProvider){		
		this(new Tuple6b(-100, -100, 0, JoglUtils.JOGL_UNIT), 
			 new Tuple6b(100, 100, 0, JoglUtils.JOGL_UNIT),
			 Length.valueOf(10, JoglUtils.JOGL_UNIT),
			 Length.valueOf(1, JoglUtils.JOGL_UNIT),
			 new Color3f(0.4f, 0.4f, 0.4f),
			 new Color3f(0.4f, 0.4f, 0.4f),
			 0.5f, 0.5f, 0.5f, new Vector4f(0f,0f,1f,0f));
		this.gcodeContextProvider = gcodeContextProvider;
		if(this.gcodeContextProvider != null){
			gcodeContextProvider.addObserver(this);
		}
	}
				
	/**
	 * Constructor
	 */
	public GridRenderer(Tuple6b start, Tuple6b end, Length majorIncrement, Length minorIncrement, Color3f majorColor, Color3f minorColor, float minorOpacity, float majorOpacity, float axisOpacity, Vector4f normal) {
		super(GL.GL_LINES,  COLORS | VERTICES);
		this.setLayerId(Layer.LAYER_GRIDS);		
		this.start 			= new Tuple6b();
		this.start.min(start, end);
		this.end 			= new Tuple6b();
		this.end.max(start, end);
		this.majorIncrement = majorIncrement.to(JoglUtils.JOGL_UNIT);
		this.minorIncrement = minorIncrement.to(JoglUtils.JOGL_UNIT);
		this.majorUnitColor = new Color4f(majorColor.x,majorColor.y,majorColor.z,majorOpacity);
		this.minorUnitColor = new Color4f(minorColor.x,minorColor.y,minorColor.z,minorOpacity);
		this.originColor.w = axisOpacity;
		this.minorOpacity = minorOpacity;
		this.majorOpacity = majorOpacity;
		this.axisOpacity = axisOpacity;
		this.normal = new Vector4f(normal);
		buildMatrix();
		setUseAlpha(true);
	}

	private void buildGrid() throws GkException{
		System.out.println("GridRenderer.buildGrid() on "+this);
		buildMatrix();		
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		List<Color4f> lstColors = new ArrayList<Color4f>();
		
		Tuple6b lclStart6b 			= new Tuple6b();
		lclStart6b.min(start, end);		
		Tuple6b lclEnd6b 			= new Tuple6b();
		lclEnd6b.max(start, end);
		Point3f tupleStart = lclStart6b.toPoint3f(JoglUtils.JOGL_UNIT);
		Point3f tupleEnd   = lclEnd6b.toPoint3f(JoglUtils.JOGL_UNIT);
		Tuple6b lclCenter6b = new Tuple6b(0,0,0, JoglUtils.JOGL_UNIT);
		if(gcodeContextProvider != null){
			GCodeContext context = gcodeContextProvider.getGCodeContext();
			lclCenter6b = context.getCoordinateSystemData( context.getCoordinateSystem() );
		}
		// Determine min/max if zero is not in the desired area
		lclCenter6b = lclCenter6b.max(lclStart6b).min(lclEnd6b);
				
		
		Point3f lclStart = lclStart6b.toPoint3f(JoglUtils.JOGL_UNIT);
		axisTransformMatrix.transform(lclStart);
		Point3f lclEnd   = lclEnd6b.toPoint3f(JoglUtils.JOGL_UNIT);
		axisTransformMatrix.transform(lclEnd);
		Point3f lclCenter   = lclCenter6b.toPoint3f(JoglUtils.JOGL_UNIT);
		axisTransformMatrix.transform(lclCenter);
		Matrix4d invAxisTransformMatrix = new Matrix4d(axisTransformMatrix);
		invAxisTransformMatrix.invert();

		
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
		
		double majorIncrementJoglUnit = majorIncrement.doubleValue(JoglUtils.JOGL_UNIT);
		double minorIncrementJoglUnit = minorIncrement.doubleValue(JoglUtils.JOGL_UNIT);
		for (int i = 1; i <= nbStepPlusMajor.x; i++) {
			addVertice(new Point4d(lclCenter.x+i*majorIncrementJoglUnit, lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);			
			addVertice(new Point4d(lclCenter.x+i*majorIncrementJoglUnit, lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);		
			
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepMinusMajor.x; i++) {
			addVertice(new Point4d(lclCenter.x-i*majorIncrementJoglUnit, lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x-i*majorIncrementJoglUnit, lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepPlusMajor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y+i*majorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y+i*majorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(majorUnitColor);
			lstColors.add(majorUnitColor);
		}
		
		for (int i = 1; i <= nbStepMinusMajor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y-i*majorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y-i*majorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
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
			addVertice(new Point4d(lclCenter.x+i*minorIncrementJoglUnit, lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x+i*minorIncrementJoglUnit, lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepMinusMinor.x; i++) {
			addVertice(new Point4d(lclCenter.x-i*minorIncrementJoglUnit, lclStart.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclCenter.x-i*minorIncrementJoglUnit, lclEnd.y , lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);			
		}
		
		for (int i = 1; i <= nbStepPlusMinor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y+i*minorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y+i*minorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		for (int i = 1; i <= nbStepMinusMinor.y; i++) {
			addVertice(new Point4d(lclStart.x, lclCenter.y-i*minorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			addVertice(new Point4d(lclEnd.x ,  lclCenter.y-i*minorIncrementJoglUnit, lclCenter.z,1), lstVertices, invAxisTransformMatrix);
			lstColors.add(minorUnitColor);
			lstColors.add(minorUnitColor);
		}
		
		// Add X Zero red axis
		if(normal.dot(new Vector4f(1,0,0,0)) == 0 ){
			addVertice(new Point4d(tupleStart.x, 0, 0,1), lstVertices, null);
			addVertice(new Point4d(tupleEnd.x, 0, 0,1), lstVertices, null);		
			lstColors.add(new Color4f(1,0,0,axisOpacity));
			lstColors.add(new Color4f(1,0,0,axisOpacity)); //ca ne fonctionne pas car les axes sont tournés
		}
		
		// Add Y Zero green axis
		if(normal.dot(new Vector4f(0,1,0,0)) == 0 ){
			addVertice(new Point4d(0, tupleStart.y , 0,1), lstVertices, null);
			addVertice(new Point4d(0, tupleEnd.y,  0,1), lstVertices, null);		
			lstColors.add(new Color4f(0,1,0,axisOpacity));
			lstColors.add(new Color4f(0,1,0,axisOpacity));
		}

		// Add Z Zero Blue axis
		if(normal.dot(new Vector4f(0,0,1,0)) == 0 ){
			addVertice(new Point4d(0, 0, tupleStart.z ,1), lstVertices, null);
			addVertice(new Point4d(0, 0, tupleEnd.z,  1), lstVertices, null);		
			lstColors.add(new Color4f(0,0,1,axisOpacity));
			lstColors.add(new Color4f(0,0,1,axisOpacity));
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

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getStart()
	 */
	@Override
	public Tuple6b getStart() {
		return start;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setStart(org.goko.core.math.Tuple6b)
	 */
	@Override
	public void setStart(Tuple6b start) {
		this.start = start;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getEnd()
	 */
	@Override
	public Tuple6b getEnd() {
		return end;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setEnd(org.goko.core.math.Tuple6b)
	 */
	@Override
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
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getNormal()
	 */
	@Override
	public Vector4f getNormal() {
		return normal;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setNormal(javax.vecmath.Vector4f)
	 */
	@Override
	public void setNormal(Vector4f normal) {
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
	
}
