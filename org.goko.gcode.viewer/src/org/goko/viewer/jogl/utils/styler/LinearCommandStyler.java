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
package org.goko.viewer.jogl.utils.styler;

import javax.media.opengl.GL2;
import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class LinearCommandStyler implements IJoglGCodeCommandStyler<LinearMotionCommand>{
	private static Point3f COLOR_FEEDRATE 	= new Point3f(0.14f,0.33f,0.80f);
	private static Point3f COLOR_RAPID 		= new Point3f(0.458f,0.0f,0.0f);
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#enableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void enableRenderingStyle(LinearMotionCommand command, JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glPushAttrib(GL2.GL_LINE_BIT);
		proxy.getGl().glLineWidth(0.75f);
		if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			proxy.getGl().glEnable(GL2.GL_LINE_STIPPLE);
			proxy.getGl().glLineStipple(1, (short) 0xAAAA);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#disableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void disableRenderingStyle(LinearMotionCommand command, JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glLineWidth(1f);
		if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			proxy.getGl().glDisable(GL2.GL_LINE_STIPPLE);
		}
		proxy.getGl().glPopAttrib();
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#setVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void setVertexColor(LinearMotionCommand command, JoglRendererProxy proxy) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#getVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public Point3f getVertexColor(LinearMotionCommand command, JoglRendererProxy proxy) throws GkException {
		Point3f color = new Point3f(1,1,1);

		if(command.getMotionMode() == EnumGCodeCommandMotionMode.FEEDRATE){
			color = COLOR_FEEDRATE;
		}else if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			color = COLOR_RAPID;
		}
		return color;
	}
}