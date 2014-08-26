package org.goko.gcode.viewer.generator.styler.impl;

import javax.media.opengl.GL2;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.gcode.viewer.generator.styler.IGCodeGlStyler;

public class ArcGCodeStyler implements IGCodeGlStyler<ArcMotionCommand> {
	private static Point3d ARC_COLOR = new Point3d(0,0.86,0);

	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#enableRenderingStyle()
	 */
	@Override
	public void enableRenderingStyle(ArcMotionCommand command, GL2 gl) throws GkException {
		// Nothing here
	}
	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#disableRenderingStyle()
	 */
	@Override
	public void disableRenderingStyle(ArcMotionCommand command, GL2 gl) throws GkException {
		// Nothing here
	}
	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#setVertexStyle()
	 */
	@Override
	public void setVertexStyle(ArcMotionCommand command, GL2 gl) throws GkException {
		gl.glColor3d(ARC_COLOR.x, ARC_COLOR.y, ARC_COLOR.z);
	}

	/** {@inheritDoc}
	 * @see org.goko.gcode.viewer.generator.styler.IGCodeGlStyler#getSupportedCommandClass()
	 */
	@Override
	public Class<ArcMotionCommand> getSupportedCommandClass() {
		return ArcMotionCommand.class;
	}



}
