/**
 * 
 */
package org.goko.tools.viewer.jogl.camera;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point2i;
import javax.vecmath.Point4f;
import javax.vecmath.Vector4f;

import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.service.JoglSceneManager;
import org.goko.tools.viewer.jogl.service.overlay.AbstractPositionOverlay;

import com.jogamp.opengl.util.PMVMatrix;

/**
 * @author Psyko
 * @date 22 juil. 2017
 */
public class PerspectivePositionOverlay extends AbstractPositionOverlay{
	private JoglSceneManager manager;
	private Vector4f planeOrigin;
	private AbstractCamera targetCamera;
	
	/**
	 * @param manager
	 */
	public PerspectivePositionOverlay(AbstractCamera camera, JoglSceneManager manager) {
		super();
		this.targetCamera = camera;
		this.manager = manager;
		this.planeOrigin = new Vector4f(0f,0f,0f,1f);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.overlay.AbstractPositionOverlay#drawPositionData(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawPositionData(Graphics2D g2d, Rectangle bounds) throws GkException {
		Point2i point = manager.getMouseCanvasPosition();
		if(point != null){
			PMVMatrix pmvMatrix = manager.getActiveCamera().getPmvMatrix();
			if(targetCamera.getWorkingPlaneNormal() == null){
				return;
			}
			float[] projMatArr = new float[16];
			pmvMatrix.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
			pmvMatrix.glGetFloatv(GLMatrixFunc.GL_PROJECTION_MATRIX, projMatArr, 0);
			
			float[] modvMatArr = new float[16];
			pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
			pmvMatrix.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, modvMatArr, 0);
			
			Matrix4f invProjMat = new Matrix4f(projMatArr);			
			Matrix4f invModvMat = new Matrix4f(modvMatArr);
			Point4f camera = new Point4f(0f,0f,0f,1f);

			invProjMat.transpose();
			invModvMat.transpose();
			invProjMat.invert();
			invModvMat.invert();

			invModvMat.transform(camera);
			
			float x = (2.0f * point.x) / bounds.width - 1.0f;
			float y = 1.0f - (2.0f * point.y) / bounds.height;
			
			Vector4f ray = new Vector4f(x, y, -1.0f, 1f);
			
			invProjMat.transform(ray);
			ray.z = -1;
			ray.w = 0;
			invModvMat.transform(ray);
			ray.normalize();
			
			Vector4f localPlaneNormal = new Vector4f(targetCamera.getWorkingPlaneNormal()); 
			float denom = ray.dot(localPlaneNormal);
			if(Math.abs(denom) > 1e-6){
				Vector4f p0l0 = new Vector4f(planeOrigin);
				p0l0.sub(camera);
				float t = p0l0.dot(localPlaneNormal) / denom;
				Point4f p = new Point4f(camera);
				ray.scale(t);
				p.add(ray);
				drawPosition(g2d, bounds, p);
			}
		}
	}

}
