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

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld;

public abstract class LinearGCodeRenderer extends AbstractGCodeGlRendererOld {

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public void render(GCodeContext preContext,GCodeContext postContext, GCodeCommand command, GL2 gl) {
		enableLineStyle(gl);
		gl.glBegin(GL2.GL_LINE_STRIP);
		// Let's redraw the current position with the accurate color
		if(command.getState().isState(GCodeCommandState.EXECUTED) || command.getState().isState(GCodeCommandState.SENT)){
			gl.glColor3d(0.4,0.4,0.4);
		}else{
			setColor(gl);
		}
		gl.glVertex3d(preContext.getPosition().getX().doubleValue(), preContext.getPosition().getY().doubleValue(), preContext.getPosition().getZ().doubleValue());

		Tuple6b tuple = new Tuple6b(postContext.getPosition());

		if(command.getState().isState(GCodeCommandState.EXECUTED) || command.getState().isState(GCodeCommandState.SENT)){
			gl.glColor3d(0.4,0.4,0.4);
		}else{
			setColor(gl);
		}
		gl.glVertex3d(tuple.getX().doubleValue(), tuple.getY().doubleValue(), tuple.getZ().doubleValue());
		gl.glEnd();
		disableLineStyle(gl);
	}

	protected abstract void enableLineStyle(GL2 gl);
	protected abstract void disableLineStyle(GL2 gl);
	protected abstract void setColor(GL2 gl);
}
