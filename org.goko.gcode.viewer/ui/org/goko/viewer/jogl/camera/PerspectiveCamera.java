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
package org.goko.viewer.jogl.camera;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
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

import com.jogamp.opengl.swt.GLCanvas;

public class PerspectiveCamera extends AbstractCamera implements MouseMoveListener,MouseListener,Listener {
	public static final String ID = "org.goko.gcode.viewer.camera.PerspectiveCamera";

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
	 * @see org.goko.viewer.jogl.camera.AbstractCamera#setup()
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
	 * @see org.goko.viewer.jogl.camera.AbstractCamera#updatePosition()
	 */
	@Override
	public void updatePosition(){
        glu.gluLookAt(eye.x, eye.y, eye.z,
      			target.x, target.y, target.z,
      			up.x, up.y, up.z);
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
		if(glCanvas.isFocusControl() && isActivated()){
			distance =  distance * (1 - event.count/20.0);
			distance = Math.min(1000, Math.max(1, distance));
			update();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.camera.AbstractCamera#getLabel()
	 */
	@Override
	public String getLabel() {
		return "Perspective";
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.camera.AbstractCamera#lookAt(javax.vecmath.Point3d)
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
		GLU glu = new GLU();
		if (height == 0) {
			height = 1; // prevent divide by zero
		}
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL2.GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 1000.0); // fovy, aspect, zNear,
														// zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.camera.AbstractCamera#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
}
