package org.goko.core.viewer.renderer.gcode;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.viewer.renderer.IRendererProxy;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class LinearMotionRenderer extends AbstractGCodeCommandRenderer<LinearMotionCommand>{
	protected static final String ID = "org.goko.core.viewer.renderer.gcode.LinearMotionRenderer";

	/**
	 * Constructor
	 * @param command the rendered command
	 */
	public LinearMotionRenderer(LinearMotionCommand command) {
		super(command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#render(org.goko.core.viewer.service.IViewer3DService)
	 */
	@Override
	public void render(IRendererProxy proxy) throws GkException {
		LinearMotionCommand command = getGCodeCommand();
		if(command != null){
			proxy.drawSegment(command.getAbsoluteStartCoordinate(), command.getAbsoluteEndCoordinate());
		}
	}

}
