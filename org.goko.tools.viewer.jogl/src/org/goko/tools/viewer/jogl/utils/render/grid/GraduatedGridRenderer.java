/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.service.Layer;
import org.goko.tools.viewer.jogl.utils.render.text.v2.TextRenderer;

/**
 * Render a grid with distance annotation on major axis division
 * @author Psyko
 * @date 12 oct. 2016
 */
public class GraduatedGridRenderer extends AbstractCoreJoglMultipleRenderer implements IGridRenderer{
	private static final GkLog LOG = GkLog.getLogger(GraduatedGridRenderer.class);
	private GridRenderer gridRenderer;
	private List<TextRenderer> lstAnnotations;
	
	/**
	 * 
	 */
	public GraduatedGridRenderer(String id, IGCodeContextProvider<GCodeContext> gcodeContextProvider) {
		super();
		setLayerId(Layer.LAYER_GRIDS);
		setCode(id);
		gridRenderer = new GridRenderer(gcodeContextProvider);		
		lstAnnotations = new ArrayList<TextRenderer>();		
	//	addRenderer(gridRenderer);
		setUseAlpha(true);
	}

	protected void buildRenderers() throws GkException{	
		System.out.println("GraduatedGridRenderer.buildRenderers() on "+this+" [back grid "+gridRenderer+"]");
		destroyAnnotations();
		createAnnotations();
		if(CollectionUtils.isNotEmpty(lstAnnotations)){
			for (TextRenderer textRenderer : lstAnnotations) {
				addRenderer(textRenderer);
			}
		}
	}

	protected void destroyAnnotations() throws GkException{
		if(CollectionUtils.isNotEmpty(lstAnnotations)){
			for (TextRenderer textRenderer : lstAnnotations) {
				textRenderer.destroy();
				removeRenderer(textRenderer);
			}
		}
	}
	
	protected Vector3d getWidthVector(){
		Vector3d width = new Vector3d();
		// Add Y Zero green axis
		if(getNormal().dot(new Vector4f(0,0,1,0)) == 1 ){	// XY plane 				
			width = new Vector3d(1,0,0);

		}else if(getNormal().dot(new Vector4f(0,1,0,0)) == 1 ){ // XZ plane
			width = new Vector3d(1,0,0);
			
		}else{ // YZ Plane
			width = new Vector3d(0,-1,0);
		}
		
		return width;
	}
	
	protected Vector3d getHeightVector(){
		Vector3d height = new Vector3d();
		// Add Y Zero green axis
		if(getNormal().dot(new Vector4f(0,0,1,0)) == 1 ){	// XY plane 				
			height = new Vector3d(0,1,0);
		}else if(getNormal().dot(new Vector4f(0,1,0,0)) == 1 ){ // XZ plane
			height = new Vector3d(0,0,1);
		}else{ // YZ Plane
			height = new Vector3d(0,0,1);
		}
		return height;
	}
	
