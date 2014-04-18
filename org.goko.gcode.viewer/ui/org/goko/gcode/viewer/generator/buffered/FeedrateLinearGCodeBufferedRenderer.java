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
package org.goko.gcode.viewer.generator.buffered;

import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.gcode.rs274ngcv3.command.FeedrateLinearMotionGCodeCommand;

public class FeedrateLinearGCodeBufferedRenderer extends LinearGCodeBufferedRenderer<FeedrateLinearMotionGCodeCommand> {

	public FeedrateLinearGCodeBufferedRenderer( Map<Integer, List<Point3d>> buffer) {
		super(buffer);
	}

	private static Point3d G01_COLOR = new Point3d(0.14,0.33,0.80);

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer#getRenderedCommandClass()
	 */
	@Override
	public Class<FeedrateLinearMotionGCodeCommand> getRenderedCommandClass() {
		return FeedrateLinearMotionGCodeCommand.class;
	}

	@Override
	protected void enableLineStyle(GL2 gl) {
		// Nothing to do
	}

	@Override
	protected void disableLineStyle(GL2 gl) {
		// Nothing to do

	}

	@Override
	protected void setColor(GL2 gl) {
		gl.glColor3d(G01_COLOR.x, G01_COLOR.y, G01_COLOR.z);
	}

}
