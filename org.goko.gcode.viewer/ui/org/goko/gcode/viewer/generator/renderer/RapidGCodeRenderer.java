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
package org.goko.gcode.viewer.generator.renderer;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.gcode.rs274ngcv3.RS274;

public class RapidGCodeRenderer extends LinearGCodeRenderer {

	private static Point3d G00_COLOR = new Point3d(1,0.27,0);

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.renderer.LinearGCodeRenderer#enableLineStyle(javax.media.opengl.GL2)
	 */
	@Override
	protected void enableLineStyle(GL2 gl) {
		gl.glEnable(GL2.GL_LINE_STIPPLE);
		gl.glLineStipple(4, (short)0xAAAA);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.renderer.LinearGCodeRenderer#disableLineStyle(javax.media.opengl.GL2)
	 */
	@Override
	protected void disableLineStyle(GL2 gl) {
		gl.glDisable(GL2.GL_LINE_STIPPLE);

	}

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.renderer.LinearGCodeRenderer#setColor(javax.media.opengl.GL2)
	 */
	@Override
	protected void setColor(GL2 gl) {
		gl.glColor3d(G00_COLOR.x, G00_COLOR.y, G00_COLOR.z);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld#getSupportedMotionType()
	 */
	@Override
	public String getSupportedMotionType() {
		return RS274.MOTION_MODE_RAPID;
	}

}
