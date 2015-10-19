/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.tools.viewer.jogl.camera;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2i;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.goko.core.common.exception.GkException;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;

import com.jogamp.opengl.swt.GLCanvas;
import com.jogamp.opengl.util.PMVMatrix;

public class PerspectiveCamera extends AbstractCamera implements MouseMoveListener,MouseListener,Listener {
	public static final String ID = "org.goko.tools.viewer.jogl.camera.PerspectiveCamera";

	protected Point2i last;
	protected Point3f eye;
	protected Point3f at;
	protected Point3f target;
	protected Vector3f up;

	private double angleVertical ;
	private double angleHorizontal;
	private double maxAngleLimit = Math.PI/300;
	private double distance;
	private GLCanvas glCanvas;
	private GLU glu;
	private float fAspect;

	public Point2i screenMin;
	public Point2i screenMax;
	public Point2i screenCenter;
	/**
	 * Constructor
	 * @param canvas the canvas
	 */
	public PerspectiveCamera(final GLCanvas canvas) {
		super();
		this.glCanvas = canvas;
		fAspect = 0;
		glCanvas.addMouseListener(this);
		glCanvas.addMouseMoveListener(this);
		glCanvas.addListener(SWT.MouseWheel, this);
		pmvMatrix = new PMVMatrix();

		glu 	= new GLU();
		last 	= new Point2i();
		eye 	= new Point3f(0,0,15);
		at 		= new Point3f(0,0,0);
		target 	= new Point3f();
		up 		= new Vector3f(0,0,1);

		angleHorizontal = 0;
		angleVertical 	= Math.PI/4;
		angleHorizontal = -Math.PI/4;
		distance 		= 120;


		update();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#setup()
	 */
	@Override
	public void setup(){
		fAspect = 0;
		if(height != 0){
			fAspect = width / height;
		}
		glCanvas.getGL().getGL2().glMatrixMode(GL2.GL_MODELVIEW);  // choose projection matrix
		glCanvas.getGL().getGL2().glLoadIdentity();             // reset projection matrix
		glu.gluPerspective(45.0f, fAspect, 0.5f, 10000.0f);

		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		pmvMatrix.glLoadIdentity();

		float fov = 45.0f;
		float aspect = fAspect;
		float zNear = 0.5f;
		float zFar = 10000.0f;

		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		pmvMatrix.glLoadIdentity();
		pmvMatrix.gluPerspective(fov, aspect, zNear, zFar);
		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		pmvMatrix.glLoadIdentity();

		pmvMatrix.update();

	//	http://stackoverflow.com/questions/13462516/camera-rotation
	}
	/**
	 *  Update camera position in spherical coordinate system
	 */
	protected void update() {

		Vector3f direction = new Vector3f(	(float)(Math.cos(angleHorizontal) * Math.sin(angleVertical)),
									(float)(Math.sin(angleHorizontal) * Math.sin(angleVertical)),
									(float)(Math.cos(angleVertical)) );
		eye.x = (float) (target.x + direction.x * distance);///Math.cos(angleHorizontal) * Math.sin(angleVertical) * distance);
		eye.y = (float) (target.y + direction.y * distance);//Math.sin(angleHorizontal) * Math.sin(angleVertical) * distance);
		eye.z = (float) (target.z + direction.z * distance);//Math.cos(angleVertical) * distance);


	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#updatePosition()
	 */
	@Override
	public void updatePosition(){
        glu.gluLookAt(eye.x, eye.y, eye.z,
      			target.x, target.y, target.z,
      			up.x, up.y, up.z);
        updatePMVMatrix();
	}

	private void updatePMVMatrix(){
        pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluLookAt(eye.x, eye.y, eye.z,
      			target.x, target.y, target.z,
      			up.x, up.y, up.z);
        pmvMatrix.update();
	}
	/** (inheritDoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent e) {
		if(glCanvas.isFocusControl() && isActivated()){
			CameraMotionMode mode = null;
			if((e.stateMask & SWT.BUTTON1) != 0 ){
				if((e.stateMask & SWT.ALT) != 0 ){
					mode = CameraMotionMode.ZOOM;

				}else{
					mode = CameraMotionMode.PAN;
				}
			}else if((e.stateMask & SWT.BUTTON3) != 0 ){
				mode = CameraMotionMode.ORBIT;
			}

			if(mode != null){
				if(last == null){
					last = new Point2i(e.x,e.y);
				}
				switch(mode){
				case PAN:panMouse(e);
				break;
				case ORBIT:orbitMouse(e);
				break;
				case ZOOM:zoomMouse(e);
				}

				last.x = e.x;
				last.y = e.y;
				update();
			}
		}
	}
	protected void zoomMouse(MouseEvent e){
		distance += (e.y - last.y)/20.0;
		distance = Math.min(1000, Math.max(1, distance));
	}
	protected void panMouse(MouseEvent e){
		Vector3f yCameraRelative = new Vector3f(target.x - eye.x, target.y - eye.y, 0);
		yCameraRelative.normalize();
		Vector3f xCameraRelative = new Vector3f();
		xCameraRelative.cross(yCameraRelative, up);
		float factor = (float) (distance / 600);
		xCameraRelative.scale( -(e.x-last.x) * factor);
		yCameraRelative.z = 0;
		yCameraRelative.scale((e.y-last.y)* factor);
		xCameraRelative.add(yCameraRelative);
		target.add(xCameraRelative);
	}

	protected void orbitMouse(MouseEvent e){
		angleHorizontal += (float) ((e.x-last.x) / 100.0)%Math.PI;
		angleVertical += (float) ((e.y-last.y) / 100.0)%Math.PI;
		angleVertical = Math.min(Math.PI-maxAngleLimit, Math.max(+maxAngleLimit, angleVertical));
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {  }

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		last = new Point2i(e.x, e.y);
		glCanvas.forceFocus();
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {

	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		// Zoom on scroll
		if(glCanvas.isFocusControl() && isActivated()){
			distance =  distance * (1 - event.count/20.0);
			distance = Math.min(1000, Math.max(1, distance));
			update();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#zoomToFit(org.goko.core.math.BoundingTuple6b)
	 */
	@Override
	public void zoomToFit(BoundingTuple6b bounds) throws GkException {
		double boundCenterX = (bounds.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT) + bounds.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT) ) /2;
		double boundCenterY = (bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT) + bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT) ) /2;

		target.x = (float) boundCenterX;
		target.y = (float) boundCenterY;
		update();
		updatePMVMatrix();

		Point3d pa = bounds.getMin().toPoint3d();
		BoundingTuple6b projectedBound = getProjectedBound(bounds);
		for(int i =0; i < 2; i++){
			double[] screenCenter = new double[]{ (projectedBound.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT) + projectedBound.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT)) / 2,
												(projectedBound.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT) + projectedBound.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT)) / 2,
												(projectedBound.getMax().getZ().doubleValue(JoglUtils.JOGL_UNIT) + projectedBound.getMin().getZ().doubleValue(JoglUtils.JOGL_UNIT)) / 2};
			float[] worldCenter = new float[4];
			getPmvMatrix().gluUnProject((float)screenCenter[0],
										(float)screenCenter[1],
										(float)screenCenter[2],
										new int[]{x,y,width,height},
										0,
										worldCenter,
										0);

