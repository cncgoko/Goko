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
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandType;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;

public class DefaultGCodeCommandStyler extends AbstractGCodeCommandStyler{
	private static Point3f COLOR_FEEDRATE 	= new Point3f(0.14f,0.33f,0.80f);
	private static Point3f COLOR_ARC 		= new Point3f(0,0.86f,0);
	private static Point3f COLOR_SENT 		= new Point3f(0.3f,0.3f,0.3f);
	private static Point3f COLOR_RAPID 		= new Point3f(0.458f,0.0f,0.0f);
	private static Point3f COLOR_ERROR		= new Point3f(1f,0.0f,0.0f);

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.AbstractGCodeCommandStyler#enableRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void enableRenderingStyle(GCodeCommand command, JoglRendererProxy proxy) throws GkException {
		if(command.getType() == EnumGCodeCommandType.MOTION){
			MotionCommand motionCommand = (MotionCommand) command;
			if(motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
				proxy.getGl().glEnable(GL2.GL_LINE_STIPPLE);
				proxy.getGl().glLineStipple(1, (short) 0xAAAA);
			}
		}
	}

	@Override
	public void disableRenderingStyle(GCodeCommand command, JoglRendererProxy proxy) throws GkException {
		if(command.getType() == EnumGCodeCommandType.MOTION){
			proxy.getGl().glLineWidth(1f);
			MotionCommand motionCommand = (MotionCommand) command;
			if(motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
				proxy.getGl().glDisable(GL2.GL_LINE_STIPPLE);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.AbstractGCodeCommandStyler#setVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void setVertexColor(GCodeCommand command, JoglRendererProxy proxy) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.styler.AbstractGCodeCommandStyler#getVertexColor(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public Point3f getVertexColor(GCodeCommand command, JoglRendererProxy proxy) throws GkException {
		Point3f color = new Point3f(1,1,1);
		if(command.getState() != null && command.getState().isState(GCodeCommandState.SENT)){
			color = COLOR_SENT;
		}else if(command.getState() != null && command.getState().isState(GCodeCommandState.ERROR)){
			color = COLOR_ERROR;
		}else {
			if(command.getType() == EnumGCodeCommandType.MOTION){
				MotionCommand motionCommand = (MotionCommand) command;
				if(motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.FEEDRATE){
					color = COLOR_FEEDRATE;
				}else if(motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.ARC_CLOCKWISE
					  || motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.ARC_COUNTERCLOCKWISE){
					color = COLOR_ARC;
				}else if(motionCommand.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
					color = COLOR_RAPID;
				}else{

				}
			}
		}
		return color;
	}


}
