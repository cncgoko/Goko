/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.grid;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
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

import com.jogamp.opengl.GL3;

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
		addRenderer(gridRenderer);
		setUseAlpha(true);
	}

	public GraduatedGridRenderer(String id, IGCodeContextProvider<GCodeContext> gcodeContextProvider, Vector4f normal, Vector4f horizontalVector) {
		super();
		setLayerId(Layer.LAYER_GRIDS);
		setCode(id);
		gridRenderer = new GridRenderer(gcodeContextProvider);		
		lstAnnotations = new ArrayList<TextRenderer>();		
		addRenderer(gridRenderer);
		setUseAlpha(true);
	}
	
	protected void buildRenderers() throws GkException{	
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

	protected void createAnnotations() throws GkException{
		Matrix4d invAxisTransformMatrix = new Matrix4d(getAxisTransformMatrix());
		invAxisTransformMatrix.invert(); 
		
		double graduationSize = JoglViewerPreference.getInstance().getGraduationSize().doubleValue(JoglUtils.JOGL_UNIT);
		Length padding = Length.valueOf("0.1", JoglUtils.JOGL_UNIT);
		
		// Let's compute the width and height vector of the texts
		Vector3d txtDirection = new Vector3d(1,0,0);
		invAxisTransformMatrix.transform(txtDirection);
		Vector3d txtUp = new Vector3d(0,1,0);
		invAxisTransformMatrix.transform(txtUp);
		
		int hStart = getHorizontalMinimal().divide(getMajorIncrement()).setScale(0, RoundingMode.DOWN).intValue();
		int hEnd   = getHorizontalMaximal().divide(getMajorIncrement()).setScale(0, RoundingMode.DOWN).intValue();

		double dMajorIncrement = getMajorIncrement().doubleValue(JoglUtils.JOGL_UNIT);
	
		// Let's build vertical major rulers
		for (int h = hStart; h <= hEnd; h++) {
			double horizontal = dMajorIncrement * h;
			Length graduationValue = getMajorIncrement().multiply(h);
			Point3d position = new Point3d(horizontal, 0, 0);
			invAxisTransformMatrix.transform(position);
			TextRenderer graduation = new TextRenderer(GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, txtDirection, txtUp, TextRenderer.TOP | TextRenderer.LEFT);		
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);			
			lstAnnotations.add(graduation);			
			graduation.setColor(getHorizontalColor().x, getHorizontalColor().y, getHorizontalColor().z, getMajorOpacity());
		}
		
		int vStart = getVerticalMinimal().divide(getMajorIncrement()).setScale(0, RoundingMode.DOWN).intValue();
		int vEnd   = getVerticalMaximal().divide(getMajorIncrement()).setScale(0, RoundingMode.DOWN).intValue();
		// Let's build horizontal major rulers
		for (int v = vStart; v <= vEnd; v++) {
			double vertical = dMajorIncrement * v;
			Length graduationValue = getMajorIncrement().multiply(v);
			Point3d position = new Point3d(0, vertical, 0);
			invAxisTransformMatrix.transform(position);
			TextRenderer graduation = new TextRenderer(GokoPreference.getInstance().format(graduationValue, false, true), graduationSize, position, txtDirection, txtUp, TextRenderer.TOP | TextRenderer.LEFT);		
			graduation.setHorizontalPadding(padding);
			graduation.setVerticalPadding(padding);
			lstAnnotations.add(graduation);			
			graduation.setColor(getVerticalColor().x, getVerticalColor().y, getVerticalColor().z, getMajorOpacity());
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer#update()
	 */
	@Override
	public void update() {
		super.update();
		try {
			buildRenderers();
		} catch (GkException e) {
			LOG.error(e);
		}		
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
	 * @param normal
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#setNormal(javax.vecmath.Vector4f)
	 */
	public void setNormal(Vector3f normal) {
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
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.GridRenderer#getNormal()
	 */
	public Vector3f getNormal() {
		return gridRenderer.getNormal();
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

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getHorizontalVector()
	 */
	@Override
	public Vector3f getHorizontalVector() {
		return gridRenderer.getHorizontalVector();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setHorizontalVector(javax.vecmath.Vector3f)
	 */
	@Override
	public void setHorizontalVector(Vector3f horizontalVector) {
		gridRenderer.setHorizontalVector(horizontalVector);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getVerticalVector()
	 */
	@Override
	public Vector3f getVerticalVector() {
		return gridRenderer.getVerticalVector();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setVerticalVector(javax.vecmath.Vector3f)
	 */
	@Override
	public void setVerticalVector(Vector3f verticalVector) {
		gridRenderer.setVerticalVector(verticalVector);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getHorizontalMinimal()
	 */
	@Override
	public Length getHorizontalMinimal() {
		return gridRenderer.getHorizontalMinimal();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setHorizontalMinimal(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setHorizontalMinimal(Length horizontalMinimal) {
		gridRenderer.setHorizontalMinimal(horizontalMinimal);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getHorizontalMaximal()
	 */
	@Override
	public Length getHorizontalMaximal() {
		return gridRenderer.getHorizontalMaximal();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setHorizontalMaximal(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setHorizontalMaximal(Length horizontalMaximal) {
		gridRenderer.setHorizontalMaximal(horizontalMaximal);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getVerticalMinimal()
	 */
	@Override
	public Length getVerticalMinimal() {
		return gridRenderer.getVerticalMinimal();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setVerticalMinimal(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setVerticalMinimal(Length verticalMinimal) {
		gridRenderer.setVerticalMinimal(verticalMinimal);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getVerticalMaximal()
	 */
	@Override
	public Length getVerticalMaximal() {
		return gridRenderer.getVerticalMaximal();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setVerticalMaximal(org.goko.core.common.measure.quantity.Length)
	 */
	@Override
	public void setVerticalMaximal(Length verticalMaximal) {
		gridRenderer.setVerticalMaximal(verticalMaximal);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getHorizontalColor()
	 */
	@Override
	public Color4f getHorizontalColor() {
		return gridRenderer.getHorizontalColor();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setHorizontalColor(javax.vecmath.Color4f)
	 */
	@Override
	public void setHorizontalColor(Color4f horizontalColor) {
		gridRenderer.setHorizontalColor(horizontalColor);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#getVerticalColor()
	 */
	@Override
	public Color4f getVerticalColor() {
		return gridRenderer.getVerticalColor();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setVerticalColor(javax.vecmath.Color4f)
	 */
	@Override
	public void setVerticalColor(Color4f verticalColor) {
		gridRenderer.setVerticalColor(verticalColor);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer#setWorldBounds(org.goko.core.math.Tuple6b, org.goko.core.math.Tuple6b)
	 */
	@Override
	public void setWorldBounds(Tuple6b min, Tuple6b max) {
		gridRenderer.setWorldBounds(min, max);
	}
}
