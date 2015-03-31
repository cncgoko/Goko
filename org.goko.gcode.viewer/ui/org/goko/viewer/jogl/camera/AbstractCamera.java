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
package org.goko.viewer.jogl.camera;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.BoundingTuple6b;

import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.awt.Overlay;


public abstract class AbstractCamera implements GLEventListener{
	protected int height;
	protected int width;
	protected int x;
	protected int y;
	private boolean activated;
	private boolean initialized;
	protected PMVMatrix pmvMatrix;
	protected Overlay overlay;
	public abstract String getId();
	public abstract String getLabel();
	public abstract void setup();
	public abstract void updatePosition();
	public abstract void lookAt(Point3d position);

	public AbstractCamera() {
		activated = false;
	}

	public void updateViewport(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void zoomToFit(BoundingTuple6b bounds) throws GkException;
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
		updateViewport(x, y, width, height);
	}
	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}
	/**
	 * @param activated the activated to set
	 */
	public void setActivated(boolean activated) {
		if(this.activated != activated){
			setInitialized(false);
		}
		this.activated = activated;
	}
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}
	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	/**
	 * @return the pmvMatrix
	 */
	public PMVMatrix getPmvMatrix() {
		return pmvMatrix;
	}
	/**
	 * @param pmvMatrix the pmvMatrix to set
	 */
	public void setPmvMatrix(PMVMatrix pmvMatrix) {
		this.pmvMatrix = pmvMatrix;
	}
	/**
	 * @return the overlay
	 */
	protected Overlay getOverlay() {
		return overlay;
	}
	/**
	 * @param overlay the overlay to set
	 */
	protected void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}
}
