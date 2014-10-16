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
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class ArcCommandStyler implements IJoglGCodeCommandStyler<ArcMotionCommand>{
	private static Point3f COLOR_ARC 		= new Point3f(0,0.86f,0);
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#enableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void enableRenderingStyle(ArcMotionCommand command, JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glPushAttrib(GL2.GL_LINE_BIT);
		proxy.getGl().glLineWidth(0.75f);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#disableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void disableRenderingStyle(ArcMotionCommand command, JoglRendererProxy proxy) throws GkException {
		proxy.getGl().glPopAttrib();
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#setVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void setVertexColor(ArcMotionCommand command, JoglRendererProxy proxy) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler#getVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public Point3f getVertexColor(ArcMotionCommand command, JoglRendererProxy proxy) throws GkException {
		return COLOR_ARC;
	}
}