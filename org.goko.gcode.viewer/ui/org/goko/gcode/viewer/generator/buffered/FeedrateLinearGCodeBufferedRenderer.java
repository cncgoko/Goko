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

import java.util.Map;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.core.gcode.bean.GCodeCommandState;

public class FeedrateLinearGCodeBufferedRenderer extends LinearGCodeBufferedRenderer {

	public FeedrateLinearGCodeBufferedRenderer(Map<Integer, BufferedRenderingData> buffer) {
		super(buffer);
	}

	private static Point3d G01_COLOR = new Point3d(0.14,0.33,0.80);
	private static Point3d G01_COLOR_EXECUTED = new Point3d(0.3,0.3,0.3);


	@Override
	public String getSupportedMotionType() {
		return "g1";
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
		if(getRenderedCommand() != null &&
				(getRenderedCommand().getState().isState(GCodeCommandState.SENT) || getRenderedCommand().getState().isState(GCodeCommandState.EXECUTED))){
			gl.glColor3d(G01_COLOR_EXECUTED.x, G01_COLOR_EXECUTED.y, G01_COLOR_EXECUTED.z);
		}else{
			gl.glColor3d(G01_COLOR.x, G01_COLOR.y, G01_COLOR.z);
		}
	}

}
