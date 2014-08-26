package org.goko.gcode.viewer.generator.styler.impl;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.gcode.viewer.generator.styler.IGCodeGlStyler;

public class LinearGCodeStyler implements IGCodeGlStyler<LinearMotionCommand> {
	private static Point3d G00_COLOR = new Point3d(1,0.27,0);
	private static Point3d G01_COLOR = new Point3d(0.14,0.33,0.80);

	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#setRenderingStyle(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public void enableRenderingStyle(LinearMotionCommand command, GL2 gl) throws GkException {
		if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			gl.glEnable(GL2.GL_LINE_STIPPLE);
			gl.glLineStipple(4, (short)0xAAAA);
		}
	}

	@Override
	public void disableRenderingStyle(LinearMotionCommand command, GL2 gl) throws GkException {
		if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			gl.glDisable(GL2.GL_LINE_STIPPLE);
		}
	}

	@Override
	public void setVertexStyle(LinearMotionCommand command, GL2 gl) throws GkException {
		if(command.getMotionMode() == EnumGCodeCommandMotionMode.RAPID){
			gl.glColor3d(G00_COLOR.x, G00_COLOR.y, G00_COLOR.z);
		}else{
			gl.glColor3d(G01_COLOR.x, G01_COLOR.y, G01_COLOR.z);
		}
	}
	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#getSupportedCommandClass()
	 */
	@Override
	public Class<LinearMotionCommand> getSupportedCommandClass() {
		return LinearMotionCommand.class;
	}

}
