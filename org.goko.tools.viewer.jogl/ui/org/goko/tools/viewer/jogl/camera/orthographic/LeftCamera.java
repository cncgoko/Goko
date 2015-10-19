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
package org.goko.tools.viewer.jogl.camera.orthographic;

import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.vecmath.Vector3f;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.goko.core.common.exception.GkException;
import org.goko.core.math.BoundingTuple6b;
import org.goko.tools.viewer.jogl.camera.OrthographicCamera;
import org.goko.tools.viewer.jogl.service.JoglUtils;

import com.jogamp.opengl.swt.GLCanvas;

public class LeftCamera extends OrthographicCamera implements MouseMoveListener,MouseListener, Listener {
	public static final String ID = "org.goko.tools.viewer.jogl.camera.orthographic.left";

	public LeftCamera(final GLCanvas canvas) {
		super(canvas);		
		up 		= new Vector3f(0,0,1);
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
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#getLabel()
	 */
	@Override
	public String getLabel() {
		return "Left";
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.OrthographicCamera#updatePosition()
	 */
	@Override
	public void updatePosition(){
		spaceWidth = width / zoomOffset;
		spaceHeight = height/ zoomOffset;
		// Set the view port (display area) to cover the entire window
		pmvMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();   
        
        pmvMatrix.glOrthof( (float)(eye.x - spaceWidth), (float)(eye.x + spaceWidth), (float)(eye.z - spaceHeight), (float)(eye.z + spaceHeight), -5000 , 5000 );
        pmvMatrix.glRotatef(90, 0, 1, 0);
        pmvMatrix.glRotatef(-90, 1, 0, 0);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.OrthographicCamera#panMouse(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	protected void panMouse(MouseEvent e){
		float dx = (float) (-(e.x-last.x) / zoomOffset);
		float dy = (float) ((e.y-last.y) / zoomOffset);
		Vector3f cameraRelativeMove = new Vector3f(2*dx, 0f, 2*dy);

		eye.add(cameraRelativeMove);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.OrthographicCamera#mouseScroll(org.eclipse.swt.widgets.Event)
	 */
	@Override
	protected void mouseScroll(Event event) {
		// Zoom on scroll
		int xMouse = event.x;
		int yMouse = event.y;
		double xWorld =  2*((xMouse - (width / 2)) / zoomOffset) + eye.x;
		double yWorld = -2*((yMouse - (height/ 2)) / zoomOffset) + eye.z;
		zoomOffset = Math.max(0.1, zoomOffset * (1+event.count/30.0) );
		eye.x = (float) (xWorld - 2*((xMouse - (width / 2)) / zoomOffset));
		eye.z = (float) (yWorld + 2*((yMouse - (height/ 2)) / zoomOffset));
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.AbstractCamera#zoomToFit(org.goko.core.math.BoundingTuple6b)
	 */
	@Override
	public void zoomToFit(BoundingTuple6b bounds) throws GkException {
		double bWidth  = bounds.getMax().getY().doubleValue(JoglUtils.JOGL_UNIT) - bounds.getMin().getY().doubleValue(JoglUtils.JOGL_UNIT);
		double bHeight = bounds.getMax().getZ().doubleValue(JoglUtils.JOGL_UNIT) - bounds.getMin().getZ().doubleValue(JoglUtils.JOGL_UNIT);

		double boundCenterX = (bounds.getMax().getY().doubleValue() + bounds.getMin().getY().doubleValue() ) /2;
		double boundCenterZ = (bounds.getMax().getZ().doubleValue() + bounds.getMin().getZ().doubleValue() ) /2;

		double targetScaleX = (2 * width  )/ (bWidth + 5);
		double targetScaleZ = (2 * height )/ (bHeight + 5);

		eye.x = (float) -boundCenterX;
		eye.z = (float) boundCenterZ;
		zoomOffset = Math.min(targetScaleX, targetScaleZ);
	}
}
