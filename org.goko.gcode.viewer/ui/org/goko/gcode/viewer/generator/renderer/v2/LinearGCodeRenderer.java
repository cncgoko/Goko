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
package org.goko.gcode.viewer.generator.renderer.v2;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer;

public class LinearGCodeRenderer extends AbstractGCodeGlRenderer<LinearMotionCommand>{
	private static Point3d G01_COLOR = new Point3d(0.14,0.33,0.80);
	private static Point3d G01_COLOR_EXECUTED = new Point3d(0.3,0.3,0.3);

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public void render(LinearMotionCommand command, GL2 gl) throws GkException {
		enableRenderingStyle(command, gl);
		gl.glBegin(GL2.GL_LINE_STRIP);

		Point3d start 	= command.getAbsoluteStartCoordinate().toPoint3d();
		Point3d end 	= command.getAbsoluteEndCoordinate().toPoint3d();

		setVertexStyle(command, gl);
		gl.glVertex3d(start.x, start.y, start.z);

		setVertexStyle(command, gl);
		gl.glVertex3d(end.x, end.y, end.z);
		gl.glEnd();
		disableRenderingStyle(command, gl);
	}

	@Override
	public Class<LinearMotionCommand> getSupportedCommandClass() {
		return LinearMotionCommand.class;
	}


}
