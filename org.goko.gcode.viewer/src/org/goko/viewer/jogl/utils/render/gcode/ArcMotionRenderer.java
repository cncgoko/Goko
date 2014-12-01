package org.goko.viewer.jogl.utils.render.gcode;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.viewer.renderer.IRendererProxy;
import org.goko.viewer.jogl.service.JoglRendererProxy;
import org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class ArcMotionRenderer extends AbstractGCodeCommandRenderer<ArcMotionCommand>{


	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.AbstractGCodeCommandRenderer#render(org.goko.core.gcode.bean.GCodeCommand, org.goko.viewer.jogl.service.JoglRendererProxy, org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler)
	 */
	@Override
	public void render(ArcMotionCommand command, JoglRendererProxy proxy, IJoglGCodeCommandStyler<ArcMotionCommand> styler ) throws GkException{

		if(command != null){
			styler.enableRenderingStyle(command, proxy);
			// TODO change plane selection
			Vector3f plane = new Vector3f(0, 0, 1);
			int direction = IRendererProxy.ARC_COUNTERCLOCKWISE;
			if(command.isClockwise()){
				direction = IRendererProxy.ARC_CLOCKWISE;
			}
			Point3f color = styler.getVertexColor(command, proxy);
			proxy.drawArc(command.getAbsoluteStartCoordinate(), command.getAbsoluteEndCoordinate(),command.getAbsoluteCenterCoordinate(), plane, direction, color);
			styler.disableRenderingStyle(command, proxy);
		}
	}

}
