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

public class OrthographicCamera extends AbstractCamera implements MouseMoveListener,MouseListener, Listener {
	protected Point2i last;
	protected Point3f eye;
	protected Vector3f up;
	protected GLU glu;
	private GLCanvas glCanvas;
	private double zoomOffset;

	public OrthographicCamera(final GLCanvas canvas) {
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

		last 	= new Point2i();
		glu = new GLU();
		eye 	= new Point3f(0,0,0);
		up 		= new Vector3f(0,1,0);
		zoomOffset = 1;
		update();
	}

	@Override
	public void setup() {

	}
	protected void update() {
	  // Update camera position in spherical coordinate system
	  glCanvas.redraw();
	}

	@Override
	public void updatePosition(){
		Rectangle bounds = glCanvas.getBounds();
		float width 	= bounds.width;
		float height 	= bounds.height;
		double spaceWidth = width / zoomOffset;
		double spaceHeight = height/ zoomOffset;
		GLU.getCurrentGL().getGL2().glOrtho( eye.x - spaceWidth, eye.x + spaceWidth, eye.y - spaceHeight, eye.y + spaceHeight, -500 , 500);
		//glu.gluOrtho2D( eye.x - spaceWidth, eye.x + spaceWidth, eye.y - spaceHeight, eye.y + spaceHeight);
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
				case ZOOM:zoomMouse(e);
				default:;
			}

			last.x = e.x;
			last.y = e.y;
			update();
		}

	}
	protected void zoomMouse(MouseEvent e){
		zoomOffset = Math.max(0.1, zoomOffset+ (e.y-last.y) / 50.0);
		update();
	}
	protected void panMouse(MouseEvent e){
		float dx = (float) (-(e.x-last.x) / zoomOffset);
		float dy = (float) ((e.y-last.y) / zoomOffset);
		Vector3f cameraRelativeMove = new Vector3f(dx, dy, 0f);

		eye.add(cameraRelativeMove);
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
		zoomOffset = Math.max(0.1, zoomOffset * (1+event.count/30.0) );
		update();
	}

	@Override
	public String getLabel() {
		return "Orthographic";
	}
}
