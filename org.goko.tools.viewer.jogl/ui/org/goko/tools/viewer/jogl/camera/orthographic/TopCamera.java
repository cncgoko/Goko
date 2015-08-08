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

import javax.vecmath.Vector3f;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.goko.tools.viewer.jogl.camera.OrthographicCamera;

import com.jogamp.opengl.swt.GLCanvas;

public class TopCamera extends OrthographicCamera implements MouseMoveListener,MouseListener, Listener {
	public static final String ID = "org.goko.tools.viewer.jogl.camera.orthographic.top";

	public TopCamera(final GLCanvas canvas) {
		super(canvas);		
	}

	/**
	 * @return
	 */
	@Override
	public String getId() {
		return ID;
	}	
	
	@Override
	public String getLabel() {		
		return "Top";
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.camera.OrthographicCamera#panMouse(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	protected void panMouse(MouseEvent e){
		float dx = (float) (-(e.x-last.x) / zoomOffset);
		float dy = (float) ((e.y-last.y) / zoomOffset);
		Vector3f cameraRelativeMove = new Vector3f(2*dx, 2*dy, 0f);

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
		double xWorld = 2*((xMouse - (width / 2)) / zoomOffset) + eye.x;
		double yWorld = -2*((yMouse - (height/ 2)) / zoomOffset) + eye.y;
		zoomOffset = Math.max(0.1, zoomOffset * (1+event.count/30.0) );
		eye.x = (float) (xWorld - 2*((xMouse - (width / 2)) / zoomOffset));
		eye.y = (float) (yWorld + 2*((yMouse - (height/ 2)) / zoomOffset));
	}
}
