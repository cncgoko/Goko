package org.goko.core.viewer.renderer.gcode;

import javax.vecmath.Point3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.viewer.renderer.IRendererProxy;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class ArcMotionRenderer extends AbstractGCodeCommandRenderer<ArcMotionCommand>{
	protected static final String ID = "org.goko.core.viewer.renderer.gcode.ArcMotionRenderer";

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	/**
	 * Constructor
	 * @param command the rendered command
	 */
	public ArcMotionRenderer(ArcMotionCommand command) {
		super(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.service.IViewer3DService)
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		ArcMotionCommand command = getGCodeCommand();


		if(command != null){
			// TODO change plane selection
			Point3f plane = new Point3f(1, 1, 0);
			int direction = IRendererProxy.ARC_COUNTERCLOCKWISE;
			if(command.isClockwise()){
				direction = IRendererProxy.ARC_CLOCKWISE;
			}
			proxy.drawArc(command.getAbsoluteStartCoordinate(), command.getAbsoluteEndCoordinate(),command.getAbsoluteCenterCoordinate(), plane, direction);
		}
	}

}
