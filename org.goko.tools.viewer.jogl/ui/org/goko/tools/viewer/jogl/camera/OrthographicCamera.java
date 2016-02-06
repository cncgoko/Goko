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

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2i;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.goko.core.common.exception.GkException;
import org.goko.core.math.BoundingTuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;

import com.jogamp.opengl.swt.GLCanvas;
import com.jogamp.opengl.util.PMVMatrix;

public abstract class OrthographicCamera extends AbstractCamera implements MouseMoveListener, MouseListener, FocusListener, Listener {
	public static final String ID = "org.goko.tools.viewer.jogl.camera.OrthographicCamera";
	protected Point2i last;
	protected Point3f eye;
	protected Vector3f up;
	protected GLU glu;
	protected GLCanvas glCanvas;
	protected double zoomOffset;
	protected double spaceWidth;
	protected double spaceHeight;

	public OrthographicCamera(final GLCanvas canvas) {
		super();
		this.glCanvas = canvas;
		glCanvas.addMouseListener(this);
		glCanvas.addMouseMoveListener(this);

		glCanvas.addListener(SWT.MouseWheel, this);


		last 	= new Point2i();
		glu = new GLU();
		eye 	= new Point3f(0,0,0);
		up 		= new Vector3f(0,1,0);
		zoomOffset = 1;
		pmvMatrix = new PMVMatrix();
	}

	/**
	 * @return
	 */
	@Override
	public String getId() {
		return ID;
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#setup()
	 */
	@Override
	public void setup() {

	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#updatePosition()
	 */
	@Override
	public void updatePosition(){
		spaceWidth = width / zoomOffset;
		spaceHeight = height/ zoomOffset;
		// Set the view port (display area) to cover the entire window
		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.glOrthof( (float)(eye.x - spaceWidth), (float)(eye.x + spaceWidth), (float)(eye.y - spaceHeight), (float)(eye.y + spaceHeight), -5000 , 5000 );

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
			}
		}
	}

	protected void zoomMouse(MouseEvent e){
		if(glCanvas.isFocusControl() && isActivated()){
			zoomOffset = Math.max(0.1, zoomOffset+ (e.y-last.y) / 50.0);
		}
	}

	protected abstract void panMouse(MouseEvent e);


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
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {  }

	/** (inheritDoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if(glCanvas.isFocusControl() && isActivated()){
			mouseScroll(event);
		}
	}

	protected abstract void mouseScroll(Event event);

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#getLabel()
	 */
	@Override
	public String getLabel() {
		return "Orthographic";
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#lookAt(javax.vecmath.Point3d)
	 */
	@Override
	public void lookAt(Point3d position) {
		eye.x = (float) position.x;
		eye.y = (float) position.y;
		eye.z = (float) position.z;
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		super.reshape(drawable, x, y, width, height);

		if (height == 0) {
			height = 1; // prevent divide by zero
		}

		// Set the view port (display area) to cover the entire window
		this.height = height;
		this.width = width;
		spaceWidth = width / zoomOffset;
		spaceHeight = height/ zoomOffset;

		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.glOrthof( (float)(eye.x - spaceWidth), (float)(eye.x + spaceWidth), (float)(eye.y - spaceHeight), (float)(eye.y + spaceHeight), 0 , 5000 );
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#zoomToFit(org.goko.core.math.BoundingTuple6b)
	 */
	@Override
	public void zoomToFit(BoundingTuple6b bounds) throws GkException {
		double bWidth  = bounds.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT) - bounds.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT);
		double bHeight = bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT) - bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT);

		double boundCenterX = (bounds.getMax().getX().doubleValue(JoglUtils.JOGL_UNIT) + bounds.getMin().getX().doubleValue(JoglUtils.JOGL_UNIT) ) /2;
		double boundCenterY = (bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT) + bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT) ) /2;

		double targetScaleX = (2 * width  )/ (bWidth + 5);
		double targetScaleY = (2 * height )/ (bHeight + 5);

		eye.x = (float) boundCenterX;
		eye.y = (float) boundCenterY;
		zoomOffset = Math.min(targetScaleX, targetScaleY);
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		last = null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		last = null;
	}

}
