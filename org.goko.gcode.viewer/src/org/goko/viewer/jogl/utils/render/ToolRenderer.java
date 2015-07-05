package org.goko.viewer.jogl.utils.render;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.service.JoglUtils;
import org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer;

import com.jogamp.opengl.util.PMVMatrix;

public class ToolRenderer extends DeprecatedAbstractVboJoglRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.ToolRenderer";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ToolRenderer.class);
	/** the controller service used to retrieve the tool position */
	private IThreeAxisControllerAdapter controllerAdapter;
	/** */
	private int segmentCount = 16;
	/**
	 * Constructor
	 * @param controllerService the controller service used to retrieve the tool position
	 */
	public ToolRenderer(IThreeAxisControllerAdapter controllerService) {
		this.controllerAdapter = controllerService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}


	private Point3d updateRenderMatrix(PMVMatrix modelMatrix){
		Point3d p = new Point3d();
		try{
			if(getControllerAdapter() != null){
				modelMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
				modelMatrix.glLoadIdentity();
				Unit<Length> targetLengthUnit = GokoPreference.getInstance().getLengthUnit();
				p.x = getControllerAdapter().getX().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT); 
				p.y = getControllerAdapter().getY().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT);
				p.z = getControllerAdapter().getZ().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT);				
				modelMatrix.glTranslatef((float)p.x, (float)p.y, (float)p.z);
				modelMatrix.update();
			}
		}catch(GkException e){
			LOG.error(e);
		}
		return p;
	}

	@Override
	protected int getVerticesCount() throws GkException {
		return segmentCount+3;
	}

	@Override
	protected FloatBuffer generateVertices() throws GkException {
		FloatBuffer vertices = FloatBuffer.allocate(getVerticesCount()*4);
		int radius = 3 / 2;
		vertices.put( new float[]{-radius,0, 6,1});
		vertices.put( new float[]{0,0, 0,1});
		float angle = (float) ((Math.PI * 2) / (segmentCount-1));
		for (int i = 0; i <= segmentCount; i++) {
			float x = (float) (radius * Math.cos(i * angle));
			float y = (float) (radius * Math.sin(i * angle));
			vertices.put( new float[]{x, y, 6,1});
		}
		return vertices;
	}

	@Override
	public PMVMatrix getModelMatrix() {
		PMVMatrix modelMatrix = super.getModelMatrix();
		updateRenderMatrix(modelMatrix);
		return modelMatrix;
	}

	@Override
	protected int getRenderType() {
		return GL.GL_LINE_STRIP;
	}

//	/** (inheritDoc)
//	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
//	 */
//	@Override
//	public void render(JoglRendererProxy proxy) throws GkException {
//		GL2 gl = proxy.getGl2();
//		Point3d toolPosition = getToolPosition();
//		if (toolPosition == null) {
//			toolPosition = new Point3d(0,0,0);
//		}
//
//		// Draw the path to the tool
//		gl.glEnable(GL2.GL_LINE_STIPPLE);
//		gl.glLineStipple(1, (short) 0x00FF);
//		gl.glLineWidth(2f);
//		gl.glBegin(GL2.GL_LINES);
//		gl.glColor3f(1, 0, 0);
//		gl.glVertex3d(0, 0, 0);
//		gl.glVertex3d(toolPosition.x, 0, 0);
//		gl.glColor3f(0, 1, 0);
//		gl.glVertex3d(toolPosition.x, 0, 0);
//		gl.glVertex3d(toolPosition.x, toolPosition.y, 0);
//		gl.glColor3f(0, 0, 1);
//		gl.glVertex3d(toolPosition.x, toolPosition.y, 0);
//		gl.glVertex3d(toolPosition.x, toolPosition.y, toolPosition.z);
//		gl.glEnd();
//		gl.glDisable(GL2.GL_LINE_STIPPLE);
//		// Draw the tool
//		gl.glPushMatrix();
//		gl.glTranslated(toolPosition.x, toolPosition.y, toolPosition.z);
//
//		gl.glLineWidth(3.0f);
//		gl.glBegin(GL2.GL_LINE_STRIP);
//		gl.glColor4f(0.87f, 0.31f, 0.43f, 1f);
//		int segmentCount = 16;
//		int radius = 3 / 2;
//		float angle = (float) ((Math.PI * 2) / segmentCount);
//		for (int i = 0; i <= segmentCount; i++) {
//			double x = radius * Math.cos(i * angle);
//			double y = radius * Math.sin(i * angle);
//			gl.glVertex3d(x, y, 6);
//		}
//		gl.glEnd();
//		gl.glBegin(GL2.GL_LINES);
//		gl.glVertex3d(radius, 0, 6);
//		gl.glVertex3d(0, 0, 0);
//		gl.glVertex3d(0, 0, 0);
//		gl.glVertex3d(-radius, 0, 6);
//		gl.glVertex3d(0, radius, 6);
//		gl.glVertex3d(0, 0, 0);
//		gl.glVertex3d(0, 0, 0);
//		gl.glVertex3d(0, -radius, 6);
//		gl.glEnd();
//		gl.glPopMatrix();
//
//	}

	/**
	 * @return the controllerService
	 */
	public IThreeAxisControllerAdapter getControllerAdapter() {
		return controllerAdapter;
	}

	@Override
	protected FloatBuffer generateColors() throws GkException {
		FloatBuffer colors = FloatBuffer.allocate(18*4);
		for(int i = 0; i < 18; i++){
			colors.put(new float[]{0.87f, 0.31f, 0.43f, 1f});
		}
		return colors;
	}
}
