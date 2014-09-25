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
package org.goko.viewer.jogl.utils.render;

import javax.media.opengl.GL2;

import org.goko.core.common.exception.GkException;
import org.goko.viewer.jogl.service.JoglRendererProxy;

/**
 * Draw the XYZ axis
 *
 * @author PsyKo
 *
 */
public class GridRenderer extends AbstractJoglRenderer {
	public static final String ID = "org.goko.viewer.jogl.utils.render.GridRenderer";
	/**
	 * Constructor
	 */
	public GridRenderer() {
		super();
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	/**
	 * {@inheritDoc}
	 *
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.renderer.IRendererProxy)
	 */
	@Override
	public void renderJogl(JoglRendererProxy proxy) throws GkException {
		GL2 gl = proxy.getGl();
		gl.glLineWidth(0.1f);

		gl.glBegin(GL2.GL_LINES);
		// Main divisions
		gl.glColor4d(0.4, 0.4, 0.4, 0.7);
		int size = 100;
		for (int i = -size; i <= size; i += 10) {
			gl.glVertex3d(i, -size, 0);
			gl.glVertex3d(i, size, 0);
			gl.glVertex3d(-size, i, 0);
			gl.glVertex3d(size, i, 0);
		}
		gl.glEnd();
		gl.glLineWidth(0.5f);
		gl.glLineStipple(4, (short) 0xA0A0);
		gl.glBegin(GL2.GL_LINES);


		for (int i = -size; i <= size; i++) {
			if (i == 0) {
				gl.glColor4d(0.8, 0.8, 0.8, 0.50);
				gl.glVertex3d(i, -size, 0);
				gl.glVertex3d(i, size, 0);
				gl.glVertex3d(-size, i, 0);
				gl.glVertex3d(size, i, 0);
			} else {
				if (i % 10 != 0) {
					gl.glColor4d(0.4, 0.4, 0.4, 0.1);
					gl.glVertex3d(i, -size, 0);
					gl.glVertex3d(i, size, 0);
					gl.glVertex3d(-size, i, 0);
					gl.glVertex3d(size, i, 0);
				}
			}
		}
		// sub divisions
		gl.glEnd();
	}

}