//			target.x = worldCenter[0];
//			target.y = worldCenter[1];
			update();
			updatePMVMatrix();
		}
		//Let's zoom now
		int i = 0;
		while(!isBoundInScreen(bounds) && i < 100){
			distance *= 1.1;
			update();
			updatePMVMatrix();
			i++;
		}
		i = 0;
		while(isBoundInScreen(bounds) && i < 1000){
			distance *= 0.99;
			update();
			updatePMVMatrix();
			i++;
		}
		distance *= 1.1;
		update();
		updatePMVMatrix();
		projectedBound = getProjectedBound(bounds);
		this.screenMin = new Point2i(projectedBound.getMin().getX().value(JoglUtils.JOGL_UNIT).intValue(), projectedBound.getMin().getY().value(JoglUtils.JOGL_UNIT).intValue());
		this.screenMax = new Point2i(projectedBound.getMax().getX().value(JoglUtils.JOGL_UNIT).intValue(), projectedBound.getMax().getY().value(JoglUtils.JOGL_UNIT).intValue());
	}

	protected BoundingTuple6b getProjectedBound(BoundingTuple6b bounds){

		float xMx = (float) bounds.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT);
		float yMx = (float) bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT);
		float zMx = (float) bounds.getMax().getZ().doubleValue(JoglUtils.JOGL_UNIT);
		float xMn = (float) bounds.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT);
		float yMn = (float) bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT);
		float zMn = (float) bounds.getMin().getZ().doubleValue(JoglUtils.JOGL_UNIT);

		float[] p1Low  = new float[]{xMn, yMn, zMn};
		float[] p2Low  = new float[]{xMx, yMn, zMn};
		float[] p3Low  = new float[]{xMx, yMx, zMn};
		float[] p4Low  = new float[]{xMn, yMx, zMn};
		float[] p1High = new float[]{xMn, yMn, zMx};
		float[] p2High = new float[]{xMx, yMn, zMx};
		float[] p3High = new float[]{xMx, yMx, zMx};
		float[] p4High = new float[]{xMn, yMx, zMx};
		float[][] pts = new float[][]{ p1Low, p2Low, p3Low, p4Low, p1High, p2High, p3High, p4High};
		float[] screenMin = new float[]{Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE};
		float[] screenMax = new float[]{-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE};
		float[] screenP = new float[4];
		for(int i = 0; i < pts.length; i++){
			float[] pt = pts[i];
			boolean result = getPmvMatrix().gluProject( pt[0],pt[1],pt[2], new int[]{0,0,width,height}, 0, screenP, 0);
			if(result){
				for(int j = 0; j < 4; j++){
					if(screenP[j] < screenMin[j]){
						screenMin[j] = screenP[j];
					}
					if(screenP[j] > screenMax[j]){
						screenMax[j] = screenP[j];
					}
				}
			}
		}

		return new BoundingTuple6b(new Tuple6b(screenMin[0], screenMin[1], screenMin[2], JoglUtils.JOGL_UNIT),
								   new Tuple6b(screenMax[0], screenMax[1], screenMax[2], JoglUtils.JOGL_UNIT));
	}
	protected boolean isBoundInScreen(BoundingTuple6b bounds){
		float xMx = (float) bounds.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT);
		float yMx = (float) bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT);
		float zMx = (float) bounds.getMax().getZ().doubleValue(JoglUtils.JOGL_UNIT);
		float xMn = (float) bounds.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT);
		float yMn = (float) bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT);
		float zMn = (float) bounds.getMin().getZ().doubleValue(JoglUtils.JOGL_UNIT);

		List<Point3f> lstAabbPoints = new ArrayList<Point3f>();
		lstAabbPoints.add(new Point3f(xMn, yMn, zMn));
		lstAabbPoints.add(new Point3f(xMx, yMn, zMn));
		lstAabbPoints.add(new Point3f(xMx, yMx, zMn));
		lstAabbPoints.add(new Point3f(xMn, yMx, zMn));