	protected void createAnnotations() throws GkException{
		double graduationSize = JoglViewerPreference.getInstance().getGraduationSize().doubleValue(JoglUtils.JOGL_UNIT);
		// Let's compute the width and height vector of the texts
		Vector3d width = getWidthVector();
		Vector3d height = getHeightVector();
		
		Tuple6b lclStart6b 			= new Tuple6b();
		lclStart6b.min(getStart(), getEnd());		
		Tuple6b lclEnd6b 			= new Tuple6b();
		lclEnd6b.max(getStart(), getEnd());
		Tuple6b lclCenter6b = new Tuple6b(0,0,0, JoglUtils.JOGL_UNIT);
		
		Length padding = Length.valueOf("0.1", JoglUtils.JOGL_UNIT);
		// Determine min/max if zero is not in the desired area
		lclCenter6b = lclCenter6b.max(lclStart6b).min(lclEnd6b);
						
		Point3f lclStart = lclStart6b.toPoint3f(JoglUtils.JOGL_UNIT);
		getAxisTransformMatrix().transform(lclStart);
		Point3f lclEnd   = lclEnd6b.toPoint3f(JoglUtils.JOGL_UNIT);
		getAxisTransformMatrix().transform(lclEnd);
		Point3f lclCenter   = lclCenter6b.toPoint3f(JoglUtils.JOGL_UNIT);
		getAxisTransformMatrix().transform(lclCenter);
		Matrix4d invAxisTransformMatrix = new Matrix4d(getAxisTransformMatrix());
		invAxisTransformMatrix.invert();

		Tuple6b deltaPlus  =  lclEnd6b.subtract(lclCenter6b).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		Tuple6b deltaMinus =  lclCenter6b.subtract(lclStart6b).max(new Tuple6b(0,0,0,JoglUtils.JOGL_UNIT));
		
		// Major divisions
		int nbStepXPlusMajor = Math.abs(deltaPlus.getX().divide(getMajorIncrement()).intValue());
		int nbStepYPlusMajor = Math.abs(deltaPlus.getY().divide(getMajorIncrement()).intValue());
		int nbStepZPlusMajor = Math.abs(deltaPlus.getZ().divide(getMajorIncrement()).intValue());
		
		int nbStepXMinusMajor = Math.abs(deltaMinus.getX().divide(getMajorIncrement()).intValue());
		int nbStepYMinusMajor = Math.abs(deltaMinus.getY().divide(getMajorIncrement()).intValue());
		int nbStepZMinusMajor = Math.abs(deltaMinus.getZ().divide(getMajorIncrement()).intValue());
				
		Vector3f nbStepPlusMajor = new Vector3f(nbStepXPlusMajor, nbStepYPlusMajor, nbStepZPlusMajor);
		Vector3f nbStepMinusMajor = new Vector3f(nbStepXMinusMajor, nbStepYMinusMajor, nbStepZMinusMajor);
		getAxisTransformMatrix().transform(nbStepPlusMajor);
		getAxisTransformMatrix().transform(nbStepMinusMajor);
				
		double majorIncrementJoglUnit = getMajorIncrement().doubleValue(JoglUtils.JOGL_UNIT);		
		for (int i = 1; i <= nbStepPlusMajor.x; i++) {
			Length graduationValue = Length.valueOf(BigDecimal.valueOf(lclCenter.x+i*majorIncrementJoglUnit), JoglUtils.JOGL_UNIT);
			Point3d position = new Point3d(lclCenter.x+i*majorIncrementJoglUnit/**width.x*/, lclCenter.y+i*majorIncrementJoglUnit/**width.y*/ , lclCenter.z+i*majorIncrementJoglUnit*width.z);
			TextRenderer graduation = new TextRenderer("x"+GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, width, height, TextRenderer.TOP | TextRenderer.LEFT);		
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);
			lstAnnotations.add(graduation);			
			graduation.setColor(getMajorUnitColor().x, getMajorUnitColor().y, getMajorUnitColor().z, getMajorUnitColor().w);
		}
 		
