package org.goko.viewer.jogl.utils.render.coordinate;

import javax.media.opengl.GL3;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.viewer.jogl.preferences.JoglViewerPreference.EnumRotaryAxisDirection;
import org.goko.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.viewer.jogl.utils.render.coordinate.measurement.ArrowRenderer;
import org.goko.viewer.jogl.utils.render.text.TextRenderer;

public class FourAxisOriginRenderer extends AbstractCoreJoglMultipleRenderer {

	private FourAxisRenderer axisRenderer;

	public FourAxisOriginRenderer(boolean displayRotaryAxis) {
		super();
		TextRenderer xTextRenderer = new TextRenderer("X",2, new Point3d(10,-0.1,0), TextRenderer.MIDDLE | TextRenderer.LEFT);
		xTextRenderer.setColor(1,0,0,1);
		addRenderer(new ArrowRenderer(new Point3d(10,0,0.01), new Vector3d(1,0,0), new Vector3d(0,1,0), new Color4f(1,0,0,1)));
		TextRenderer yTextRenderer = new TextRenderer("Y",2, new Point3d(-0.5,10,0), TextRenderer.BOTTOM | TextRenderer.LEFT);
		yTextRenderer.setColor(0,1,0,1);
		addRenderer(new ArrowRenderer(new Point3d(0,10,0.01), new Vector3d(0,1,0), new Vector3d(1,0,0), new Color4f(0,1,0,1)));
		TextRenderer zTextRenderer = new TextRenderer("Z",2, new Point3d(-0.5,0,10), new Vector3d(1,0,0), new Vector3d(0,0,1), TextRenderer.BOTTOM | TextRenderer.LEFT);
		addRenderer(new ArrowRenderer(new Point3d(0,0,10), new Vector3d(0,0,1), new Vector3d(1,0,0), new Color4f(0,0,1,1)));
		zTextRenderer.setColor(0,0,1,1);
		addRenderer(xTextRenderer);
		addRenderer(yTextRenderer);
		addRenderer(zTextRenderer);
		axisRenderer = new FourAxisRenderer(10, JoglViewerPreference.getInstance().getRotaryAxisDirection(), new Color3f(1,0,0), new Color3f(0,1,0), new Color3f(0,0,1), new Color3f(1,1,0));
		axisRenderer.setDisplayRotaryAxis(displayRotaryAxis);
		addRenderer(axisRenderer);	
	}
	
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the rotationAxis
	 */
	protected EnumRotaryAxisDirection getRotationAxis() {
		return axisRenderer.getRotationAxis();
	}

	/**
	 * @param rotationAxis the rotationAxis to set
	 * @throws GkException GkException
	 */
	public void setRotationAxis(EnumRotaryAxisDirection rotationAxis) throws GkException {
		axisRenderer.setRotationAxis(rotationAxis);
	}

	/**
	 * @return the displayRotaryAxis
	 */
	public boolean isDisplayRotaryAxis() {
		return axisRenderer.isDisplayRotaryAxis();
	}

	/**
	 * @param displayRotaryAxis the displayRotaryAxis to set
	 */
	public void setDisplayRotaryAxis(boolean displayRotaryAxis) {
		axisRenderer.setDisplayRotaryAxis(displayRotaryAxis);
	}
}
