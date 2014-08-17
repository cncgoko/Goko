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
package org.goko.gcode.viewer.camera;

import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2i;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.goko.gcode.viewer.CameraMotionMode;

public class PerspectiveCamera extends AbstractCamera implements MouseMoveListener,MouseListener,Listener {
	protected Point2i last;
	protected Point3f eye;
	protected Point3f at;
	protected Point3f target;
	protected Vector3f up;
	private double angleVertical ;
	private double angleHorizontal;
	private double maxAngleLimit = Math.PI/100;
	private double distance;
	private GLCanvas glCanvas;
	private GLU glu;

	public PerspectiveCamera(final GLCanvas canvas) {
		this.glCanvas = canvas;
		glCanvas.addMouseListener(this);
		glCanvas.addMouseMoveListener(this);

		glCanvas.addListener(SWT.MouseWheel, this);

		glCanvas.addListener(SWT.MouseEnter, new Listener() {

			@Override
			public void handleEvent(Event event) {
				glCanvas.forceFocus();
			}
		});

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

	@Override
	public void setup(){
		Rectangle bounds = glCanvas.getBounds();
		float width 	= bounds.width;
		float height 	= bounds.height;
		float fAspect = width / height;

		glu.gluPerspective(45.0f, fAspect, 0.5f, 1000.0f);

	}

	protected void update() {
	  // Update camera position in spherical coordinate system
	  eye.x = (float) (target.x + Math.cos(angleHorizontal) * Math.sin(angleVertical) * distance);
	  eye.y = (float) (target.y + Math.sin(angleHorizontal) * Math.sin(angleVertical) * distance);
	  eye.z = (float) (target.z + Math.cos(angleVertical) * distance);
	  glCanvas.redraw();
	}

	@Override
	public void updatePosition(){
		//update();
        glu.gluLookAt(eye.x, eye.y, eye.z,
      			target.x, target.y, target.z,
      			up.x, up.y, up.z);

	}
	@Override
	public void mouseMove(MouseEvent e) {
		CameraMotionMode mode = null;

		if((e.stateMask & SWT.BUTTON1) != 0 ){
			if((e.stateMask & SWT.ALT) != 0 ){
				mode = CameraMotionMode.ORBIT;

			}else{
				mode = CameraMotionMode.PAN;
			}
		}else if((e.stateMask & SWT.BUTTON3) != 0 ){
			mode = CameraMotionMode.ZOOM;
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
	protected void zoomMouse(MouseEvent e){
		distance += (e.y - last.y)/20.0;
		distance = Math.min(1000, Math.max(1, distance));
	}
	protected void panMouse(MouseEvent e){
		Vector3f yCameraRelative = new Vector3f(target.x - eye.x, target.y - eye.y, 0);
		yCameraRelative.normalize();
		Vector3f xCameraRelative = new Vector3f();
		xCameraRelative.cross(yCameraRelative, up);
		xCameraRelative.scale((float) (-(e.x-last.x) / 20.0));
		yCameraRelative.z = 0;
		yCameraRelative.scale((float) ((e.y-last.y) / 20.0));
		xCameraRelative.add(yCameraRelative);
		target.add(xCameraRelative);
	}

	protected void orbitMouse(MouseEvent e){
		angleHorizontal += (float) ((e.x-last.x) / 100.0)%Math.PI;
		angleVertical += (float) ((e.y-last.y) / 100.0)%Math.PI;
		angleVertical = Math.min(Math.PI-maxAngleLimit, Math.max(+maxAngleLimit, angleVertical));
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {  }

	@Override
	public void mouseDown(MouseEvent e) {
		last = new Point2i(e.x, e.y);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEvent(Event event) {
		distance =  distance * (1 - event.count/20.0);
		distance = Math.min(1000, Math.max(1, distance));
		update();
	}

	@Override
	public String getLabel() {
		return "Perspective";
	}
}