 		for (int i = 1; i <= nbStepMinusMajor.x; i++) {
 			Length graduationValue = Length.valueOf(BigDecimal.valueOf(lclCenter.x-i*majorIncrementJoglUnit), JoglUtils.JOGL_UNIT);
 			Point3d position = new Point3d(lclCenter.x-i*majorIncrementJoglUnit, lclCenter.y-i*majorIncrementJoglUnit , lclCenter.z-i*majorIncrementJoglUnit*width.z);
 			TextRenderer graduation = new TextRenderer(GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, width, height,TextRenderer.TOP | TextRenderer.RIGHT);
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);
			lstAnnotations.add(graduation);
			graduation.setColor(getMajorUnitColor().x, getMajorUnitColor().y, getMajorUnitColor().z, getMajorUnitColor().w);			
 		}
		
		for (int i = 1; i <= nbStepPlusMajor.y; i++) {
			Length graduationValue = Length.valueOf(BigDecimal.valueOf(lclCenter.y+i*majorIncrementJoglUnit), JoglUtils.JOGL_UNIT);
			Point3d position = new Point3d(lclCenter.x+i*majorIncrementJoglUnit*height.x, lclCenter.y+i*majorIncrementJoglUnit*height.y , lclCenter.z+i*majorIncrementJoglUnit*height.z);
			TextRenderer graduation = new TextRenderer(GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, width, height,TextRenderer.BOTTOM | TextRenderer.RIGHT);
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);
			lstAnnotations.add(graduation);			
			graduation.setColor(getMajorUnitColor().x, getMajorUnitColor().y, getMajorUnitColor().z, getMajorUnitColor().w);
		}
		
		for (int i = 1; i <= nbStepMinusMajor.y; i++) {
			Length graduationValue = Length.valueOf(BigDecimal.valueOf(lclCenter.y-i*majorIncrementJoglUnit), JoglUtils.JOGL_UNIT);
			Point3d position = new Point3d(lclCenter.x-i*majorIncrementJoglUnit*height.x, lclCenter.y-i*majorIncrementJoglUnit*height.y , lclCenter.z-i*majorIncrementJoglUnit*height.z);
			TextRenderer graduation = new TextRenderer(GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, width, height,TextRenderer.BOTTOM | TextRenderer.RIGHT);
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);
			lstAnnotations.add(graduation);	
			graduation.setColor(getMajorUnitColor().x, getMajorUnitColor().y, getMajorUnitColor().z, getMajorUnitColor().w);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer#update()
	 */
	@Override
	public void update() {
		gridRenderer.update();
		super.update();
		try {
			buildRenderers();
		} catch (GkException e) {
			LOG.error(e);
		}		
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer#initialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void initialize(GL3 gl) throws GkException {		
		
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {		
		buildRenderers();
	}
	/**
	 * @param bounds
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#setBounds(org.goko.core.math.BoundingTuple6b)
	 */
	public void setBounds(BoundingTuple6b bounds) {
		gridRenderer.setBounds(bounds);
	}
	/**
	 * @param opacity
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMinorOpacity(float)
	 */
	public void setMinorOpacity(float opacity) {
		gridRenderer.setMinorOpacity(opacity);
	}
	/**
	 * @param opacity
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMajorOpacity(float)
	 */
	public void setMajorOpacity(float opacity) {
		gridRenderer.setMajorOpacity(opacity);
	}
	/**
	 * @param opacity
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setAxisOpacity(float)
	 */
	public void setAxisOpacity(float opacity) {
		gridRenderer.setAxisOpacity(opacity);
	}
	/**
	 * @param majorIncrement
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMajorIncrement(org.goko.core.common.measure.quantity.Length)
	 */
	public void setMajorIncrement(Length majorIncrement) {
		gridRenderer.setMajorIncrement(majorIncrement);
	}
	/**
	 * @param minorIncrement
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMinorIncrement(org.goko.core.common.measure.quantity.Length)
	 */
	public void setMinorIncrement(Length minorIncrement) {
		gridRenderer.setMinorIncrement(minorIncrement);
	}
	/**
	 * @param majorUnitColor
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMajorUnitColor(javax.vecmath.Color3f)
	 */
	public void setMajorUnitColor(Color3f majorUnitColor) {
		gridRenderer.setMajorUnitColor(majorUnitColor);
	}
	/**
	 * @param minorUnitColor
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setMinorUnitColor(javax.vecmath.Color3f)
	 */
	public void setMinorUnitColor(Color3f minorUnitColor) {
		gridRenderer.setMinorUnitColor(minorUnitColor);
	}
	/**
	 * @param originColor
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setOriginColor(javax.vecmath.Color4f)
	 */
	public void setOriginColor(Color4f originColor) {
		gridRenderer.setOriginColor(originColor);
	}
	/**
	 * @param end
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setEnd(org.goko.core.math.Tuple6b)
	 */
	public void setEnd(Tuple6b end) {
		gridRenderer.setEnd(end);
	}
	/**
	 * @param normal
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setNormal(javax.vecmath.Vector4f)
	 */
	public void setNormal(Vector4f normal) {
		gridRenderer.setNormal(normal);
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#getBounds()
	 */
	public BoundingTuple6b getBounds() {
		return gridRenderer.getBounds();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMinorOpacity()
	 */
	public float getMinorOpacity() {
		return gridRenderer.getMinorOpacity();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMajorOpacity()
	 */
	public float getMajorOpacity() {
		return gridRenderer.getMajorOpacity();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getAxisOpacity()
	 */
	public float getAxisOpacity() {
		return gridRenderer.getAxisOpacity();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMajorIncrement()
	 */
	public Length getMajorIncrement() {
		return gridRenderer.getMajorIncrement();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMinorIncrement()
	 */
	public Length getMinorIncrement() {
		return gridRenderer.getMinorIncrement();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getOriginColor()
	 */
	public Color4f getOriginColor() {
		return gridRenderer.getOriginColor();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getStart()
	 */
	public Tuple6b getStart() {
		return gridRenderer.getStart();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getEnd()
	 */
	public Tuple6b getEnd() {
		return gridRenderer.getEnd();
	}
	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getNormal()
	 */
	public Vector4f getNormal() {
		return gridRenderer.getNormal();
	}
	/**
	 * @param start
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setStart(org.goko.core.math.Tuple6b)
	 */
	public void setStart(Tuple6b start) {
		gridRenderer.setStart(start);
	}

	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getAxisTransformMatrix()
	 */
	protected Matrix4f getAxisTransformMatrix() {
		return gridRenderer.getAxisTransformMatrix();
	}

	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMajorUnitColor()
	 */
	public Color4f getMajorUnitColor() {
		return gridRenderer.getMajorUnitColor();
	}

	/**
	 * @return
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getMinorUnitColor()
	 */
	public Color4f getMinorUnitColor() {
		return gridRenderer.getMinorUnitColor();
	}

}