//		lstAabbPoints.add(new Point3f(xMn, yMn, zMx));
//		lstAabbPoints.add(new Point3f(xMx, yMn, zMx));
//		lstAabbPoints.add(new Point3f(xMx, yMx, zMx));
//		lstAabbPoints.add(new Point3f(xMn, yMx, zMx));

		Point3f screen = new Point3f();

		for (Point3f point3f : lstAabbPoints) {
			getProjectedPoint(point3f, screen);
			if(screen.x < 0 || screen.x >  width
			|| screen.y < 0 || screen.y >  height){
				return false;
			}
		}
		return true;
	}

	protected void getProjectedPoint(Point3f obj, Point3f screen){
		float[] screenCoord = new float[4];
		getPmvMatrix().gluProject( obj.x, obj.y, obj.z, new int[]{0,0,width,height}, 0, screenCoord, 0);
		screen.x = screenCoord[0];
		screen.y = screenCoord[1];
		screen.z = screenCoord[2];
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#getLabel()
	 */
	@Override
	public String getLabel() {
		return "Perspective";
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#lookAt(javax.vecmath.Point3d)
	 */
	@Override
	public void lookAt(Point3d lookat) {
		target.x = (float) lookat.x;
		target.y = (float) lookat.y;
		target.z = (float) lookat.z;
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void display(GLAutoDrawable arg0) { }

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void dispose(GLAutoDrawable arg0) { }

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void init(GLAutoDrawable arg0) {
		setup();
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		super.reshape(drawable, x, y, width, height);
		GL2 gl = drawable.getGL().getGL2();

		if (height == 0) {
			height = 1; // prevent divide by zero
		}


		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		float fov = 45.0f;
		float aspect = (float) width / height;
		float zNear = 0.5f;
		float zFar = 10000.0f;

		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		pmvMatrix.glLoadIdentity();
		pmvMatrix.gluPerspective(fov, aspect, zNear, zFar);
		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		pmvMatrix.glLoadIdentity();
//		pmvMatrix.gluLookAt(eye.x, eye.y, eye.z,
//      			target.x, target.y, target.z,
//      			up.x, up.y, up.z);
		pmvMatrix.update();

		// Setup perspective projection, with aspect ratio matches viewport
//		gl.glMatrixMode(GL2.GL_PROJECTION); // choose projection matrix
//		gl.glLoadIdentity(); // reset projection matrix
//		glu.gluPerspective(45.0, aspect, 0.1, 1000.0); // fovy, aspect, zNear,
//														// zFar
//
//		// Enable the model-view transform
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		gl.glLoadIdentity(); // reset

	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/**
	 * @return the pmvMatrix
	 */
	@Override
	public PMVMatrix getPmvMatrix() {
		return pmvMatrix;
	}

	/**
	 * @param pmvMatrix the pmvMatrix to set
	 */
	@Override
	public void setPmvMatrix(PMVMatrix pmvMatrix) {
		this.pmvMatrix = pmvMatrix;
	}
}
