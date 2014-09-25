package org.goko.viewer.jogl.utils.render.gcode;

import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.viewer.renderer.IRendererProxy;
import org.goko.viewer.jogl.service.JoglRendererProxy;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class ArcMotionRenderer extends AbstractGCodeCommandRenderer<ArcMotionCommand>{
	protected static final String ID = "org.goko.viewer.jogl.utils.render.gcode.ArcMotionRenderer";

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.AbstractJoglRenderer#renderJogl(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void renderJogl(JoglRendererProxy proxy) throws GkException {
		ArcMotionCommand command = getGCodeCommand();

		if(command != null){
			getStyler().enableRenderingStyle(command, proxy);
			// TODO change plane selection
			Point3f plane = new Point3f(1, 1, 0);
			int direction = IRendererProxy.ARC_COUNTERCLOCKWISE;
			if(command.isClockwise()){
				direction = IRendererProxy.ARC_CLOCKWISE;
			}
			Point3f color = getStyler().getVertexColor(command, proxy);
			proxy.drawArc(command.getAbsoluteStartCoordinate(), command.getAbsoluteEndCoordinate(),command.getAbsoluteCenterCoordinate(), plane, direction, color);
			getStyler().disableRenderingStyle(command, proxy);
		}
	}

}
